package com.bigbigdw.manavara.dataBase.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.dataBase.event.EventDB
import com.bigbigdw.manavara.dataBase.event.StateDB
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

class ViewModelDatabase @Inject constructor() : ViewModel() {

    private val events = Channel<EventDB>()

    val state: StateFlow<StateDB> = events.receiveAsFlow()
        .runningFold(StateDB(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateDB())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateDB, event: EventDB): StateDB {
        return when (event) {
            EventDB.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventDB.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventDB.SetItemBookInfo -> {
                current.copy(itemBookInfo = event.itemBookInfo)
            }

            is EventDB.SetItemBestInfoTrophyList -> {
                current.copy(itemBookInfo = event.itemBookInfo, itemBestInfoTrophyList = event.itemBestInfoTrophyList)
            }

            is EventDB.SetJsonNameList -> {
                current.copy(jsonNameList = event.jsonNameList)
            }

            is EventDB.SetScreen -> {
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type)
            }

            is EventDB.SetGenreList -> {
                current.copy(genreList = event.genreList)
            }

            is EventDB.SetGenreWeekList -> {
                current.copy(genreWeekList = event.genreWeekList, genreList = event.genreList)
            }

            is EventDB.SetWeekTrophyList -> {
                current.copy(weekTrophyList = event.weekTrophyList)
            }

            is EventDB.SetFilteredList -> {
                current.copy(filteredList = event.filteredList)
            }

            is EventDB.SetDate -> {
                current.copy(week = event.week, month = event.month)
            }

            is EventDB.SetKeywordDay -> {
                current.copy(keywordDay = event.keywordDay)
            }

            is EventDB.SetKeywordWeek -> {
                current.copy(keywordDay = event.keywordDay, keywordDayList = event.keywordDayList)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setItemBookInfoMap(itemBookInfoMap: MutableMap<String, ItemBookInfo>) {
        viewModelScope.launch {
            events.send(EventDB.SetItemBookInfoMap(itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setItemBookInfo(itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventDB.SetItemBookInfo(itemBookInfo = itemBookInfo))
        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo,  itemBestInfoTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventDB.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setJsonNameList( jsonNameList: List<String>){
        viewModelScope.launch {
            events.send(EventDB.SetJsonNameList(jsonNameList = jsonNameList))
        }
    }

    fun setScreen(menu: String = "", platform: String = "", detail: String = "", type: String = "" ){
        viewModelScope.launch {
            events.send(EventDB.SetScreen(menu = menu, platform = platform, detail = detail, type = type))
        }
    }

    fun setGenreWeekList(genreWeekList: ArrayList<ArrayList<ItemGenre>>, genreList: ArrayList<ItemGenre>){
        viewModelScope.launch {
            events.send(EventDB.SetGenreWeekList(genreWeekList = genreWeekList, genreList = genreList))
        }
    }

    fun setGenreList(genreList: ArrayList<ItemGenre>){
        viewModelScope.launch {
            events.send(EventDB.SetGenreWeekList(genreList = genreList))
        }
    }

    fun setWeekTrophyList(weekTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventDB.SetWeekTrophyList(weekTrophyList = weekTrophyList))
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
            events.send(EventDB.SetFilteredList(filteredList = filteredList))
        }
    }

    fun setDate(week: String = "", month: String = ""){
        viewModelScope.launch {
            events.send(EventDB.SetDate(week = week, month = month))
        }
    }

    fun setKeywordDay(keywordDay: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDB.SetKeywordDay(keywordDay = keywordDay))
        }
    }

    fun setKeywordWeek(keywordDay: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDB.SetKeywordWeek(keywordDay = keywordDay))
        }
    }
}