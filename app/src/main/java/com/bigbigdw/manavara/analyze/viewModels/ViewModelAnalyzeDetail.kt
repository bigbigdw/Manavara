package com.bigbigdw.manavara.analyze.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.analyze.event.EventAnalyzeDetail
import com.bigbigdw.manavara.analyze.event.StateAnalyzeDetail
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemGenre
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelAnalyzeDetail @Inject constructor() : ViewModel() {

    private val events = Channel<EventAnalyzeDetail>()

    val state: StateFlow<StateAnalyzeDetail> = events.receiveAsFlow()
        .runningFold(StateAnalyzeDetail(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateAnalyzeDetail())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateAnalyzeDetail, event: EventAnalyzeDetail): StateAnalyzeDetail {
        return when (event) {
            EventAnalyzeDetail.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventAnalyzeDetail.SetScreen -> {
                current.copy(
                    menu = event.menu,
                    key = event.key,
                )
            }

            is EventAnalyzeDetail.SetInit -> {
                current.copy(
                    platform = event.platform,
                    type = event.type,
                    title = event.title,
                    json = event.json,
                    mode = event.mode
                )
            }

            is EventAnalyzeDetail.SetItemBookInfo -> {
                current.copy(
                    itemBookInfo = event.itemBookInfo,
                )
            }

            is EventAnalyzeDetail.SetItemBestInfoTrophyList -> {
                current.copy(
                    itemBookInfo = event.itemBookInfo,
                    itemBestInfoTrophyList = event.itemBestInfoTrophyList,
                )
            }

            is EventAnalyzeDetail.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventAnalyzeDetail.SetGenreList -> {
                current.copy(genreList = event.genreList)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setItemBookInfoMap(itemBookInfoMap: MutableMap<String, ItemBookInfo>) {
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetItemBookInfoMap(itemBookInfoMap = itemBookInfoMap))
        }
    }

    fun setGenreList(genreList: ArrayList<ItemGenre>){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetGenreList(genreList = genreList))
        }
    }

    fun setScreen(menu: String = "", key: String = ""){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetScreen(menu = menu,key = key))
        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo, itemBestInfoTrophyList: ArrayList<ItemBestInfo> = ArrayList()){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setItemBookInfo(itemBookInfo: ItemBookInfo){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetItemBookInfo(itemBookInfo = itemBookInfo))
        }
    }

    fun setInit(title: String = "", type: String = "", json: String = "" , platform: String = "", mode: String = ""){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetInit(title = title, type = type, json = json, platform = platform, mode = mode))
        }
    }
}