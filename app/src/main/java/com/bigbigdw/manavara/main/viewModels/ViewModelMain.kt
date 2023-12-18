package com.bigbigdw.manavara.main.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.main.event.EventMain
import com.bigbigdw.manavara.main.event.StateMain
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.DataStoreManager.Companion.UID
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

            is EventMain.SetJson -> {
                current.copy(json = event.json)
            }

            is EventMain.SetStorage -> {
                current.copy(storage = event.storage)
            }

            is EventMain.SetIsPicked -> {
                current.copy(isPicked = event.isPicked)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    //TODO: UID 받아오게 작업하기
    fun setIsPicked(type: String, platform: String, bookCode: String, uid: String = "ecXPTeFiDnV732gOiaD8u525NnE3"){

        val mRootRef = FirebaseDatabase.getInstance().reference
            .child("USER")
            .child(uid)
            .child("PICK")
            .child("MY")
            .child(type)
            .child(platform)
            .child(bookCode)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    viewModelScope.launch {
                        events.send(EventMain.SetIsPicked(true))
                    }
                } else {
                    viewModelScope.launch {
                        events.send(EventMain.SetIsPicked(false))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    //TODO: UID 받아오게 작업하기

    fun setPickBook(context : Context, platform: String, type : String, item: ItemBookInfo, uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3"){
        val dataStore = DataStoreManager(context)

        viewModelScope.launch {

            dataStore.getDataStoreString(UID).collect{
                FirebaseDatabase.getInstance().reference
                    .child("USER")
//                    .child(it ?: uid)
                    .child(uid)
                    .child("PICK")
                    .child("MY")
                    .child(type)
                    .child(platform)
                    .child(item.bookCode).setValue(item)

                events.send(EventMain.SetIsPicked(true))
                _sideEffects.send("${item.title} 이 PICK 되었습니다. 마나바라 -> PICK에서 확인 할 수 있습니다.")
            }
        }
    }

    //TODO: UID 받아오게 작업하기
    fun setUnPickBook(platform: String, type: String, item: ItemBookInfo){

        FirebaseDatabase.getInstance().reference
            .child("USER")
            .child( "ecXPTeFiDnV732gOiaD8u525NnE3")
            .child("PICK")
            .child("MY")
            .child(type)
            .child(platform)
            .child(item.bookCode).removeValue()

        viewModelScope.launch {
            events.send(EventMain.SetIsPicked(false))
            _sideEffects.send("${item.title} 이 PICK 해제 되었습니다.")
        }
    }


    fun setJson(json: ArrayList<ItemBookInfo>){
        viewModelScope.launch {
            events.send(EventMain.SetJson(json = json))
        }
    }

    fun setUserInfo(uid: String){

        val mRootRef = FirebaseDatabase.getInstance().reference
            .child("USER")
            .child(uid)
            .child("USER_INFO")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    val userInfo = dataSnapshot.getValue(UserInfo::class.java)

                    if(userInfo != null){
                        viewModelScope.launch {
                            events.send(EventMain.SetUserInfo(userInfo = userInfo))
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }
}