package com.bigbigdw.manavara.login.events

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventLogin{
    object Loaded: EventLogin

    object Loading: EventLogin

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventLogin


    class SetIsResgister(
        val isResgister: Boolean = false,
    ) : EventLogin

    class SetIsExpandedScreen(
        val isExpandedScreen: Boolean = false,
    ) : EventLogin

    class SetPlatformRangeNovel(
        var platformRangeNovel: ArrayList<String> = ArrayList(),
    ) : EventLogin

    class SetPlatformRangeComic(
        var platformRangeComic: ArrayList<String> = ArrayList(),
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
    val isRegisterConfirm: Boolean = false,
)