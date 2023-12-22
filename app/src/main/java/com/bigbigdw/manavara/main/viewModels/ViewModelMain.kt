package com.bigbigdw.manavara.main.viewModels

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.main.event.EventMain
import com.bigbigdw.manavara.main.event.StateMain
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.manavara.event.EventManavara
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.DataStoreManager.Companion.UID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Collections
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

            is EventMain.SetScreen -> {
                current.copy(menu = event.menu, platform = event.platform, detail = event.detail, type = event.type)
            }

            is EventMain.SetItemBestInfoTrophyList -> {
                current.copy(itemBookInfo = event.itemBookInfo, itemBestInfoTrophyList = event.itemBestInfoTrophyList)
            }

        }
    }

    fun setItemBestInfoTrophyList(itemBookInfo: ItemBookInfo,  itemBestInfoTrophyList: ArrayList<ItemBestInfo>){
        viewModelScope.launch {
            events.send(EventMain.SetItemBestInfoTrophyList(itemBookInfo = itemBookInfo, itemBestInfoTrophyList = itemBestInfoTrophyList))
        }
    }

    fun setScreen(
        menu: String = state.value.menu,
        platform: String = state.value.platform,
        detail: String = state.value.detail,
        type: String = state.value.type
    ) {
        viewModelScope.launch {
            events.send(
                EventMain.SetScreen(
                    menu = menu,
                    platform = platform,
                    detail = detail,
                    type = type
                )
            )
        }
    }

    fun setIsPicked(context: Context, type: String, platform: String, bookCode: String, uid: String = "ecXPTeFiDnV732gOiaD8u525NnE3"){

        val dataStore = DataStoreManager(context)

        viewModelScope.launch {
            dataStore.getDataStoreString(UID).collect{
                val mRootRef = FirebaseDatabase.getInstance().reference
                    .child("USER")
                    .child(uid)
//                    .child(it ?: uid)
                    .child("PICK")
                    .child("MY")
                    .child(type)
                    .child(platform)

                mRootRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.exists()) {

                            for(item in dataSnapshot.children){
                                val itemBookInfo = item.getValue(ItemBookInfo::class.java)

                                if(itemBookInfo?.bookCode.equals(bookCode)){
                                    viewModelScope.launch {
                                        events.send(EventMain.SetIsPicked(true))
                                    }
                                    return
                                }
                            }
                            viewModelScope.launch {
                                events.send(EventMain.SetIsPicked(false))
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
        }
    }

    fun setPickBook(context : Context, platform: String, type : String, item: ItemBookInfo, uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3"){
        val dataStore = DataStoreManager(context)

        viewModelScope.launch {

            dataStore.getDataStoreString(UID).collect{
                val rootRef = FirebaseDatabase.getInstance().reference
                    .child("USER")
                    .child(uid)
//                    .child(it ?: uid)
                    .child("PICK")
                    .child("MY")
                    .child(type)
                    .child(platform)

                rootRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        item.belong = "PICK"

                        rootRef.child((dataSnapshot.childrenCount + 1).toString()).setValue(item)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                events.send(EventMain.SetIsPicked(true))
                _sideEffects.send("[${item.title}] 이 PICK 되었습니다.")
            }
        }
    }

    //TODO: UID 받아오게 작업하기
    fun setUnPickBook(context : Context, platform: String, type: String, item: ItemBookInfo, uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3"){
        val dataStore = DataStoreManager(context)

        viewModelScope.launch {

            dataStore.getDataStoreString(UID).collect{
                val rootRef = FirebaseDatabase.getInstance().reference
                    .child("USER")
                    .child(uid)
//                    .child(it ?: uid)
                    .child("PICK")
                    .child("MY")
                    .child(type)
                    .child(platform)


                rootRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for(pickedItem in dataSnapshot.children){
                            val pickedItemValue = pickedItem.getValue(ItemBookInfo::class.java)

                            if(pickedItemValue == item){
                                rootRef.child(pickedItem.key ?: "").removeValue()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                events.send(EventMain.SetIsPicked(true))
                _sideEffects.send("[${item.title}] 이 PICK 되었습니다.")
            }
        }

        viewModelScope.launch {
            events.send(EventMain.SetIsPicked(false))
            _sideEffects.send("[${item.title}] 이 PICK 해제 되었습니다.")
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