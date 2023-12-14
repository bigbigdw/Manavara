package com.bigbigdw.manavara.manavara.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.dataBase.event.EventDataBase
import com.bigbigdw.manavara.manavara.event.EventManavara
import com.bigbigdw.manavara.manavara.event.StateManavara
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelManavara @Inject constructor() : ViewModel() {

    private val events = Channel<EventManavara>()

    val state: StateFlow<StateManavara> = events.receiveAsFlow()
        .runningFold(StateManavara(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateManavara())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateManavara, event: EventManavara): StateManavara {
        return when(event){
            EventManavara.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventManavara.SetScreen -> {
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type)
            }

            is EventManavara.SetPickList -> {
                current.copy(pickCategory = event.pickCategory, pickItemList = event.pickItemList, platform = event.platform)
            }

            is EventManavara.SetItemBestInfoTrophyList -> {
                current.copy(itemBookInfo = event.itemBookInfo, itemBestInfoTrophyList = event.itemBestInfoTrophyList)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo,  itemBestInfoTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventManavara.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setScreen(menu: String = "", platform: String = "", detail: String = "", type: String = ""){
        viewModelScope.launch {
            events.send(EventManavara.SetScreen(menu = menu, platform = platform, detail = detail, type = type))
        }
    }

    fun setPickList(pickCategory: ArrayList<String>, pickItemList: ArrayList<ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventManavara.SetPickList(pickCategory = pickCategory, pickItemList = pickItemList, platform = "전체"))
        }
    }

}