package com.bigbigdw.manavara.manavara.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.manavara.event.EventManavara
import com.bigbigdw.manavara.manavara.event.StateManavara
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import convertItemBookJson
import convertItemKeyword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.File
import java.nio.charset.Charset
import java.util.Collections
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

            is EventManavara.SetItemBestInfoList -> {
                current.copy(itemBookInfoList = event.itemBookInfoList)
            }

            is EventManavara.SetItemBookInfoList -> {
                current.copy(itemBestInfoList = event.itemBestInfoList)
            }

            is EventManavara.SetWeekTrophyList -> {
                current.copy(weekTrophyList = event.weekTrophyList)
            }

            is EventManavara.SetItemBookInfoMap -> {
                current.copy(itemBookInfoMap = event.itemBookInfoMap)
            }

            is EventManavara.SetWeekList -> {
                current.copy(weekList = event.weekList)
            }

            is EventManavara.SetMonthList -> {
                current.copy(monthList = event.monthList)
            }

            is EventManavara.SetMonthTrophyList -> {
                current.copy(monthTrophyList = event.monthTrophyList)
            }

            is EventManavara.SetGenreDay -> {
                current.copy(
                    genreDay = event.genreDay
                )
            }

            is EventManavara.SetGenreWeek -> {
                current.copy(
                    genreDay = event.genreDay,
                    genreDayList = event.genreDayList
                )
            }

            is EventManavara.SetItemBookInfo -> {
                current.copy(
                    itemBookInfo = event.itemBookInfo
                )
            }

            is EventManavara.SetItemBestInfoTrophyList ->{
                current.copy(
                    itemBestInfoTrophyList = event.itemBestInfoTrophyList,
                    itemBookInfo = event.itemBookInfo
                )
            }

            is EventManavara.SetBest -> {
                current.copy(platform = event.platform, bestType = event.bestType, type = event.type, menu = event.menu)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

}