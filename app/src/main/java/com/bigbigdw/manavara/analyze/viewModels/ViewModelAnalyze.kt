package com.bigbigdw.manavara.analyze.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.analyze.event.EventAnalyze
import com.bigbigdw.manavara.analyze.event.StateAnalyze
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemGenre
import com.bigbigdw.manavara.best.models.ItemKeyword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelAnalyze @Inject constructor() : ViewModel() {

    private val events = Channel<EventAnalyze>()

    val state: StateFlow<StateAnalyze> = events.receiveAsFlow()
        .runningFold(StateAnalyze(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateAnalyze())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateAnalyze, event: EventAnalyze): StateAnalyze {
        return when (event) {
            EventAnalyze.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventAnalyze.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventAnalyze.SetItemBookInfo -> {
                current.copy(itemBookInfo = event.itemBookInfo)
            }

            is EventAnalyze.SetItemBestInfoTrophyList -> {
                current.copy(itemBookInfo = event.itemBookInfo, itemBestInfoTrophyList = event.itemBestInfoTrophyList)
            }

            is EventAnalyze.SetJsonNameList -> {
                current.copy(jsonNameList = event.jsonNameList)
            }

            is EventAnalyze.SetScreen -> {
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type)
            }

            is EventAnalyze.SetGenreList -> {
                current.copy(genreList = event.genreList)
            }

            is EventAnalyze.SetGenreWeekList -> {
                current.copy(genreWeekList = event.genreWeekList, genreList = event.genreList)
            }

            is EventAnalyze.SetWeekTrophyList -> {
                current.copy(weekTrophyList = event.weekTrophyList)
            }

            is EventAnalyze.SetFilteredList -> {
                current.copy(filteredList = event.filteredList)
            }

            is EventAnalyze.SetDate -> {
                current.copy(week = event.week, month = event.month)
            }

            is EventAnalyze.SetKeywordDay -> {
                current.copy(keywordDay = event.keywordDay)
            }

            is EventAnalyze.SetKeywordWeek -> {
                current.copy(keywordDay = event.keywordDay, keywordDayList = event.keywordDayList)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setItemBookInfoMap(itemBookInfoMap: MutableMap<String, ItemBookInfo>) {
        viewModelScope.launch {
            events.send(EventAnalyze.SetItemBookInfoMap(itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setItemBookInfo(itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventAnalyze.SetItemBookInfo(itemBookInfo = itemBookInfo))
        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo,  itemBestInfoTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setJsonNameList( jsonNameList: List<String>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetJsonNameList(jsonNameList = jsonNameList))
        }
    }

    fun setScreen(menu: String = "", platform: String = "", detail: String = "", type: String = "" ){
        viewModelScope.launch {
            events.send(EventAnalyze.SetScreen(menu = menu, platform = platform, detail = detail, type = type))
        }
    }

    fun setGenreWeekList(genreWeekList: ArrayList<ArrayList<ItemGenre>>, genreList: ArrayList<ItemGenre>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetGenreWeekList(genreWeekList = genreWeekList, genreList = genreList))
        }
    }

    fun setGenreList(genreList: ArrayList<ItemGenre>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetGenreWeekList(genreList = genreList))
        }
    }

    fun setWeekTrophyList(weekTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetWeekTrophyList(weekTrophyList = weekTrophyList))
        }
    }

    fun setFilteredList(){

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
            events.send(EventAnalyze.SetFilteredList(filteredList = filteredList))
        }
    }

    fun setDate(week: String = "", month: String = ""){
        viewModelScope.launch {
            events.send(EventAnalyze.SetDate(week = week, month = month))
        }
    }

    fun setKeywordDay(keywordDay: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetKeywordDay(keywordDay = keywordDay))
        }
    }

    fun setKeywordWeek(keywordDay: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventAnalyze.SetKeywordWeek(keywordDay = keywordDay))
        }
    }
}