package com.bigbigdw.manavara.main.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.event.EventBest
import com.bigbigdw.manavara.main.event.StateBest
import com.bigbigdw.manavara.main.models.ItemBestInfo
import com.bigbigdw.manavara.main.models.ItemBookInfo
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import convertItemBookJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.nio.charset.Charset
import java.util.Collections
import javax.inject.Inject

class ViewModelBest @Inject constructor() : ViewModel() {

    private val events = Channel<EventBest>()

    val state: StateFlow<StateBest> = events.receiveAsFlow()
        .runningFold(StateBest(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateBest())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateBest, event: EventBest): StateBest {
        return when(event){
            EventBest.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventBest.SetItemBestInfoList -> {
                current.copy(itemBookInfoList = event.itemBookInfoList)
            }

            is EventBest.SetItemBookInfoList -> {
                current.copy(itemBestInfoList = event.itemBestInfoList)
            }

            is EventBest.SetWeekTrophyList -> {
                current.copy(weekTrophyList = event.weekTrophyList)
            }

            is EventBest.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventBest.SetWeekList -> {
                current.copy(weekList = event.weekList)
            }

            is EventBest.SetMonthList -> {
                current.copy(monthList = event.monthList)
            }

            is EventBest.SetMonthTrophyList -> {
                current.copy(monthTrophyList = event.monthTrophyList)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun getBestListToday(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")

        val todayFile = todayFileRef.getBytes(1024 * 1024)

        todayFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

            val todayJsonList = ArrayList<ItemBookInfo>()

            for (item in itemList) {
                todayJsonList.add(item)
            }

            viewModelScope.launch {
                events.send(EventBest.SetItemBestInfoList(itemBookInfoList = todayJsonList))
            }
        }.addOnFailureListener {
            Log.d("getBestJsonList", "ScreenTodayBest--getBestJsonList--addOnFailureListener == $it")
        }
    }

    fun getBestWeekTrophy(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val weekTrophyRef = storageRef.child("${platform}/${type}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        val weekTrophyFile = weekTrophyRef.getBytes(1024 * 1024)

        weekTrophyFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

            val weekJsonList = ArrayList<ItemBestInfo>()

            for (item in itemList) {
                weekJsonList.add(item)
            }

            val cmpAsc: java.util.Comparator<ItemBestInfo> =
                Comparator { o1, o2 -> o1.total.compareTo(o2.total) }
            Collections.sort(itemList, cmpAsc)

            viewModelScope.launch {
                events.send(EventBest.SetWeekTrophyList(weekTrophyList = weekJsonList))
            }
        }
    }

    fun getBestMapToday(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/DAY/${DBDate.dateMMDD()}.json")

        val todayFile = todayFileRef.getBytes(1024 * 1024)

        todayFile.addOnSuccessListener { bytes ->
            val todayJson = Json { ignoreUnknownKeys = true }
            val itemList = todayJson.decodeFromString<List<ItemBookInfo>>(String(bytes,Charset.forName("UTF-8")))

            val itemMap = mutableMapOf<String, ItemBookInfo>()

            for (item in itemList) {
                itemMap[item.bookCode] = item
            }

            viewModelScope.launch {
                events.send(EventBest.SetItemBookInfoMap(itemBookInfoMap = itemMap))
            }
        }
    }

    fun getBestWeekList(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val weekRef =   storageRef.child("${platform}/${type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        val weekFile = weekRef.getBytes(1024 * 1024)

        weekFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

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

            viewModelScope.launch {
                events.send(EventBest.SetWeekList(weekList = weekJsonList))
            }
        }
    }

    fun getBestMonthList(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val monthRef = storageRef.child("${platform}/${type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")
        val monthFile = monthRef.getBytes(1024 * 1024)

        monthFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))

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

            viewModelScope.launch {
                events.send(EventBest.SetMonthList(monthList = monthJsonList))
            }
        }
    }

    fun getBestMonthTrophy(platform: String, type: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val monthTrophyRef =  storageRef.child("${platform}/${type}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        val monthTrophyFile = monthTrophyRef.getBytes(1024 * 1024)

        monthTrophyFile.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<ItemBestInfo>>(jsonString)

            val monthJsonList = ArrayList<ItemBestInfo>()

            for (item in itemList) {
                monthJsonList.add(item)
            }

            val cmpAsc: java.util.Comparator<ItemBestInfo> =
                Comparator { o1, o2 -> o1.total.compareTo(o2.total) }
            Collections.sort(itemList, cmpAsc)

            viewModelScope.launch {
                events.send(EventBest.SetMonthTrophyList(monthTrophyList = monthJsonList))
            }
        }
    }

}