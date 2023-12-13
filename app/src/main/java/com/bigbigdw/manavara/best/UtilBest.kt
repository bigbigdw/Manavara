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
    checkUpdate: Boolean = false,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    val filePath = File(context.filesDir, "BEST_DAY_${type}_${platform}.json").absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

        val todayJsonList = ArrayList<ItemBookInfo>()

        for (item in itemList) {
            todayJsonList.add(item)
        }

        callbacks.invoke(todayJsonList)
    } catch (exception: Exception) {

        if (checkUpdate) {
            callbacks.invoke(ArrayList())
        } else {
            getBestListTodayStorage(platform = platform, type = type, context = context) {
                callbacks.invoke(it)
            }
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
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val todayJsonList = ArrayList<ItemBookInfo>()

            for (item in itemList) {
                todayJsonList.add(item)
            }

            callbacks(todayJsonList)

        }.addOnFailureListener { exception ->

            Log.d("STORAGE", "ERROR =$exception")

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
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    try {
        val filePath = File(context.filesDir, "BOOK_${type}_${platform}.json").absolutePath
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        val jsonObject = JSONObject(jsonString)
        val itemList = ArrayList<ItemBookInfo>()

        for (key in jsonObject.keys()) {
            val value = jsonObject.getString(key)
            val item = convertItemBookJson(JSONObject(value))
            itemList.add(item)
        }

        Log.d("HIHI", "itemList == $itemList")

        callbacks.invoke(itemList)
    } catch (e: Exception) {
        getBookMapStorage(
            platform = platform,
            type = type,
            context = context
        ) {
            callbacks.invoke(it)
        }
    }
}

fun getBookMapStorage(
    platform: String,
    type: String,
    context: Context,
    callbacks: (ArrayList<ItemBookInfo>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val bookRef =
        storageRef.child("${platform}/${type}/BOOK/${platform}.json")
    val bookFile = File(context.filesDir, "BOOK_${type}_${platform}.json")

    bookRef.getFile(bookFile).addOnSuccessListener {
        val jsonString = bookFile.readText(Charset.forName("UTF-8")).trimIndent()
        val jsonObject = JSONObject(jsonString)
        val itemList = ArrayList<ItemBookInfo>()

        for (key in jsonObject.keys()) {
            val value = jsonObject.getString(key)
            val item = convertItemBookJson(JSONObject(value))
            itemList.add(item)
        }

        callbacks.invoke(itemList)
    }.addOnFailureListener {
        Log.d("HIHI", "FAIL == $it")
    }
}

fun getBookMap(
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ItemBookInfo>) -> Unit
) {

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

fun getBookItemWeekTrophyDialog(
    itemBookInfo: ItemBookInfo,
    type: String,
    platform: String,
    callbacks: (itemBookInfo: ItemBookInfo, itemBestInfoTrophyList: ArrayList<ItemBestInfo>) -> Unit
) {

    val weekArray = ArrayList<ItemBestInfo>()

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("TROPHY_WEEK").child(itemBookInfo.bookCode)

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

                callbacks.invoke(itemBookInfo, weekArray)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getBestListWeekJson(
    context: Context,
    platform: String,
    type: String,
    checkUpdate: Boolean = false,
    bestType : String = "WEEK",
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val filePath = if(bestType == "WEEK"){
        File(context.filesDir, "BEST_WEEK_${type}_${platform}.json").absolutePath
    } else {
        File(context.filesDir, "BEST_MONTH_${type}_${platform}.json").absolutePath
    }

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

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

    } catch (exception: Exception) {

        Log.d("STORAGE", "ERROR =$exception")

        if (checkUpdate) {
            callbacks.invoke(ArrayList())
        } else {
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
}

fun getBestWeekListStorage(
    platform: String,
    type: String,
    context: Context,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val weekRef =
        storageRef.child("${platform}/${type}/BEST_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
    val weekFile = File(context.filesDir, "BEST_WEEK_${type}_${platform}.json")

    weekRef.getFile(weekFile).addOnSuccessListener {
        val jsonString = weekFile.readText(Charset.forName("UTF-8"))

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
    context: Context,
    callbacks: (ArrayList<ArrayList<ItemBookInfo>>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val monthRef =
        storageRef.child("${platform}/${type}/BEST_MONTH/${DBDate.year()}_${DBDate.month()}.json")
    val monthFile = File(context.filesDir, "BEST_MONTH_${type}_${platform}.json")

    monthRef.getFile(monthFile).addOnSuccessListener {
        val jsonString = monthFile.readText(Charset.forName("UTF-8"))
        val jsonArray = JSONArray(jsonString)
        val monthJsonList = ArrayList<ArrayList<ItemBookInfo>>()

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

                monthJsonList.add(itemList)
            } catch (e: Exception) {
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
    val monthTrophyRef = storageRef.child("${platform}/${type}/TROPHY_MONTH/${root}")
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