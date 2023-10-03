package com.bigbigdw.manavara.main.viewModels

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.ActivityMain
import com.bigbigdw.manavara.main.ActivityRegister
import com.bigbigdw.manavara.main.events.EventLogin
import com.bigbigdw.manavara.main.events.StateLogin
import com.bigbigdw.manavara.main.models.UserInfo
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelLogin @Inject constructor() : ViewModel() {

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

    fun loginResult(activity: ComponentActivity, oneTapClient: SignInClient, data : Intent?){
        val auth: FirebaseAuth = Firebase.auth
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken

        when {
            idToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            checkUserExist(user = user, activity = activity)

                        } else {
                            viewModelScope.launch {
                                _sideEffects.send("signInWithCredential:failure == ${task.exception}")
                            }
                        }
                    }
            }
            else -> {
                viewModelScope.launch {
                    _sideEffects.send("No ID token!")
                }
            }
        }
    }

    private fun checkUserExist(user: FirebaseUser?, activity: ComponentActivity){
        val mRootRef = FirebaseDatabase.getInstance().reference.child("USER").child(user?.uid ?: "")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    viewModelScope.launch {
                        _sideEffects.send("이미 가입된 계정입니다")
                    }

                    val intent = Intent(activity, ActivityMain::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                } else {

                    viewModelScope.launch {
                        _sideEffects.send("회원가입을 진행합니다.")
                    }

                    if(state.value.isExpandedScreen){
                        viewModelScope.launch {
                            events.send(
                                EventLogin.SetUserInfo(state.value.userInfo.copy(userUID = user?.uid ?: "", userEmail = user?.email ?: ""))
                            )

                            events.send(
                                EventLogin.SetIsResgister(isResgister = true)
                            )
                        }
                    } else {
                        val intent = Intent(activity, ActivityRegister::class.java)
                        intent.putExtra("UID", user?.uid ?: "")
                        intent.putExtra("EMAIL", user?.email ?: "")
                        activity.startActivity(intent)
                        activity.finish()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun doRegister(getUserInfo: UserInfo, getRange: SnapshotStateList<String>) {

        viewModelScope.launch {
            events.send(
                EventLogin.SetUserInfo(state.value.userInfo.copy(userNickName = getUserInfo.userNickName, viewMode = getUserInfo.viewMode))
            )

            events.send(
                EventLogin.SetPlatformRange(getRange)
            )
        }

        if(state.value.userInfo.userNickName.isEmpty()){
            viewModelScope.launch {
                _sideEffects.send("닉네임을 입력해주세요")
            }
        } else if(state.value.userInfo.viewMode.isEmpty()){
            viewModelScope.launch {
                _sideEffects.send("웹툰 / 웹소설 모드를 선택해 주세요.")
            }
        } else if(state.value.platformRange.isEmpty()){
            viewModelScope.launch {
                _sideEffects.send("플랫폼을 선택해 주세요.")
            }
        } else {
            viewModelScope.launch {
                events.send(
                    EventLogin.SetIsRegisterConfirm(true)
                )
            }
        }
    }

    fun setIsRegisterConfirm(bool: Boolean){
        viewModelScope.launch {
            events.send(
                EventLogin.SetIsRegisterConfirm(bool)
            )
        }
    }

    fun isExpandedScreen(bool : Boolean){
        viewModelScope.launch {
            events.send(
                EventLogin.SetIsExpandedScreen(isExpandedScreen = bool)
            )
        }
    }

    fun finishRegister(activity: ComponentActivity){
        val mRootRef = FirebaseDatabase.getInstance().reference

        mRootRef.child("USER").child(state.value.userInfo.userUID).child("USERINFO").setValue(state.value.userInfo)
        mRootRef.child("USER").child(state.value.userInfo.userUID).child("PLATFORM").setValue(state.value.platformRange)

        val intent = Intent(activity, ActivityMain::class.java)
        activity.startActivity(intent)
        activity.finish()

        viewModelScope.launch {
            _sideEffects.send("가입이 완료되었습니다.")
        }
    }
}