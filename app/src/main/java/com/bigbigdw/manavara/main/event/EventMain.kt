package com.bigbigdw.manavara.main.event

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.bigbigdw.manavara.login.events.EventLogin
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventMain{
    object Loaded: EventMain

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventMain

    class SetPlatformRange(
        var platformRange: ArrayList<String> = ArrayList()
    ) : EventMain

}

data class StateMain(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var platformRange: ArrayList<String> = ArrayList()
)