package com.bigbigdw.manavara.dataBase.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.dataBase.event.EventDataBaseDetail
import com.bigbigdw.manavara.dataBase.event.StateDataBaseDetail
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

class ViewModelDataBaseDetail @Inject constructor() : ViewModel() {

    private val events = Channel<EventDataBaseDetail>()

    val state: StateFlow<StateDataBaseDetail> = events.receiveAsFlow()
        .runningFold(StateDataBaseDetail(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateDataBaseDetail())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateDataBaseDetail, event: EventDataBaseDetail): StateDataBaseDetail {
        return when (event) {
            EventDataBaseDetail.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventDataBaseDetail.SetScreen -> {
                current.copy(
                    menu = event.menu,
                    key = event.key,
                )
            }

            is EventDataBaseDetail.SetInit -> {
                current.copy(
                    platform = event.platform,
                    type = event.type,
                    title = event.title,
                    json = event.json,
                    mode = event.mode
                )
            }

            is EventDataBaseDetail.SetItemBookInfo -> {
                current.copy(
                    itemBookInfo = event.itemBookInfo,
                )
            }

            is EventDataBaseDetail.SetItemBestInfoTrophyList -> {
                current.copy(
                    itemBookInfo = event.itemBookInfo,
                    itemBestInfoTrophyList = event.itemBestInfoTrophyList,
                )
            }

            is EventDataBaseDetail.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventDataBaseDetail.SetGenreList -> {
                current.copy(genreKeywordList = event.genreList, genreKeywordMonthList = event.genreMonthList)
            }

            is EventDataBaseDetail.SetGenreMap -> {
                current.copy(itemGenreKeywordMap = event.ItemKeywordMap)
            }

            is EventDataBaseDetail.SetJsonNameList -> {
                current.copy(jsonNameList = event.jsonNameList)
            }

            is EventDataBaseDetail.SetGenreBook -> {
                current.copy(
                    jsonNameList = event.jsonNameList,
                    genreKeywordList = event.genreKeywordList,
                    genreKeywordMonthList = event.genreKeywordMonthList,
                    itemBookInfoMap = event.itemBookInfoMap,
                    menu = event.menu,
                    key = event.key
                )
            }

            is EventDataBaseDetail.SetGenreStatus -> {
                current.copy(
                    jsonNameList = event.jsonNameList,
                    genreKeywordList = event.genreKeywordList,
                    genreKeywordMonthList = event.genreKeywordMonthList,
                    itemGenreKeywordMap = event.itemGenreKeywordMap,
                    menu = event.menu,
                    key = event.key
                )
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setGenreBook(
        jsonNameList: List<String>,
        genreKeywordList: ArrayList<ItemKeyword>,
        genreKeywordMonthList: ArrayList<ArrayList<ItemKeyword>>,
        itemBookInfoMap:  MutableMap<String, ItemBookInfo>,
        menu: String,
        key: String
    ) {
        viewModelScope.launch {
            events.send(
                EventDataBaseDetail.SetGenreBook(
                    jsonNameList = jsonNameList,
                    genreKeywordList = genreKeywordList,
                    genreKeywordMonthList = genreKeywordMonthList,
                    itemBookInfoMap = itemBookInfoMap,
                    menu = menu,
                    key = key
                )
            )
        }
    }

    fun setGenreStatus(
        jsonNameList: List<String>,
        genreKeywordList: ArrayList<ItemKeyword>,
        genreKeywordMonthList: ArrayList<ArrayList<ItemKeyword>>,
        itemGenreKeywordMap: MutableMap<String, ArrayList<ItemKeyword>>,
        menu: String,
        key: String
    ) {
        viewModelScope.launch {
            events.send(
                EventDataBaseDetail.SetGenreStatus(
                    jsonNameList = jsonNameList,
                    genreKeywordList = genreKeywordList,
                    genreKeywordMonthList = genreKeywordMonthList,
                    itemGenreKeywordMap = itemGenreKeywordMap,
                    menu = menu,
                    key = key
                )
            )
        }
    }

    fun setItemBookInfoMap(itemBookInfoMap: MutableMap<String, ItemBookInfo>) {
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetItemBookInfoMap(itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setGenreMap(ItemKeywordMap: MutableMap<String, ArrayList<ItemKeyword>>){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetGenreMap(ItemKeywordMap = ItemKeywordMap))
        }
    }

    fun setJsonNameList( jsonNameList: List<String>){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetJsonNameList(jsonNameList = jsonNameList))
        }
    }

    fun setGenreList(genreList: ArrayList<ItemKeyword>, genreMonthList: ArrayList<ArrayList<ItemKeyword>>){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetGenreList(genreList = genreList, genreMonthList = genreMonthList))
        }
    }

    fun setScreen(menu: String = "", key: String = ""){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetScreen(menu = menu,key = key))
        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo, itemBestInfoTrophyList: ArrayList<ItemBestInfo> = ArrayList()){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setItemBookInfo(itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetItemBookInfo(itemBookInfo = itemBookInfo))
        }
    }

    fun setInit(title: String = "", type: String = "", json: String = "" , platform: String = "", mode: String = ""){
        viewModelScope.launch {
            events.send(EventDataBaseDetail.SetInit(title = title, type = type, json = json, platform = platform, mode = mode))
        }
    }


}