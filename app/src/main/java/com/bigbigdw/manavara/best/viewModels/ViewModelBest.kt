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

    fun setWeekList(weekList: ArrayList<ArrayList<ItemBookInfo>>){
        viewModelScope.launch {
            events.send(EventBest.SetWeekList(weekList = weekList))
        }
    }

    fun setWeekTrophyList(weekTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventBest.SetWeekTrophyList(weekTrophyList = weekTrophyList))
        }
    }

    fun setMonthList(monthList: ArrayList<ArrayList<ItemBookInfo>>){
        viewModelScope.launch {
            events.send(EventBest.SetMonthList(monthList = monthList))
        }
    }

    fun setMonthTrophyList(monthTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventBest.SetMonthTrophyList(monthTrophyList = monthTrophyList))
        }
    }

}