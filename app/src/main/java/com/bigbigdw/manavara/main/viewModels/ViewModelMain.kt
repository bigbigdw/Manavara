package com.bigbigdw.manavara.main.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.main.event.EventMain
import com.bigbigdw.manavara.main.event.StateMain
import com.bigbigdw.manavara.main.models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

class ViewModelMain @Inject constructor() : ViewModel() {

    private val events = Channel<EventMain>()

    val state: StateFlow<StateMain> = events.receiveAsFlow()
        .runningFold(StateMain(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateMain())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateMain, event: EventMain): StateMain {
        return when(event){
            EventMain.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventMain.SetUserInfo -> {
                current.copy(userInfo = event.userInfo)
            }

            is EventMain.SetPlatformRange -> {
                current.copy(platformRange = event.platformRange)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setPlatformRange(){

        val currentUser :  FirebaseUser?
        val auth: FirebaseAuth = Firebase.auth
        currentUser = auth.currentUser

        val mRootRef = FirebaseDatabase.getInstance().reference

        mRootRef.child("USER").child(currentUser?.uid ?: "").child("PLATFORM").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val platformArray = ArrayList<String>()

                if(dataSnapshot.exists()){

                    for(item in dataSnapshot.children){
                        val platform: String? = item.getValue(String::class.java)
                        if (platform != null) {
                            platformArray.add(platform)
                        }
                    }

                    viewModelScope.launch {
                        events.send(
                            EventMain.SetPlatformRange(platformRange = platformArray)
                        )
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setUserInfo(){

        val currentUser :  FirebaseUser?
        val auth: FirebaseAuth = Firebase.auth
        currentUser = auth.currentUser

        val mRootRef = FirebaseDatabase.getInstance().reference

        mRootRef.child("USER").child(currentUser?.uid ?: "").child("USERINFO").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val userInfoResult: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                    if (userInfoResult != null) {
                        viewModelScope.launch {
                            events.send(
                                EventMain.SetUserInfo(userInfo = userInfoResult)
                            )
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
}