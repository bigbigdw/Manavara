package com.bigbigdw.manavara.best.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBest
import com.bigbigdw.manavara.best.event.StateBest
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import convertItemBookJson
import convertItemKeyword
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

            is EventBest.SetPlatform -> {
                current.copy(platform = event.platform)
            }

            is EventBest.SetBestType -> {
                current.copy(bestType = event.bestType)
            }

            is EventBest.SetType -> {
                current.copy(type = event.type)
            }

            is EventBest.SetMenu -> {
                current.copy(menu = event.menu)
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

    fun getBestListTodayStorage(context: Context){

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

    fun getBestMapToday() {

        val state = state.value

        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${state.platform}/${state.type}/DAY/${DBDate.dateMMDD()}.json")

        val todayFile = todayFileRef.getBytes(1024 * 1024)

        viewModelScope.launch {
            events.send(EventBest.SetItemBookInfoMap(itemBookInfoMap = mutableMapOf()))
        }

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

    fun getBestWeekListStorage(context: Context) {

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

    fun getBestMonthListStorage(context: Context) {
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

    fun getGenreDay(
        platform: String,
        type: String
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
                                    title = key,
                                    value = value.toString()
                                )
                            )
                        }
                    }

                    val cmpAsc: java.util.Comparator<ItemKeyword> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(arrayList, cmpAsc)

                    viewModelScope.launch {
                        events.send(EventBest.SetGenreDay(genreDay = arrayList))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getGenreDayWeek(
        platform: String,
        type: String
    ) {

        val mRootRef =  FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_WEEK")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataMap = HashMap<String, Any>()
                    val arrayList = ArrayList<ItemKeyword>()

                    for (i in 0..7) {

                        val item = dataSnapshot.child(i.toString())

                        if (item.exists()) {

                            for (snapshot in item.children) {
                                val key = snapshot.key
                                val value = snapshot.value

                                if (key != null && value != null) {

                                    if(dataMap[key] != null){

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
                                title = key,
                                value = value.toString()
                            )
                        )
                    }

                    val cmpAsc: java.util.Comparator<ItemKeyword> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(arrayList, cmpAsc)

                    viewModelScope.launch {
                        events.send(EventBest.SetGenreDay(genreDay = arrayList))
                    }

                } else {
                    Log.d("HIHIHI", "FAIL == NOT EXIST")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getGenreDayMonth(
        platform: String,
        type: String
    ) {

        val mRootRef =  FirebaseDatabase.getInstance().reference.child("BEST").child(type).child(platform).child("GENRE_MONTH")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val dataMap = HashMap<String, Any>()
                    val arrayList = ArrayList<ItemKeyword>()

                    for (i in 1..31) {

                        val item = dataSnapshot.child(i.toString())

                        if (item.exists()) {

                            for (snapshot in item.children) {
                                val key = snapshot.key
                                val value = snapshot.value

                                if (key != null && value != null) {

                                    if(dataMap[key] != null){

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
                                title = key,
                                value = value.toString()
                            )
                        )
                    }

                    val cmpAsc: java.util.Comparator<ItemKeyword> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(arrayList, cmpAsc)

                    viewModelScope.launch {
                        events.send(EventBest.SetGenreDay(genreDay = arrayList))
                    }

                } else {
                    Log.d("HIHIHI", "FAIL == NOT EXIST")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getJsonGenreList(platform: String, type: String){
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

            viewModelScope.launch {
                events.send(EventBest.SetGenreDay(genreDay = jsonList))
            }
        }
    }

    fun getJsonGenreWeekList(platform: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val fileRef: StorageReference =  storageRef.child("${platform}/${type}/GENRE_WEEK/${DBDate.year()}_${DBDate.month()}_${DBDate.getCurrentWeekNumber()}.json")

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

                    val cmpAsc: java.util.Comparator<ItemKeyword> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(itemList, cmpAsc)

                    weekJsonList.add(itemList)
                } catch (e: Exception) {
                    weekJsonList.add(ArrayList())
                }
            }

            for(item in sumList){

                val key = item.title
                val value = item.value

                if(dataMap[key] != null){

                    val preValue = dataMap[key] as Long
                    val currentValue = value.toLong()

                    dataMap[key] = preValue + currentValue
                } else {
                    dataMap[key] = value.toLong()
                }
            }

            val arrayList = ArrayList<ItemKeyword>()

            for ((key, value) in dataMap) {
                arrayList.add(
                    ItemKeyword(
                        title = key,
                        value = value.toString()
                    )
                )
            }

            val cmpAsc: java.util.Comparator<ItemKeyword> =
                Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
            Collections.sort(arrayList, cmpAsc)

            viewModelScope.launch {
                events.send(EventBest.SetGenreWeek(genreDayList = weekJsonList, genreDay = arrayList))
            }
        }
    }

    fun getJsonGenreMonthList(platform: String, type: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val fileRef: StorageReference = storageRef.child("${platform}/${type}/GENRE_MONTH/${DBDate.year()}_${DBDate.month()}.json")

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

                    val cmpAsc: java.util.Comparator<ItemKeyword> =
                        Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
                    Collections.sort(itemList, cmpAsc)

                    weekJsonList.add(itemList)
                } catch (e: Exception) {
                    weekJsonList.add(ArrayList())
                }
            }

            for(item in sumList){

                val key = item.title
                val value = item.value

                if(dataMap[key] != null){

                    val preValue = dataMap[key] as Long
                    val currentValue = value.toLong()

                    dataMap[key] = preValue + currentValue
                } else {
                    dataMap[key] = value.toLong()
                }
            }

            val arrayList = ArrayList<ItemKeyword>()

            for ((key, value) in dataMap) {
                arrayList.add(
                    ItemKeyword(
                        title = key,
                        value = value.toString()
                    )
                )
            }

            val cmpAsc: java.util.Comparator<ItemKeyword> =
                Comparator { o1, o2 -> o2.value.toInt().compareTo(o1.value.toInt()) }
            Collections.sort(arrayList, cmpAsc)

            viewModelScope.launch {
                events.send(EventBest.SetGenreWeek(genreDayList = weekJsonList, genreDay = arrayList))
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

    fun setPlatform(platform : String){
        viewModelScope.launch {
            events.send(EventBest.SetPlatform(platform = platform))
        }
    }

    fun setMenu(menu : String){
        viewModelScope.launch {
            events.send(EventBest.SetMenu(menu = menu))
        }
    }

    fun setBestType(bestType : String){
        viewModelScope.launch {
            events.send(EventBest.SetBestType(bestType = bestType))
        }
    }

    fun setType(type : String){
        viewModelScope.launch {
            events.send(EventBest.SetType(type = type))
        }
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

}