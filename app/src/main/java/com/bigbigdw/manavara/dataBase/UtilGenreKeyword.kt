package com.bigbigdw.manavara.dataBase

import android.util.Log
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import convertItemKeyword
import kotlinx.serialization.json.Json
import org.json.JSONArray
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

fun getJsonGenreList(platform: String, type: String, callbacks: (ArrayList<ItemKeyword>) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef = storageRef.child("${platform}/${type}/GENRE_DAY/${DBDate.dateMMDD()}.json")

    val todayFile = todayFileRef.getBytes(1024 * 1024)

    todayFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemKeyword>>(jsonString)

        val jsonList = ArrayList<ItemKeyword>()

        for (item in itemList) {
            jsonList.add(item)
        }

        val cmpAsc: java.util.Comparator<ItemKeyword> =
            Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
        Collections.sort(jsonList, cmpAsc)

        callbacks.invoke(jsonList)
    }
}

fun getJsonGenreWeekList(
    platform: String,
    type: String,
    dataType : String = "GENRE",
    root: String = "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json",
    callbacks: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference = if(dataType == "GENRE"){
        storageRef.child("${platform}/${type}/GENRE_WEEK/${root}")
    } else {
        storageRef.child("${platform}/${type}/KEYWORD_WEEK/${root}")
    }

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)
        val weekJsonList = ArrayList<ArrayList<ItemKeyword>>()
        val sumList = ArrayList<ItemKeyword>()

        val dataMap = HashMap<String, Long>()

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
                    val preValue = (dataMap[key] as String).split("\\s+".toRegex()).count { it.isNotEmpty() }
                    val currentValue = value.split("\\s+".toRegex()).count { it.isNotEmpty() }.toLong()

                    dataMap[key] = preValue + currentValue

                } else {
                    val preValue = dataMap[key] as Long
                    val currentValue = value.toLong()

                    dataMap[key] = preValue + currentValue
                }

            } else {

                if(dataType == "KEYWORD"){
                    val wordCount = value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                    dataMap[key] = wordCount.toLong()
                } else {
                    dataMap[key] = value.toLong()
                }
            }
        }

        val arrayList = ArrayList<ItemKeyword>()

        for ((key, value) in dataMap) {
            arrayList.add(
                ItemKeyword(
                    key = key,
                    value = value.toString()
                )
            )
        }

        val cmpAsc: java.util.Comparator<ItemKeyword> =
            Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
        Collections.sort(arrayList, cmpAsc)

        callbacks.invoke(weekJsonList, arrayList)

    }
}

fun getJsonGenreMonthList(
    platform: String,
    type: String,
    dataType : String = "GENRE",
    root: String = "${DBDate.year()}_${DBDate.month()}.json",
    callbacks: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference = if(dataType == "GENRE"){
        storageRef.child("${platform}/${type}/GENRE_MONTH/${root}")
    } else {
        storageRef.child("${platform}/${type}/KEYWORD_MONTH/${root}")
    }

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)
        val weekJsonList = ArrayList<ArrayList<ItemKeyword>>()
        val sumList = ArrayList<ItemKeyword>()

        val dataMap = HashMap<String, Long>()

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
                    val preValue = (dataMap[key] as String).split("\\s+".toRegex()).count { it.isNotEmpty() }
                    val currentValue = value.split("\\s+".toRegex()).count { it.isNotEmpty() }.toLong()

                    dataMap[key] = preValue + currentValue

                } else {
                    val preValue = dataMap[key] as Long
                    val currentValue = value.toLong()

                    dataMap[key] = preValue + currentValue
                }

            } else {

                if(dataType == "KEYWORD"){
                    val wordCount = value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                    dataMap[key] = wordCount.toLong()
                } else {
                    dataMap[key] = value.toLong()
                }
            }
        }

        val arrayList = ArrayList<ItemKeyword>()

        for ((key, value) in dataMap) {
            arrayList.add(
                ItemKeyword(
                    key = key,
                    value = value.toString()
                )
            )
        }

        val cmpAsc: java.util.Comparator<ItemKeyword> =
            Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
        Collections.sort(arrayList, cmpAsc)

        callbacks.invoke(weekJsonList, arrayList)
    }
}

fun getGenreDay(
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemKeyword>) -> Unit
) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("GENRE_DAY")

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

fun getGenreDayWeek(
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemKeyword>) -> Unit
) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("GENRE_WEEK")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val dataMap = HashMap<String, Any>()
                val arrayList = ArrayList<ItemKeyword>()

                for (i in 0..7) {

                    val item = dataSnapshot.child(i.toString())

                    if (item.exists()) {

                        for (snapshot in item.children) {
                            val key = snapshot.key
                            val value = snapshot.value

                            if (key != null && value != null) {

                                if (dataMap[key] != null) {

                                    val preValue = dataMap[key] as Long
                                    val currentValue = value as Long

                                    dataMap[key] = preValue + currentValue
                                } else {
                                    dataMap[key] = value
                                }
                            }
                        }
                    }
                }

                for ((key, value) in dataMap) {
                    arrayList.add(
                        ItemKeyword(
                            key = key,
                            value = value.toString()
                        )
                    )
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

fun getGenreDayMonth(
    platform: String,
    type: String,
    callbacks: (ArrayList<ItemKeyword>) -> Unit
) {

    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform)
            .child("GENRE_MONTH")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val dataMap = HashMap<String, Any>()
                val arrayList = ArrayList<ItemKeyword>()

                for (i in 1..31) {

                    val item = dataSnapshot.child(i.toString())

                    if (item.exists()) {

                        for (snapshot in item.children) {
                            val key = snapshot.key
                            val value = snapshot.value

                            if (key != null && value != null) {

                                if (dataMap[key] != null) {

                                    val preValue = dataMap[key] as Long
                                    val currentValue = value as Long

                                    dataMap[key] = preValue + currentValue
                                } else {
                                    dataMap[key] = value
                                }
                            }
                        }
                    }
                }

                for ((key, value) in dataMap) {
                    arrayList.add(
                        ItemKeyword(
                            key = key,
                            value = value.toString()
                        )
                    )
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

fun getJsonKeywordList(platform: String, type: String, callback: (ArrayList<ItemKeyword>) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val todayFileRef = storageRef.child("${platform}/${type}/KEYWORD_DAY/${DBDate.dateMMDD()}.json")

    val todayFile = todayFileRef.getBytes(1024 * 1024)

    todayFile.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))
        val json = Json { ignoreUnknownKeys = true }
        val itemList = json.decodeFromString<List<ItemKeyword>>(jsonString)

        val jsonList = ArrayList<ItemKeyword>()

        for (item in itemList) {
            jsonList.add(item)
        }

        callback(jsonList)
    }
}

