package com.bigbigdw.manavara.best

import android.content.Context
import android.util.Log
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import convertItemBookJson
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
import java.util.Collections

fun getBestListTodayJson(
    context: Context,
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    val filePath = File(context.filesDir, "BEST_DAY_${type}_${platform}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        getBestToday(jsonString){
            callbacks.invoke(it)
        }

    } catch (exception: Exception) {

        getBestListTodayStorage(platform = platform, type = type, context = context) {
            callbacks.invoke(it)
        }
    }
}

fun getBestListTodayStorage(
    context: Context,
    platform: String,
    type: String,
    checkUpdate: Boolean = false,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    try {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef =
            storageRef.child("${platform}/${type}/BEST_DAY/${DBDate.dateMMDD()}.json")
        val todayFile = File(context.filesDir, "BEST_DAY_${type}_${platform}.json")

        todayFileRef.getFile(todayFile).addOnSuccessListener { bytes ->
            val jsonString = todayFile.readText(Charset.forName("UTF-8"))

            getBestToday(jsonString){
                callbacks.invoke(it)
            }

        }.addOnFailureListener { exception ->

            if (checkUpdate) {
                callbacks.invoke(ArrayList())
            } else {
                getBestList(platform = platform, type = type) {
                    callbacks.invoke(it)
                }
            }
        }

    } catch (e: Exception) {
        if (checkUpdate) {
            callbacks.invoke(ArrayList())
        } else {
            getBestList(platform = platform, type = type) {
                callbacks.invoke(it)
            }
        }
    }
}

private fun getBestList(
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

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

fun getBookMapJson(
    platform: String,
    type: String,
    context: Context,
    callbacks: (MutableMap<String, ItemBookInfo>) -> Unit
) {

    try {
        val filePath = File(context.filesDir, "BOOK_${type}_${platform}.json").absolutePath
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        getBookMap(jsonString){
            Log.d("ScreenTodayBest", "getBookMap == ${it.size}")
            callbacks.invoke(it)
        }

    } catch (e: Exception) {
        getBookMapStorage(
            platform = platform,
            type = type,
            context = context
        ) {
            Log.d("ScreenTodayBest", "getBookMap Exception == ${it.size}")
            callbacks.invoke(it)
        }
    }
}

fun getBookMapStorage(
    platform: String,
    type: String,
    context: Context,
    callbacks: (MutableMap<String, ItemBookInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val bookRef = storageRef.child("${platform}/${type}/BOOK/${platform}.json")
    val bookFile = File(context.filesDir, "BOOK_${type}_${platform}.json")

    bookRef.getFile(bookFile).addOnSuccessListener {
        val jsonString = bookFile.readText(Charset.forName("UTF-8")).trimIndent()

        getBookMap(jsonString){
            callbacks.invoke(it)
        }

    }.addOnFailureListener {
        getBookMapRealTimeDB(platform = platform, type = type) {
            callbacks.invoke(it)
        }
    }
}

fun getBookMapRealTimeDB(
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBookInfo>) -> Unit
) {

    val mRootRef = FirebaseDatabase.getInstance().reference.child("BOOK").child(type).child(platform)
    val itemList = mutableMapOf<String, ItemBookInfo>()

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    val value = snapshot.value

                    if (key != null && value != null) {

                        val item = snapshot.getValue(ItemBookInfo::class.java)

                        if (item != null) {
                            itemList[key] = item
                        }
                    }
                }

                callbacks.invoke(itemList)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBookItemWeekTrophy(
    bookCode: String,
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemBestInfo>) -> Unit
) {

    val weekArray = ArrayList<ItemBestInfo>()

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_WEEK").child(bookCode)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                for (i in 0..6) {
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

fun getBestListWeekJson(
    context: Context,
    platform: String,
    type: String,
    bestType: String = "WEEK",
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val filePath = if(bestType == "WEEK"){
        File(context.filesDir, "BEST_WEEK_${type}_${platform}.json").absolutePath
    } else {
        File(context.filesDir, "BEST_MONTH_${type}_${platform}.json").absolutePath
    }

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        getBestWeekMonth(jsonString){
            callbacks.invoke(it)
        }

    } catch (exception: Exception) {

        Log.d("getBestListWeekJson", "getBestListWeekJson exception == $exception")

        if(bestType == "WEEK"){
            getBestWeekListStorage(platform = platform, type = type, context = context) {
                callbacks.invoke(it)
            }
        } else {
            getBestMonthListStorage(platform = platform, type = type, context = context) {
                callbacks.invoke(it)
            }
        }
    }
}

fun getBestWeekListStorage(
    platform: String,
    type: String,
    context: Context,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekRef = storageRef.child("${platform}/${type}/BEST_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val weekFile = File(context.filesDir, "BEST_WEEK_${type}_${platform}.json")

    weekRef.getFile(weekFile).addOnSuccessListener {
        val jsonString = weekFile.readText(Charset.forName("UTF-8"))

        getBestWeekMonth(jsonString){
            callbacks.invoke(it)
        }
    }.addOnSuccessListener{

    }
}

fun getTrophyWeekMonthJson(
    platform: String,
    type: String,
    dayType : String = "WEEK",
    root: String = if (dayType == "WEEK") {
        "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json"
    } else {
        "${DBDate.year()}_${DBDate.month()}.json"
    },
    context : Context,
    callbacks: (ArrayList<ItemBestInfo>) -> Unit
) {

    try {
        val filePath = if(dayType == "WEEK"){
            File(context.filesDir, "TROPHY_WEEK_${type}_${platform}.json").absolutePath
        } else {
            File(context.filesDir, "TROPHY_MONTH_${type}_${platform}.json").absolutePath
        }
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        getTrophyWeek(jsonString){
            callbacks.invoke(it)
        }

    } catch (e: Exception) {

        getTrophyWeekMonthStorage(
            platform = platform,
            type = type,
            context = context,
            root = root,
            dayType = dayType
        ) {
            callbacks.invoke(it)
        }
    }
}

fun getTrophyWeekMonthStorage(
    platform: String,
    type: String,
    dayType: String = "WEEK",
    root: String = if (dayType == "WEEK") {
        "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json"
    } else {
        "${DBDate.year()}_${DBDate.month()}.json"
    },
    context : Context,
    callbacks: (ArrayList<ItemBestInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekTrophyRef = if(dayType == "WEEK"){
        storageRef.child("${platform}/${type}/TROPHY_WEEK/${root}")
    } else {
        storageRef.child("${platform}/${type}/TROPHY_MONTH/${root}")
    }
    val weekTrophyFile = if(dayType == "WEEK"){
        File(context.filesDir, "TROPHY_WEEK_${type}_${platform}.json")
    } else {
        File(context.filesDir, "TROPHY_MONTH_${type}_${platform}.json")
    }

    weekTrophyRef.getFile(weekTrophyFile).addOnSuccessListener { bytes ->
        val jsonString = weekTrophyFile.readText(Charset.forName("UTF-8"))
        getTrophyWeek(jsonString){
            callbacks.invoke(it)
        }
    }.addOnFailureListener {
        Log.d("getBestWeekTrophy", "FAIL $it")
    }
}

fun getBestMonthListStorage(
    platform: String,
    type: String,
    context: Context,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val monthRef = storageRef.child("${platform}/${type}/BEST_MONTH/${DBDate.year()}_${DBDate.month()}.json")
    val monthFile = File(context.filesDir, "BEST_MONTH_${type}_${platform}.json")

    monthRef.getFile(monthFile).addOnSuccessListener {
        val jsonString = monthFile.readText(Charset.forName("UTF-8"))

        getBestWeekMonth(jsonString){
            callbacks.invoke(it)
        }
    }
}

fun areListsEqual(list1: ArrayList<ItemBookInfo>, list2: ArrayList<ItemBookInfo>): Boolean {
    // 두 리스트의 크기가 같아야 함
    if (list1.size != list2.size) {
        return false
    }

    // 각각의 요소들을 비교
    for (i in list1.indices) {
        if (list1[i] != list2[i]) {
            return false
        }
    }

    return true
}

fun getBookMap(jsonString: String, callbacks: (MutableMap<String, ItemBookInfo>) -> Unit){
    val jsonObject = JSONObject(jsonString)
    val itemList = mutableMapOf<String, ItemBookInfo>()

    for (key in jsonObject.keys()) {
        val value = jsonObject.getString(key)
        val item = convertItemBookJson(JSONObject(value))
        itemList[item.bookCode] = item
    }

    callbacks.invoke(itemList)
}

fun getTrophyWeek(jsonString: String, callbacks: (ArrayList<ItemBestInfo>) -> Unit){
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
}

fun getBestWeekMonth(jsonString : String, callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit){
    val jsonArray = JSONArray(jsonString)

    val weekJsonList = ArrayList<ArrayList<ItemBookInfo>>()

    for (i in 0 until jsonArray.length()) {

        try {
            val jsonArrayItem = jsonArray.getJSONArray(i)
            val itemList = ArrayList<ItemBookInfo>()

            for (j in 0 until jsonArrayItem.length()) {

                try {
                    val jsonObject = jsonArrayItem.getJSONObject(j)
                    itemList.add(convertItemBookJson(jsonObject))
                } catch (e: Exception) {
                    itemList.add(ItemBookInfo())
                }
            }

            weekJsonList.add(itemList)
        } catch (e: Exception) {
            weekJsonList.add(ArrayList())
        }
    }

    callbacks.invoke(weekJsonList)
}

fun getBestToday(jsonString : String, callbacks: (ArrayList<ItemBookInfo>) -> Unit){

    val json = Json { ignoreUnknownKeys = true }
    val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

    val todayJsonList = ArrayList<ItemBookInfo>()

    for (item in itemList) {
        todayJsonList.add(item)
    }

    callbacks.invoke(todayJsonList)
}