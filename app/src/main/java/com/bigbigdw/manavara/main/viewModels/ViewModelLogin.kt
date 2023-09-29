package com.bigbigdw.manavara.main.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.ActivityMain
import com.bigbigdw.manavara.main.ActivityRegister
import com.bigbigdw.manavara.main.events.EventLogin
import com.bigbigdw.manavara.main.events.StateLogin
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

    fun isExpandedScreen(bool : Boolean){
        viewModelScope.launch {
            events.send(
                EventLogin.SetIsExpandedScreen(isExpandedScreen = bool)
            )
        }
    }
}