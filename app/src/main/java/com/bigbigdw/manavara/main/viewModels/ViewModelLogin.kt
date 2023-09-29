package com.bigbigdw.manavara.main.viewModels

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.ActivityMain
import com.bigbigdw.manavara.main.events.EventLogin
import com.bigbigdw.manavara.main.events.StateLogin
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
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
                            Log.d("ActivityLogin", "signInWithCredential:success")
                            val user = auth.currentUser

                            val intent = Intent(activity, ActivityMain::class.java)
                            activity.startActivity(intent)
                            activity.finish()
                        } else {
                            Log.w("ActivityLogin", "signInWithCredential:failure", task.exception)
                        }
                    }
            }
            else -> {
                Log.d("ActivityLogin", "No ID token!")
            }
        }
    }
}