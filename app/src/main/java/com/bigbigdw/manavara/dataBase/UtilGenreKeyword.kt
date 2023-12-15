package com.bigbigdw.manavara.dataBase

import android.content.Context
import android.util.Log
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import convertItemKeyword
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.File
import java.nio.charset.Charset
import java.util.Collections

fun getJsonFiles(
    platform: String,
    type: String,
    root: String,
    callbacks: (List<String>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val testRef = storageRef.child("${platform}/${type}/$root")

    testRef.listAll().addOnSuccessListener { result ->
        val fileList = result.items.map { it.name }
        callbacks.invoke(fileList)
    }.addOnFailureListener {
        // Handle the error
    }
}

fun getGenreKeywordJson(
    context: Context,
    platform: String,
    type: String,
    dataType: String,
    callbacks: (ArrayList<ItemKeyword>) -> Unit
) {

    val filePath = getGenreKeywordFile(
        dataType = dataType,
        dayType = "TODAY",
        context = context,
        platform = platform,
        type = type).absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        callbacks.invoke(getGenreKeywordToday(jsonString))
    } catch (exception: Exception) {

        getGenreKeywordStorage(platform = platform, type = type, context = context, dataType = dataType) {
            callbacks.invoke(it)
        }
    }
}

fun getGenreKeywordStorage(context : Context, dataType : String, platform: String, type: String, callbacks: (ArrayList<ItemKeyword>) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val todayFileRef = getGenreKeywordPath(
        dataType = dataType,
        dayType = "TODAY",
        storageRef = storageRef,
        platform = platform,
        type = type,
    )

    val todayFile = getGenreKeywordFile(
        context = context,
        dataType = dataType,
        dayType = "TODAY",
        platform = platform,
        type = type,
    )

    todayFileRef.getFile(todayFile).addOnSuccessListener {
        val jsonString = todayFile.readText(Charset.forName("UTF-8"))

        callbacks.invoke(getGenreKeywordToday(jsonString))
    }.addOnFailureListener {

        Log.d("KEYWORD", "getGenreKeywordStorage == $it")

        getGenreKeywordToday(platform = platform, type = type, dataType = dataType, dayType = "TODAY"){
            callbacks.invoke(it)
        }
    }
}


fun getGenreListWeekJson(
    context: Context,
    platform: String,
    type: String,
    dayType: String = "WEEK",
    dataType: String = "GENRE",
    root: String = if (dayType == "WEEK") {
        "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json"
    } else {
        "${DBDate.year()}_${DBDate.month()}.json"
    },
    callbacks: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {

    val filePath = getGenreKeywordFile(
        dataType = dataType,
        dayType = dayType,
        context = context,
        platform = platform,
        type = type,
    ).absolutePath

    try {
        val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

        getGenreKeywordWeekMonth(dataType = dataType, jsonString = jsonString) { weekJsonList, arrayList ->
            callbacks.invoke(weekJsonList, arrayList)
        }

    } catch (exception: Exception) {

        Log.d("STORAGE", "ERROR = $exception")

        getJsonGenreWeekList(
            context = context,
            platform = platform,
            type = type,
            dayType = dayType,
            dataType = dataType,
            root = root
        ) { genreWeekList, genreList ->
            callbacks.invoke(genreWeekList, genreList)
        }
    }
}

fun getJsonGenreWeekList(
    context : Context,
    platform: String,
    type: String,
    dayType : String = "WEEK",
    dataType : String = "GENRE",
    root: String = if (dayType == "WEEK") {
        "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json"
    } else {
        "${DBDate.year()}_${DBDate.month()}.json"
    },
    callbacks: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference = getGenreKeywordPath(
        dataType = dataType,
        dayType = dayType,
        storageRef = storageRef,
        platform = platform,
        type = type,
        root = root
    )

    val file = getGenreKeywordFile(
        dataType = dataType,
        dayType = dayType,
        context = context,
        platform = platform,
        type = type
    )

    fileRef.getFile(file).addOnSuccessListener { bytes ->
        val jsonString = file.readText(Charset.forName("UTF-8"))

        getGenreKeywordWeekMonth(dataType = dataType, jsonString = jsonString) { weekJsonList, arrayList ->
            callbacks.invoke(weekJsonList, arrayList)
        }
    }.addOnFailureListener {

        Log.d("GENRE", "getJsonGenreWeekList == $it")

        getGenreKeywordRealtimeDB(platform = platform, type = type, dataType = dataType, dayType ){ weekJsonList, arrayList ->
            callbacks.invoke(weekJsonList, arrayList)
        }
    }
}

fun getGenreKeywordToday(
    platform: String,
    type: String,
    dataType : String,
    dayType : String,
    callbacks: (ArrayList<ItemKeyword>) -> Unit
) {

    val mRootRef = getGenreKeywordRootRef(
        dataType = dataType,
        dayType = dayType,
        platform = platform,
        type = type
    )

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val arrayList = ArrayList<ItemKeyword>()

                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key
                    val value = snapshot.value

                    if (key != null && value != null) {

                        arrayList.add(
                            ItemKeyword(
                                key = key,
                                value = value.toString()
                            )
                        )
                    }
                }

                val cmpAsc: java.util.Comparator<ItemKeyword> =
                    Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                Collections.sort(arrayList, cmpAsc)

                callbacks.invoke(arrayList)

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getGenreKeywordRealtimeDB(
    platform: String,
    type: String,
    dataType : String,
    dayType : String,
    callbacks: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {

    val mRootRef = getGenreKeywordRootRef(
        dataType = dataType,
        dayType = dayType,
        platform = platform,
        type = type
    )

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val weekJsonList = ArrayList<ArrayList<ItemKeyword>>()
                val sumList = ArrayList<ItemKeyword>()
                val dataMap = HashMap<String, String>()

                for (i in 0 until dataSnapshot.childrenCount) {

                    val item = dataSnapshot.child(i.toString())
                    val itemList = ArrayList<ItemKeyword>()

                    if (item.exists()) {

                        for (snapshot in item.children) {

                            if (snapshot != null) {

                                val snapShotItem = ItemKeyword(
                                    key = snapshot.key ?: "",
                                    value = snapshot.value.toString()
                                )

                                itemList.add(snapShotItem)
                                sumList.add(snapShotItem)
                            }
                        }
                    }

                    if(dataType == "GENRE"){
                        val cmpAsc: java.util.Comparator<ItemKeyword> =
                            Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                        Collections.sort(itemList, cmpAsc)
                    } else {

                        val cmpAsc: java.util.Comparator<ItemKeyword> =
                            Comparator { o1, o2 -> o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() }
                                .compareTo(o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() }) }
                        Collections.sort(itemList, cmpAsc)
                    }

                    weekJsonList.add(itemList)

                }

                for (item in sumList) {

                    val key = item.key
                    val value = item.value

                    if (dataMap[key] != null) {

                        if(dataType == "KEYWORD"){
                            val preValue = dataMap[key].toString().split("\\s+".toRegex()).count { it.isNotEmpty() }.toLong()
                            val currentValue = value.split("\\s+".toRegex()).count { it.isNotEmpty() }.toLong()

                            dataMap[key] = (preValue + currentValue).toString()

                        } else {
                            val preValue = dataMap[key]?.toLong() ?: 0L
                            val currentValue = value.toLong()

                            dataMap[key] = (preValue + currentValue).toString()
                        }

                    } else {

                        if(dataType == "KEYWORD"){
                            val wordCount = value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                            dataMap[key] = wordCount.toString()
                        } else {
                            dataMap[key] = value
                        }
                    }
                }

                val arrayList = ArrayList<ItemKeyword>()

                for ((key, value) in dataMap) {
                    arrayList.add(
                        ItemKeyword(
                            key = key,
                            value = value
                        )
                    )
                }

                val cmpAsc: java.util.Comparator<ItemKeyword> =
                    Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                Collections.sort(arrayList, cmpAsc)

                callbacks.invoke(weekJsonList, arrayList)

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getGenreMap(
    menuType : String,
    platform: String,
    type: String,
    callbacks: (MutableMap<String, ArrayList<ItemKeyword>>) -> Unit
) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child(menuType).child(type).child(platform)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val itemMap = mutableMapOf<String, ArrayList<ItemKeyword>>()

                for (genre in dataSnapshot.children) {

                    val itemMapArray = ArrayList<ItemKeyword>()

                    for (item in genre.children) {

                        val result = item.getValue(ItemKeyword::class.java)

                        if (result != null) {
                            itemMapArray.add(result)
                        }
                    }

                    if (genre != null) {
                        itemMap[genre.key ?: ""] = itemMapArray
                    }
                }

                callbacks.invoke(itemMap)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("FAIL", "databaseError == $databaseError")
        }
    })
}

fun getGenreKeywordToday(jsonString: String) : ArrayList<ItemKeyword> {
    val todayJsonList = ArrayList<ItemKeyword>()
    val json = Json { ignoreUnknownKeys = true }
    val itemList = json.decodeFromString<List<ItemKeyword>>(jsonString)

    for (item in itemList) {
        todayJsonList.add(item)
    }

    val cmpAsc: java.util.Comparator<ItemKeyword> =
        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
    Collections.sort(todayJsonList, cmpAsc)

    return todayJsonList
}

fun getGenreKeywordWeekMonth(dataType: String, jsonString: String, callbacks: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit) {
    val jsonArray = JSONArray(jsonString)
    val weekJsonList = ArrayList<ArrayList<ItemKeyword>>()
    val sumList = ArrayList<ItemKeyword>()
    val dataMap = HashMap<String, String>()

    for (i in 0 until jsonArray.length()) {

        try {
            val jsonArrayItem = jsonArray.getJSONArray(i)
            val itemList = ArrayList<ItemKeyword>()

            for (j in 0 until jsonArrayItem.length()) {

                try {
                    val jsonObject = jsonArrayItem.getJSONObject(j)
                    itemList.add(convertItemKeyword(jsonObject))
                    sumList.add(convertItemKeyword(jsonObject))
                } catch (e: Exception) {
                    itemList.add(ItemKeyword())
                }
            }

            if(dataType == "GENRE"){
                val cmpAsc: java.util.Comparator<ItemKeyword> =
                    Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                Collections.sort(itemList, cmpAsc)
            } else {

                val cmpAsc: java.util.Comparator<ItemKeyword> =
                    Comparator { o1, o2 -> o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() }
                        .compareTo(o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() }) }
                Collections.sort(itemList, cmpAsc)
            }

            weekJsonList.add(itemList)
        } catch (e: Exception) {
            weekJsonList.add(ArrayList())
        }
    }

    for (item in sumList) {

        val key = item.key
        val value = item.value

        if (dataMap[key] != null) {

                if(dataType == "KEYWORD"){
                    val preValue = dataMap[key].toString().split("\\s+".toRegex()).count { it.isNotEmpty() }.toLong()
                    val currentValue = value.split("\\s+".toRegex()).count { it.isNotEmpty() }.toLong()

                    dataMap[key] = (preValue + currentValue).toString()

                } else {
                    val preValue = dataMap[key]?.toLong() ?: 0L
                    val currentValue = value.toLong()

                    dataMap[key] = (preValue + currentValue).toString()
                }

            } else {

                if(dataType == "KEYWORD"){
                    val wordCount = value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                    dataMap[key] = wordCount.toString()
                } else {
                dataMap[key] = value
            }
        }
    }

    val arrayList = ArrayList<ItemKeyword>()

    for ((key, value) in dataMap) {
        arrayList.add(
            ItemKeyword(
                key = key,
                value = value
            )
        )
    }

    val cmpAsc: java.util.Comparator<ItemKeyword> =
        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
    Collections.sort(arrayList, cmpAsc)

    callbacks.invoke(weekJsonList, arrayList)
}

fun getGenreKeywordPath(
    dataType: String,
    dayType: String,
    storageRef: StorageReference,
    platform: String,
    type: String,
    root : String = "${DBDate.dateMMDD()}.json",
): StorageReference {
    val fileRef: StorageReference = if(dataType == "GENRE"){

        when (dayType) {
            "TODAY" -> {
                storageRef.child("${platform}/${type}/GENRE_TODAY/${root}")
            }
            "WEEK" -> {
                storageRef.child("${platform}/${type}/GENRE_WEEK/${root}")
            }
            else -> {
                storageRef.child("${platform}/${type}/GENRE_MONTH/${root}")
            }
        }

    } else {
        when (dayType) {
            "TODAY" -> {
                storageRef.child("${platform}/${type}/KEYWORD_TODAY/${root}")
            }
            "WEEK" -> {
                storageRef.child("${platform}/${type}/KEYWORD_WEEK/${root}")
            }
            else -> {
                storageRef.child("${platform}/${type}/KEYWORD_MONTH/${root}")
            }
        }
    }

    return fileRef
}

fun getGenreKeywordFile(
    dataType: String,
    dayType: String,
    context: Context,
    platform: String,
    type: String
): File {
    val file = if(dataType == "GENRE"){

        when (dayType) {
            "TODAY" -> {
                File(context.filesDir, "GENRE_DAY_${type}_${platform}.json")
            }
            "WEEK" -> {
                File(context.filesDir, "GENRE_WEEK_${type}_${platform}.json")
            }
            else -> {
                File(context.filesDir, "GENRE_MONTH_${type}_${platform}.json")
            }
        }

    } else {

        when (dayType) {
            "TODAY" -> {
                File(context.filesDir, "KEYWORD_DAY_${type}_${platform}.json")
            }
            "WEEK" -> {
                File(context.filesDir, "KEYWORD_WEEK_${type}_${platform}.json")
            }
            else -> {
                File(context.filesDir, "KEYWORD_MONTH_${type}_${platform}.json")
            }
        }

    }

    return file
}

fun getGenreKeywordRootRef(
    dataType: String,
    dayType: String,
    platform: String,
    type: String
): DatabaseReference {
    val mRootRef = if(dataType == "GENRE"){
        when (dayType) {
            "TODAY" -> {
                FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_DAY")
            }
            "WEEK" -> {
                FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_WEEK")
            }
            else -> {
                FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_MONTH")
            }
        }

    } else {

        when (dayType) {
            "TODAY" -> {
                FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("KEYWORD_DAY")
            }
            "WEEK" -> {
                FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("KEYWORD_WEEK")
            }
            else -> {
                FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("KEYWORD_WEEK")
            }
        }

    }

    return mRootRef
}

