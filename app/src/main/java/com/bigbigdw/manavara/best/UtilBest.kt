package com.bigbigdw.manavara.best

import android.content.Context
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.Charset

fun getBestListTodayJson(context : Context, needDataUpdate : Boolean, platform : String, type : String, callbacks: (ArrayList<ItemBookInfo>) -> Unit){

    if(needDataUpdate){
        getBestListTodayStorage(context = context, platform = platform, type = type){
            callbacks.invoke(it)
        }
    } else {
        val filePath = File(context.filesDir, "${platform}_TODAY_${type}.json").absolutePath

        try {
            val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val todayJsonList = ArrayList<ItemBookInfo>()

            for (item in itemList) {
                todayJsonList.add(item)
            }

            callbacks.invoke(todayJsonList)
        } catch (e: Exception) {
            getBestListTodayStorage(context = context, platform = platform, type = type){
                callbacks.invoke(it)
            }
        }
    }
}

private fun getBestListTodayStorage(context: Context, platform : String, type : String, callbacks: (ArrayList<ItemBookInfo>) -> Unit){

    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef = storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")
    val localFile = File(context.filesDir, "${platform}_TODAY_${type}.json")

    todayFileRef.getFile(localFile).addOnSuccessListener {
        val jsonString = localFile.readText(Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

        val todayJsonList = ArrayList<ItemBookInfo>()

        for (item in itemList) {
            todayJsonList.add(item)
        }

        callbacks(todayJsonList)
    }.addOnFailureListener {
        getBestList(platform = platform, type = type){
            callbacks.invoke(it)
        }
    }
}

private fun getBestList(platform : String, type : String, callbacks: (ArrayList<ItemBookInfo>) -> Unit) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("DAY")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val bestList = ArrayList<ItemBookInfo>()

                for (book in dataSnapshot.children) {
                    val item: ItemBookInfo? =
                        dataSnapshot.child(book.key ?: "").getValue(ItemBookInfo::class.java)
                    if (item != null) {
                        bestList.add(item)
                    }
                }

                callbacks.invoke(bestList)

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBookMap(platform : String, type : String, callbacks: (MutableMap<String, ItemBookInfo>) -> Unit) {

    val mRootRef = FirebaseDatabase.getInstance().reference.child("BOOK").child(type).child(platform)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val itemMap = mutableMapOf<String, ItemBookInfo>()

                for (item in dataSnapshot.children) {

                    val book = item.getValue(ItemBookInfo::class.java)

                    if (book != null) {
                        itemMap[book.bookCode] = book
                    }
                }

                callbacks.invoke(itemMap)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBookItemWeekTrophy(bookCode: String, platform : String, type : String, callbacks: (ArrayList<ItemBestInfo>) -> Unit){

    val weekArray = ArrayList<ItemBestInfo>()

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_WEEK").child(bookCode)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                for(i in 0..6){
                    weekArray.add(ItemBestInfo())
                }

                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    val value = snapshot.value

                    if (key != null && value != null) {

                        val item = snapshot.getValue(ItemBestInfo::class.java)

                        if (item != null) {
                            weekArray[key.toInt()] = item
                        }
                    }
                }

                callbacks.invoke(weekArray)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}