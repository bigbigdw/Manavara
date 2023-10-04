package com.bigbigdw.manavara.main.event

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.bigbigdw.manavara.login.events.EventLogin
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventBest{
    object Loaded: EventBest

}

data class StateBest(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var platformRange: ArrayList<String> = ArrayList()
)