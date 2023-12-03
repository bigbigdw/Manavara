package com.bigbigdw.manavara.best

import android.content.Context
import android.util.Log
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.DataStoreManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import convertItemBookJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.File
import java.nio.charset.Charset
import java.util.Collections

fun getBestListTodayJson(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.IO).launch {
        dataStore.getDataStoreBoolean(DataStoreManager.NEED_UPDATE).collect { value ->

            if (value == true) {
                getBestListTodayStorage(platform = platform, type = type){
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
                    getBestList(platform = platform, type = type) {
                        callbacks.invoke(it)
                    }
                }
            }
        }
    }
}

fun getBestListTodayStorage(
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
){

    try {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/BEST_DAY/${DBDate.dateMMDD()}.json")

        todayFileRef.getBytes(1024 * 1024).addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val todayJsonList = ArrayList<ItemBookInfo>()

            for (item in itemList) {
                todayJsonList.add(item)
            }

            callbacks(todayJsonList)

        }.addOnFailureListener {
            getBestList(platform = platform, type = type) {
                callbacks.invoke(it)
            }
        }

    } catch (e: Exception) {
        getBestList(platform = platform, type = type) {
            callbacks.invoke(it)
        }
    }
}

private fun getBestList(platform : String, type : String, callbacks: (ArrayList<ItemBookInfo>) -> Unit) {

    val mRootRef = FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("DAY")

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

fun getBookItemWeekTrophyDialog(itemBookInfo: ItemBookInfo, type : String, platform: String, callbacks: (itemBookInfo: ItemBookInfo,  itemBestInfoTrophyList: ArrayList<ItemBestInfo>) -> Unit){

    val weekArray = ArrayList<ItemBestInfo>()

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_WEEK").child(itemBookInfo.bookCode)

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

                callbacks.invoke(itemBookInfo, weekArray)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBestWeekListStorage(
    platform: String,
    type: String,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit,
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekRef = storageRef.child("${platform}/${type}/BEST_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

    weekRef.getBytes(1024 * 1024).addOnSuccessListener {
        val jsonString = String(it, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)

        val weekJsonList = ArrayList<ArrayList<ItemBookInfo>>()

        for (i in 0 until jsonArray.length()) {

            try{
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBookInfo>()

                for (j in 0 until jsonArrayItem.length()) {

                    try{
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemBookJson(jsonObject))
                    }catch (e : Exception){
                        itemList.add(ItemBookInfo())
                    }
                }

                weekJsonList.add(itemList)
            } catch (e : Exception){
                weekJsonList.add(ArrayList())
            }
        }

        callbacks.invoke(weekJsonList)
    }
}

fun getBestWeekTrophy(
    platform: String,
    type: String,
    root: String = "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json",
    callbacks: (ArrayList<ItemBestInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekTrophyRef = storageRef.child("${platform}/${type}/TROPHY_WEEK/${root}")
    val weekTrophyFile = weekTrophyRef.getBytes(1024 * 1024)

    weekTrophyFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

        val cmpAsc: java.util.Comparator<ItemBestInfo> =
            Comparator { o1, o2 -> o2.total.compareTo(o1.total) }
        Collections.sort(itemList, cmpAsc)

        val weekJsonList = ArrayList<ItemBestInfo>()

        for (item in itemList) {
            weekJsonList.add(item)
        }

        callbacks.invoke(weekJsonList)
    }.addOnFailureListener {
        Log.d("getBestWeekTrophy", "FAIL $it")
    }
}

fun getBestMonthListStorage(
    platform: String,
    type: String,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val monthRef = storageRef.child("${platform}/${type}/BEST_MONTH/${DBDate.year()}_${DBDate.month()}.json")

    monthRef.getBytes(1024 * 1024).addOnSuccessListener {
        val jsonString = String(it, Charset.forName("UTF-8"))
        val jsonArray = JSONArray(jsonString)
        val monthJsonList = ArrayList<ArrayList<ItemBookInfo>>()

        for (i in 0 until jsonArray.length()) {

            try{
                val jsonArrayItem = jsonArray.getJSONArray(i)
                val itemList = ArrayList<ItemBookInfo>()

                for (j in 0 until jsonArrayItem.length()) {

                    try{
                        val jsonObject = jsonArrayItem.getJSONObject(j)
                        itemList.add(convertItemBookJson(jsonObject))
                    }catch (e : Exception){
                        itemList.add(ItemBookInfo())
                    }
                }

                monthJsonList.add(itemList)
            } catch (e : Exception){
                monthJsonList.add(ArrayList())
            }
        }

        callbacks.invoke(monthJsonList)
    }
}

fun getBestMonthTrophy(
    platform: String,
    type: String,
    root: String = "${DBDate.year()}_${DBDate.month()}.json",
    callbacks: (ArrayList<ItemBestInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val monthTrophyRef =  storageRef.child("${platform}/${type}/TROPHY_MONTH/${root}")
    val monthTrophyFile = monthTrophyRef.getBytes(1024 * 1024)

    monthTrophyFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

        val monthJsonList = ArrayList<ItemBestInfo>()

        val cmpAsc: java.util.Comparator<ItemBestInfo> =
            Comparator { o1, o2 -> o2.total.compareTo(o1.total) }
        Collections.sort(itemList, cmpAsc)

        for (item in itemList) {
            monthJsonList.add(item)
        }

        callbacks.invoke(monthJsonList)
    }.addOnFailureListener {
        Log.d("HIHI", "FAIL == $it")
    }
}
