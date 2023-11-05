package com.bigbigdw.manavara.login.viewModels

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.ActivityMain
import com.bigbigdw.manavara.login.ActivityRegister
import com.bigbigdw.manavara.login.events.EventLogin
import com.bigbigdw.manavara.login.events.StateLogin
import com.bigbigdw.manavara.main.ActivitySplash
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.changePlatformNameKor
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

            EventLogin.Loading -> {
                current.copy(Loaded = false)
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

                    val userInfo = dataSnapshot.child("USERINFO").getValue(UserInfo::class.java)

                    if(userInfo?.userStatus == "ALLOW"){
                        viewModelScope.launch {
                            _sideEffects.send("이미 가입된 계정입니다")
                        }

                        val intent = Intent(activity, ActivityMain::class.java)
                        activity.startActivity(intent)
                        activity.finish()
                    } else {
                        viewModelScope.launch {
                            _sideEffects.send("가입 승인 대기중입니다.")
                        }
                    }

                } else {

                    Firebase.auth.signOut()

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

    fun doRegister(getUserInfo: UserInfo) {

        viewModelScope.launch {
            events.send(
                EventLogin.SetUserInfo(state.value.userInfo.copy(userNickName = getUserInfo.userNickName))
            )
        }

        getFCMToken()

        if(state.value.userInfo.userNickName.isEmpty()){
            viewModelScope.launch {
                _sideEffects.send("닉네임을 입력해주세요")
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

        val intent = Intent(activity, ActivitySplash::class.java)
        activity.startActivity(intent)
        activity.finish()

        viewModelScope.launch {
            _sideEffects.send("가입이 완료되었습니다. 가입 승인을 기다려주세요.")
        }
    }

    fun setUserInfo(uid : String, email : String){
        viewModelScope.launch {
            events.send(
                EventLogin.SetUserInfo(state.value.userInfo.copy(userUID = uid, userEmail = email))
            )
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("getFCMToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }


            val token = task.result

            viewModelScope.launch {
                events.send(
                    EventLogin.SetUserInfo(state.value.userInfo.copy(userFcmToken = token))
                )
            }
        })
    }
}