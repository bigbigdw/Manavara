package com.bigbigdw.manavara.best.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBest
import com.bigbigdw.manavara.best.event.StateBest
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
                current.copy(itemBookInfoList = event.itemBookInfoList, itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventBest.SetItemBookInfoList -> {
                current.copy(itemBestInfoList = event.itemBestInfoList)
            }

            is EventBest.SetWeekTrophyList -> {
                current.copy(weekMonthTrophyList = event.weekTrophyList)
            }

            is EventBest.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventBest.SetWeekMonthList -> {
                current.copy(weekMonthList = event.weekMonthList, itemBookInfoMap = event.itemBookInfoMap, weekMonthTrophyList = event.weekMonthTrophyList)
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

            is EventBest.SetScreen -> {
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type)
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

    fun setScreen(
        platform: String = state.value.platform,
        menu: String = state.value.menu,
        detail: String = state.value.detail,
        type: String = state.value.type
    ) {
        viewModelScope.launch {
            events.send(EventBest.SetScreen(platform = platform, menu = menu, detail = detail, type = type))
        }
    }

    fun setItemBestInfoList(itemBookInfoList: ArrayList<ItemBookInfo>, itemBookInfoMap: MutableMap<String, ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventBest.SetItemBestInfoList(itemBookInfoList = itemBookInfoList, itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setItemBestInfoTrophyList(itemBestInfoTrophyList: ArrayList<ItemBestInfo>,  itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventBest.SetItemBestInfoTrophyList(itemBestInfoTrophyList = itemBestInfoTrophyList, itemBookInfo = itemBookInfo))
        }
    }

    fun setWeekList(
        weekList: ArrayList<ArrayList<ItemBookInfo>>,
        itemBookInfoMap: MutableMap<String, ItemBookInfo>,
        weekTrophyList: ArrayList<ItemBestInfo>
    ) {
        viewModelScope.launch {
            events.send(
                EventBest.SetWeekMonthList(
                    weekMonthList = weekList,
                    itemBookInfoMap = itemBookInfoMap,
                    weekMonthTrophyList = weekTrophyList
                )
            )
        }
    }

}