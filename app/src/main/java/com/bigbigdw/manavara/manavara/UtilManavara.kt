package com.bigbigdw.manavara.manavara

import android.content.Context
import android.util.Log
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.manavara.models.ItemAlert
import com.bigbigdw.manavara.util.DataStoreManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun getPickList(context: Context, uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3", type : String, root : String = "MY", callbacks: (ArrayList<String>, ArrayList<ItemBookInfo>) -> Unit){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch {
        dataStore.getDataStoreString(DataStoreManager.UID).collect{
            val rootRef = FirebaseDatabase.getInstance().reference
                .child("USER")
//                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                .child(it ?: uid)
                .child("PICK")
                .child(root)
                .child(type)

            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory : ArrayList<String> = ArrayList()
                    val pickItemList : ArrayList<ItemBookInfo> = ArrayList()

                    if (dataSnapshot.exists()) {

                        pickCategory.add("전체")

                        for(category in dataSnapshot.children){
                            pickCategory.add(category.key ?: "")

                            for(pickItem in category.children){
                                val item = pickItem.getValue(ItemBookInfo::class.java)

                                if(item != null){
                                    pickItemList.add(item)
                                }
                            }
                        }

                    }

                    callbacks.invoke(pickCategory, pickItemList)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun setSharePickList(
    context: Context,
    listName: String,
    type : String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    pickCategory: ArrayList<String>,
    pickItemList: ArrayList<ItemBookInfo>,
    initTitle: () -> Unit
){

    val filteredMap = mutableMapOf<String, ItemBookInfo>()
    val pickItemListMap = mutableMapOf<String, ItemBookInfo>()
    val dataStore = DataStoreManager(context)

    for(item in pickItemList){
        pickItemListMap[item.bookCode] = item
    }

    for(bookCode in pickCategory){
        val item = pickItemListMap[bookCode]
        if(item != null){
            filteredMap[bookCode] = item
        }
    }

    CoroutineScope(Dispatchers.Main).launch {
        dataStore.getDataStoreString(DataStoreManager.UID).collect{
            FirebaseDatabase.getInstance().reference
                .child("USER")
//                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                .child(it ?: uid)
                .child("PICK")
                .child("SHARE")
                .child(type)
                .child(listName)
                .setValue(filteredMap)

            FirebaseDatabase.getInstance().reference
                .child("PICK_SHARE")
//                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                .child(it ?: uid)
                .child(type)
                .child(listName)
                .setValue(filteredMap)
        }
    }

    initTitle()
}

fun getUserPickList(
    context : Context,
    type: String,
    root : String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    callbacks: (ArrayList<String>, MutableMap<String, ArrayList<ItemBookInfo>>) -> Unit
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch{
        dataStore.getDataStoreString(DataStoreManager.UID).collect{

            val userpickRef = FirebaseDatabase.getInstance().reference
                .child("USER")
//                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                .child(it ?: uid)
                .child("PICK")

            val rootRef = when (root) {
                "MY" -> {
                    userpickRef.child("MY").child(type)
                }
                "MY_SHARE" -> {
                    userpickRef.child("SHARE").child(type)
                }
                "PICK_SHARE" -> {
                    FirebaseDatabase.getInstance().reference
                        .child("PICK_SHARE")
//                        .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                        .child(it ?: uid)
                        .child(type)
                }
                else -> {
                    userpickRef.child("DOWNLOADED").child(type)
                }
            }

            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory = ArrayList<String>()
                    val pickItemList = mutableMapOf<String, ArrayList<ItemBookInfo>>()

                    if (dataSnapshot.exists()) {

                        try {
                            for (category in dataSnapshot.children) {
                                pickCategory.add(category.key ?: "")
                                val itemList = ArrayList<ItemBookInfo>()

                                for (pickItem in category.children) {
                                    val item = pickItem.getValue(ItemBookInfo::class.java)

                                    if (item != null) {
                                        itemList.add(item)
                                    }
                                }

                                pickItemList[category.key ?: ""] = itemList
                            }

                            callbacks.invoke(pickCategory, pickItemList)
                        } catch (e: Exception) {
                            callbacks.invoke(pickCategory, pickItemList)
                        }
                    } else {
                        callbacks.invoke(pickCategory, pickItemList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun getPickShareList(
    context: Context,
    type: String,
    callbacks: (ArrayList<String>, MutableMap<String, ArrayList<ItemBookInfo>>) -> Unit
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch{
        dataStore.getDataStoreString(DataStoreManager.UID).collect{

            val rootRef = FirebaseDatabase.getInstance().reference
                .child("PICK_SHARE")

            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory = ArrayList<String>()
                    val pickItemList = mutableMapOf<String, ArrayList<ItemBookInfo>>()

                    if (dataSnapshot.exists()) {
                        try {

                            for(uid in dataSnapshot.children){
                                for(category in uid.child(type).children){
                                    pickCategory.add(category.key ?: "")
                                    val itemList = ArrayList<ItemBookInfo>()

                                    for (pickItem in category.children) {
                                        val item = pickItem.getValue(ItemBookInfo::class.java)

                                        if (item != null) {
                                            itemList.add(item)
                                        }
                                    }

                                    pickItemList[category.key ?: ""] = itemList
                                }
                            }

                            callbacks.invoke(pickCategory, pickItemList)
                        } catch (e: Exception) {
                            callbacks.invoke(pickCategory, pickItemList)
                        }
                    } else {
                        callbacks.invoke(pickCategory, pickItemList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun editSharePickList(
    context: Context,
    type : String,
    status : String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    listName: String,
    pickItemList: ArrayList<ItemBookInfo>?,
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch {
        dataStore.getDataStoreString(DataStoreManager.UID).collect {
            if(status == "DELETE"){

                val pickUser = FirebaseDatabase.getInstance().reference
                    .child("USER")
                    .child(it ?: uid)
//                    .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                    .child("PICK")

                pickUser.child("DOWNLOADED")
                    .child(type)
                    .child(listName)
                    .removeValue()

                pickUser.child("SHARE")

                    .child(type)
                    .child(listName)
                    .removeValue()

            } else {
                val pickItemListMap = mutableMapOf<String, ItemBookInfo>()

                if (pickItemList != null) {
                    for(item in pickItemList){
                        item.belong = "SHARE_DOWNLOADED"
                        pickItemListMap[item.bookCode] = item
                    }
                }

                FirebaseDatabase.getInstance().reference
                    .child("USER")
//                    .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                    .child(it ?: uid)
                    .child("PICK")
                    .child("DOWNLOADED")
                    .child(type)
                    .child(listName)
                    .setValue(pickItemListMap)
            }
        }
    }
}

fun getUserPickShareListALL(
    context : Context,
    type: String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    callbacks: (ArrayList<String>, MutableMap<String, ArrayList<ItemBookInfo>>) -> Unit
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch{
        dataStore.getDataStoreString(DataStoreManager.UID).collect{
            val rootRef = FirebaseDatabase.getInstance().reference
                .child("USER")
//                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                .child(it ?: uid)
                .child("PICK")


            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory = ArrayList<String>()
                    val pickItemList = mutableMapOf<String, ArrayList<ItemBookInfo>>()
                    val myList = dataSnapshot.child("MY").child(type)
                    val downloadList = dataSnapshot.child("DOWNLOADED").child(type)
                    val shareList = dataSnapshot.child("SHARE").child(type)

                    if (myList.exists()) {

                        pickCategory.add("내 작품들")
                        val itemList = ArrayList<ItemBookInfo>()

                        for (category in myList.children) {

                            for (pickItem in category.children) {
                                val item = pickItem.getValue(ItemBookInfo::class.java)
                                item?.belong = "MY"

                                if (item != null) {
                                    itemList.add(item)
                                }
                            }

                            pickItemList["내 작품들"] = itemList
                        }
                    }

                    if (downloadList.exists()) {

                        for (category in downloadList.children) {

                            pickCategory.add(category.key ?: "")

                            val itemList = ArrayList<ItemBookInfo>()

                            for (pickItem in category.children) {
                                val item = pickItem.getValue(ItemBookInfo::class.java)
                                item?.belong = "DOWNLOADED"

                                if (item != null) {
                                    itemList.add(item)
                                }
                            }

                            pickItemList[category.key ?: ""] = itemList
                        }
                    }

                    if (shareList.exists()) {

                        for (category in shareList.children) {

                            pickCategory.add(category.key ?: "")

                            val itemList = ArrayList<ItemBookInfo>()

                            for (pickItem in category.children) {
                                val item = pickItem.getValue(ItemBookInfo::class.java)
                                item?.belong = "SHARE"

                                if (item != null) {
                                    itemList.add(item)
                                }
                            }

                            pickItemList[category.key ?: ""] = itemList
                        }
                    }

                    callbacks.invoke(pickCategory, pickItemList)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun getFCMList(child: String = "NOTICE", callbacks: (ArrayList<ItemAlert>) -> Unit) {

    val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child(child)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val fcmlist = ArrayList<ItemAlert>()

                for (item in dataSnapshot.children) {
                    val fcm: ItemAlert? =
                        dataSnapshot.child(item.key ?: "").getValue(ItemAlert::class.java)

                    if (fcm != null) {
                        fcmlist.add(fcm)
                    }
                }

                fcmlist.reverse()

                callbacks.invoke(fcmlist)

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}