package com.bigbigdw.manavara.main.viewModels

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.ActivityMain
import com.bigbigdw.manavara.main.ActivityRegister
import com.bigbigdw.manavara.main.events.EventLogin
import com.bigbigdw.manavara.main.events.StateLogin
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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