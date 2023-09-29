package com.bigbigdw.manavara.main.events

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
}

data class StateLogin(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val isResgister: Boolean = false,
    val isExpandedScreen: Boolean = false,
)