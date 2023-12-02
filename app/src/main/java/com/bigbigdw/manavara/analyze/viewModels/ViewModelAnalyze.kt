package com.bigbigdw.manavara.analyze.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.analyze.event.EventAnalyze
import com.bigbigdw.manavara.analyze.event.StateAnalyze
import com.bigbigdw.manavara.best.event.EventBest
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
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

}