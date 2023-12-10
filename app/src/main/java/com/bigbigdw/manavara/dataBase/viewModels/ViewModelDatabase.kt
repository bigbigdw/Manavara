package com.bigbigdw.manavara.dataBase.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.dataBase.event.EventDataBase
import com.bigbigdw.manavara.dataBase.event.StateDataBase
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelDatabase @Inject constructor() : ViewModel() {

    private val events = Channel<EventDataBase>()

    val state: StateFlow<StateDataBase> = events.receiveAsFlow()
        .runningFold(StateDataBase(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateDataBase())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateDataBase, event: EventDataBase): StateDataBase {
        return when (event) {
            EventDataBase.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventDataBase.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventDataBase.SetItemBookInfo -> {
                current.copy(itemBookInfo = event.itemBookInfo)
            }

            is EventDataBase.SetItemBestInfoTrophyList -> {
                current.copy(itemBookInfo = event.itemBookInfo, itemBestInfoTrophyList = event.itemBestInfoTrophyList)
            }

            is EventDataBase.SetJsonNameList -> {
                current.copy(jsonNameList = event.jsonNameList)
            }

            is EventDataBase.SetScreen -> {
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type)
            }

            is EventDataBase.SetGenreList -> {
                current.copy(genreList = event.genreList)
            }

            is EventDataBase.SetGenreWeekList -> {
                current.copy(genreWeekList = event.genreWeekList, genreList = event.genreList)
            }

            is EventDataBase.SetWeekTrophyList -> {
                current.copy(weekTrophyList = event.weekTrophyList)
            }

            is EventDataBase.SetFilteredList -> {
                current.copy(filteredList = event.filteredList)
            }

            is EventDataBase.SetDate -> {
                current.copy(week = event.week, month = event.month)
            }

            is EventDataBase.SetKeywordDay -> {
                current.copy(keywordDay = event.keywordDay)
            }

            is EventDataBase.SetKeywordWeek -> {
                current.copy(keywordDay = event.keywordDay, keywordDayList = event.keywordDayList)
            }

            is EventDataBase.SetSearchQuery -> {
                current.copy(searchQuery = event.searchQuery)
            }

            is EventDataBase.SetSearch -> {
                current.copy(filteredList = event.filteredList, itemBookInfoMap = event.itemBookInfoMap)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setSearch(filteredList: ArrayList<ItemBookInfo>, itemBookInfoMap: MutableMap<String, ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventDataBase.SetSearch(filteredList = filteredList, itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setSearchQuery(searchQuery: String){
        viewModelScope.launch {
            events.send(EventDataBase.SetSearchQuery(searchQuery = searchQuery))
        }
    }

    fun setItemBookInfoMap(itemBookInfoMap: MutableMap<String, ItemBookInfo>) {
        viewModelScope.launch {
            events.send(EventDataBase.SetItemBookInfoMap(itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setItemBookInfo(itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventDataBase.SetItemBookInfo(itemBookInfo = itemBookInfo))
        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo,  itemBestInfoTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventDataBase.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setJsonNameList( jsonNameList: List<String>){
        viewModelScope.launch {
            events.send(EventDataBase.SetJsonNameList(jsonNameList = jsonNameList))
        }
    }

    fun setScreen(menu: String = "", platform: String = "", detail: String = "", type: String = "" ){
        viewModelScope.launch {
            events.send(EventDataBase.SetScreen(menu = menu, platform = platform, detail = detail, type = type))
        }
    }

    fun setGenreWeekList(genreWeekList: ArrayList<ArrayList<ItemKeyword>>, genreList: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDataBase.SetGenreWeekList(genreWeekList = genreWeekList, genreList = genreList))
        }
    }

    fun setGenreList(genreList: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDataBase.SetGenreWeekList(genreList = genreList))
        }
    }

    fun setWeekTrophyList(weekTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventDataBase.SetWeekTrophyList(weekTrophyList = weekTrophyList))
        }
    }

    fun setFilteredListTrophy(){

        val filteredList: ArrayList<ItemBookInfo> = ArrayList()

        if(state.value.weekTrophyList.isNotEmpty() && state.value.itemBookInfoMap.isNotEmpty()){
            for (trophyItem in state.value.weekTrophyList) {
                val bookCode = trophyItem.bookCode
                val bookInfo = state.value.itemBookInfoMap[bookCode]

                if (bookInfo != null) {
                    filteredList.add(bookInfo)
                }
            }
        }


        viewModelScope.launch {
            events.send(EventDataBase.SetFilteredList(filteredList = filteredList))
        }
    }

    fun setFilteredList(filteredList: ArrayList<ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventDataBase.SetFilteredList(filteredList = filteredList))
        }
    }

    fun setDate(week: String = "", month: String = ""){
        viewModelScope.launch {
            events.send(EventDataBase.SetDate(week = week, month = month))
        }
    }

    fun setKeywordDay(keywordDay: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDataBase.SetKeywordDay(keywordDay = keywordDay))
        }
    }

    fun setKeywordWeek(keywordDay: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDataBase.SetKeywordWeek(keywordDay = keywordDay))
        }
    }
}