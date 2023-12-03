package com.bigbigdw.manavara.analyze

import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import convertItemKeyword
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.nio.charset.Charset

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

fun getJsonKeywordWeekList(
    platform: String,
    type: String,
    root : String = "${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json",
    callback: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference =
        storageRef.child("${platform}/${type}/KEYWORD_WEEK/${root}")

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

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

                weekJsonList.add(itemList)
            } catch (e: Exception) {
                weekJsonList.add(ArrayList())
            }
        }

        for (item in sumList) {

            val key = item.key
            val value = item.value

            if (dataMap[key] != null) {
                dataMap[key] = "${dataMap[key]}, $value"
            } else {
                dataMap[key] = value
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

        callback(weekJsonList, arrayList)

    }
}

fun getJsonKeywordMonthList(
    platform: String,
    type: String,
    root: String = "${DBDate.year()}_${DBDate.month()}.json",
    callback: (ArrayList<ArrayList<ItemKeyword>>, ArrayList<ItemKeyword>) -> Unit
) {
    val storage = Firebase.storage
    val storageRef = storage.reference

    val fileRef: StorageReference =
        storageRef.child("${platform}/${type}/KEYWORD_MONTH/${root}")

    val file = fileRef.getBytes(1024 * 1024)

    file.addOnSuccessListener { bytes ->
        val jsonString = String(bytes, Charset.forName("UTF-8"))

        val jsonArray = JSONArray(jsonString)
        val monthJsonList = ArrayList<ArrayList<ItemKeyword>>()
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

                monthJsonList.add(itemList)
            } catch (e: Exception) {
                monthJsonList.add(ArrayList())
            }
        }

        for (item in sumList) {

            val key = item.key
            val value = item.value

            if (dataMap[key] != null) {

                dataMap[key] = "${dataMap[key]}, $value"
            } else {
                dataMap[key] = value
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

        callback(monthJsonList, arrayList)
    }
}