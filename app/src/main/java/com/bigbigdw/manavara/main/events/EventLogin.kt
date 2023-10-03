package com.bigbigdw.manavara.main.events

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventLogin{
    object Loaded: EventLogin

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventLogin

    class SetIsResgister(
        val isResgister: Boolean = false,
    ) : EventLogin

    class SetIsExpandedScreen(
        val isExpandedScreen: Boolean = false,
    ) : EventLogin

    class SetPlatformRange(
        var platformRange: SnapshotStateList<String> = SnapshotStateList(),
    ) : EventLogin

    class SetIsRegisterConfirm(
        val isRegisterConfirm: Boolean = false,
    ) : EventLogin
}

data class StateLogin(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val isResgister: Boolean = false,
    val isExpandedScreen: Boolean = false,
    var platformRange: SnapshotStateList<String> = SnapshotStateList(),
    val isRegisterConfirm: Boolean = false,
)