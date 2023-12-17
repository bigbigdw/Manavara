package com.bigbigdw.manavara.dataBase.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
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
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type, menuDesc = event.menuDesc)
            }

            is EventDataBase.SetGenreKeywordList -> {
                current.copy(genreKeywordList = event.genreKeywordList)
            }

            is EventDataBase.SetGenreKeywordWeekList -> {
                current.copy(genreKeywordWeekList = event.genreWeekList, genreKeywordList = event.genreKeywordList)
            }

            is EventDataBase.SetWeekTrophyList -> {
                current.copy(weekTrophyList = event.weekTrophyList)
            }

            is EventDataBase.SetFilteredList -> {
                current.copy(filteredList = event.filteredList)
            }

            is EventDataBase.SetDate -> {
                current.copy(date = event.date, jsonNameList = event.jsonNameList, dateType = event.dateType)
            }

            is EventDataBase.SetSearchQuery -> {
                current.copy(searchQuery = event.searchQuery)
            }

            is EventDataBase.SetSearch -> {
                current.copy(filteredList = event.filteredList, itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventDataBase.SetItemBestDetailInfo -> {
                current.copy(itemBestDetailInfo = event.itemBestDetailInfo)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setGenreKeywordList(genreKeywordList: ArrayList<ItemKeyword>) {
        viewModelScope.launch {
            events.send(
                EventDataBase.SetGenreKeywordList(genreKeywordList = genreKeywordList)
            )
        }
    }

    fun setBestDetailInfo(itemBestDetailInfo: ItemBestDetailInfo) {
        viewModelScope.launch {
            events.send(
                EventDataBase.SetItemBestDetailInfo(itemBestDetailInfo = itemBestDetailInfo)
            )
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

    fun setScreen(
        menu: String = state.value.menu,
        platform: String = state.value.platform,
        detail: String = state.value.detail,
        type: String = state.value.type,
        menuDesc: String = state.value.menuDesc,
    ) {
        viewModelScope.launch {
            events.send(
                EventDataBase.SetScreen(
                    menu = menu,
                    platform = platform,
                    detail = detail,
                    type = type,
                    menuDesc = menuDesc,
                )
            )
        }
    }

    fun setGenreWeekList(genreWeekList: ArrayList<ArrayList<ItemKeyword>>, genreList: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDataBase.SetGenreKeywordWeekList(genreWeekList = genreWeekList, genreKeywordList = genreList))
        }
    }

    fun setGenreKeywordWeekList(genreList: ArrayList<ItemKeyword>){
        viewModelScope.launch {
            events.send(EventDataBase.SetGenreKeywordWeekList(genreKeywordList = genreList))
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

    fun setDate(
        date: String = "",
        jsonNameList: List<String> = state.value.jsonNameList,
        dateType : String = state.value.dateType
    ) {
        viewModelScope.launch {
            events.send(EventDataBase.SetDate(date = date, jsonNameList = jsonNameList, dateType = dateType))
        }
    }
}