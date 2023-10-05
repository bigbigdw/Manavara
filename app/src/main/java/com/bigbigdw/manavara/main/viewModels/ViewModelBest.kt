package com.bigbigdw.manavara.main.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.event.EventBest
import com.bigbigdw.manavara.main.event.StateBest
import com.bigbigdw.manavara.main.models.ItemBookInfo
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import convertBestItemData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.charset.Charset
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

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun getBestJsonList(platform : String, genre : String, type: String, date : String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val todayFileRef = storageRef.child("${platform}/${type}/${genre}/DAY/${date}.json")

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
            getBestJsonList(
                platform = platform,
                genre = genre,
                type = type,
                date = DBDate.dateYesterdayMMDD()
            )
        }
    }

}