package com.bigbigdw.manavara.main.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.login.events.EventLogin
import com.bigbigdw.manavara.login.events.StateLogin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ViewModelMain @Inject constructor() : ViewModel() {

    private val events = Channel<EventLogin>()

    val state: StateFlow<StateLogin> = events.receiveAsFlow()
        .runningFold(StateLogin(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateLogin())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateLogin, event: EventLogin): StateLogin {
        return when(event){
            EventLogin.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventLogin.SetUserInfo -> {
                current.copy(userInfo = event.userInfo)
            }

            is EventLogin.SetIsResgister -> {
                current.copy(isResgister = event.isResgister)
            }

            is EventLogin.SetIsExpandedScreen -> {
                current.copy(isExpandedScreen = event.isExpandedScreen)
            }

            is EventLogin.SetPlatformRange -> {
                current.copy(platformRange = event.platformRange)
            }

            is EventLogin.SetIsRegisterConfirm -> {
                current.copy(isRegisterConfirm = event.isRegisterConfirm)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }
}