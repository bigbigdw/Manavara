package com.bigbigdw.manavara.best.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBest
import com.bigbigdw.manavara.best.event.StateBest
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.File
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

            is EventBest.SetGenreDay -> {
                current.copy(
                    genreDay = event.genreDay
                )
            }

            is EventBest.SetGenreWeek -> {
                current.copy(
                    genreDay = event.genreDay,
                    genreDayList = event.genreDayList
                )
            }

            is EventBest.SetItemBookInfo -> {
                current.copy(
                    itemBookInfo = event.itemBookInfo
                )
            }

            is EventBest.SetItemBestInfoTrophyList ->{
                current.copy(
                    itemBestInfoTrophyList = event.itemBestInfoTrophyList,
                    itemBookInfo = event.itemBookInfo
                )
            }

            is EventBest.SetBest -> {
                current.copy(platform = event.platform, bestType = event.bestType, type = event.type, menu = event.menu)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun getBestListTodayJson(context: Context, needDataUpdate: Boolean){

        val state = state.value

        if(needDataUpdate){
            val filePath = File(context.filesDir, "${state.platform}_TODAY_${state.type}.json").absolutePath

            try {
                val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

                val json = Json { ignoreUnknownKeys = true }
                val itemList = json.decodeFromString<List<ItemBookInfo>>(jsonString)

                val todayJsonList = ArrayList<ItemBookInfo>()

                for (item in itemList) {
                    todayJsonList.add(item)
                }

                viewModelScope.launch {
                    events.send(EventBest.SetItemBestInfoList(itemBookInfoList = todayJsonList))
                }
            } catch (e: Exception) {
                getBestListTodayStorage(context = context)
            }
        } else {
            getBestListTodayStorage(context = context)
        }
    }

    private fun getBestListTodayStorage(context: Context){

        val state = state.value

        viewModelScope.launch {
            events.send(EventBest.SetItemBestInfoList(itemBookInfoList = ArrayList()))
        }

        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${state.platform}/${state.type}/DAY/${DBDate.dateMMDD()}.json")
        val localFile = File(context.filesDir, "${state.platform}_TODAY_${state.type}.json")

        todayFileRef.getFile(localFile).addOnSuccessListener {
            val jsonString = localFile.readText(Charset.forName("UTF-8"))
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
            getBestList()
        }

    }

    private fun getBestList() {

        val state = state.value

        val mRootRef =
            FirebaseDatabase.getInstance().reference.child("BEST").child(state.type).child(state.platform)
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

                    viewModelScope.launch {
                        events.send(EventBest.SetItemBestInfoList(itemBookInfoList = bestList))
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getBestWeekTrophy() {

        val state = state.value

        val storage = Firebase.storage
        val storageRef = storage.reference
        val weekTrophyRef = storageRef.child("${state.platform}/${state.type}/WEEK_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        val weekTrophyFile = weekTrophyRef.getBytes(1024 * 1024)

        viewModelScope.launch {
            events.send(EventBest.SetWeekTrophyList(weekTrophyList = ArrayList()))
        }

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

            viewModelScope.launch {
                events.send(EventBest.SetWeekTrophyList(weekTrophyList = weekJsonList))
            }
        }
    }

    fun getBookMap() {

        val state = state.value

        val mRootRef = FirebaseDatabase.getInstance().reference.child("BOOK").child(state.type).child(state.platform)

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

                    viewModelScope.launch {
                        events.send(EventBest.SetItemBookInfoMap(itemBookInfoMap = itemMap))
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getBestWeekListJson(context: Context, needDataUpdate: Boolean){

        val state = state.value

        if(needDataUpdate){
            val filePath = File(context.filesDir, "${state.platform}_WEEK_${state.type}.json").absolutePath

            try {
                val jsonString = File(filePath).readText(Charset.forName("UTF-8"))

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
            } catch (e: Exception) {
                getBestWeekListStorage(context = context)
            }
        } else {
            getBestWeekListStorage(context = context)
        }
    }

    private fun getBestWeekListStorage(context: Context) {

        val state = state.value

        val storage = Firebase.storage
        val storageRef = storage.reference
        val weekRef =   storageRef.child("${state.platform}/${state.type}/WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        val weekFile = File(context.filesDir, "${state.platform}_WEEK_${state.type}.json")

        viewModelScope.launch {
            events.send(EventBest.SetWeekList(weekList = ArrayList()))
        }

        weekRef.getFile(weekFile).addOnSuccessListener {
            val jsonString = weekFile.readText(Charset.forName("UTF-8"))

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

    fun getBestMonthListJson(context: Context, needDataUpdate: Boolean){

        val state = state.value

        if(needDataUpdate){
            val filePath = File(context.filesDir, "${state.platform}_MONTH_${state.type}.json").absolutePath

            try {
                val jsonString = File(filePath).readText(Charset.forName("UTF-8"))
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

            } catch (e: Exception) {
                getBestMonthListStorage(context = context)
            }
        } else {
            getBestMonthListStorage(context = context)
        }
    }

    private fun getBestMonthListStorage(context: Context) {
        val state = state.value

        val storage = Firebase.storage
        val storageRef = storage.reference
        val monthRef = storageRef.child("${state.platform}/${state.type}/MONTH/${DBDate.year()}_${DBDate.month()}.json")
        val monthFile = File(context.filesDir, "${state.platform}_MONTH_${state.type}.json")

        viewModelScope.launch {
            events.send(EventBest.SetMonthList(monthList = ArrayList()))
        }

        monthRef.getFile(monthFile).addOnSuccessListener { bytes ->
            val jsonString = monthFile.readText(Charset.forName("UTF-8"))
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

    fun getBestMonthTrophy() {
        val state = state.value
        val storage = Firebase.storage
        val storageRef = storage.reference
        val monthTrophyRef =  storageRef.child("${state.platform}/${state.type}/MONTH_TROPHY/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")
        val monthTrophyFile = monthTrophyRef.getBytes(1024 * 1024)

        viewModelScope.launch {
            events.send(EventBest.SetMonthTrophyList(monthTrophyList = ArrayList()))
        }

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

            viewModelScope.launch {
                events.send(EventBest.SetMonthTrophyList(monthTrophyList = monthJsonList))
            }
        }
    }

    fun getBookItemInfo(itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventBest.SetItemBookInfo(itemBookInfo = itemBookInfo))
        }
    }

    fun getBookItemWeekTrophy(itemBookInfo: ItemBookInfo){

        val state = state.value

        val weekArray = ArrayList<ItemBestInfo>()

        val mRootRef =
            FirebaseDatabase.getInstance().reference.child("BEST").child(state.type).child(state.platform)
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

                    viewModelScope.launch {
                        events.send(EventBest.SetItemBestInfoTrophyList(itemBestInfoTrophyList = weekArray, itemBookInfo = itemBookInfo))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setBest(
        platform: String = state.value.platform,
        menu: String = state.value.menu,
        bestType: String = state.value.bestType,
        type: String = state.value.type
    ) {
        viewModelScope.launch {
            events.send(EventBest.SetBest(platform = platform, menu = menu, bestType = bestType, type = type))
        }
    }

    fun setItemBestInfoList(todayJsonList: ArrayList<ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventBest.SetItemBestInfoList(itemBookInfoList = todayJsonList))
        }
    }

    fun setItemBookInfoMap(itemBookInfoMap: MutableMap<String, ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventBest.SetItemBookInfoMap(itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setItemBestInfoTrophyList(itemBestInfoTrophyList: ArrayList<ItemBestInfo>,  itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventBest.SetItemBestInfoTrophyList(itemBestInfoTrophyList = itemBestInfoTrophyList, itemBookInfo = itemBookInfo))
        }
    }

}