package com.bigbigdw.manavara.analyze.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.analyze.event.EventAnalyze
import com.bigbigdw.manavara.analyze.event.EventAnalyzeDetail
import com.bigbigdw.manavara.analyze.event.StateAnalyzeDetail
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

                )
            }

            is EventAnalyzeDetail.SetInit -> {
                current.copy(
                    platform = event.platform,
                    type = event.type,
                    title = event.title,
                    json = event.json
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

    fun setScreen(menu: String = ""){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetScreen(menu = menu,))
        }
    }

    fun setInit(title: String = "", type: String = "", json: String = "" , platform: String = ""){
        viewModelScope.launch {
            events.send(EventAnalyzeDetail.SetInit(title = title, type = type, json = json, platform = platform))
        }
    }
}