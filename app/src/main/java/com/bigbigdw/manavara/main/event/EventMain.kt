package com.bigbigdw.manavara.main.event

import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventMain{
    object Loaded: EventMain

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventMain

}

data class StateMain(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
)

data class MainSettingLine (
    var title: String = "",
    var value: String = "",
    var onClick : () -> Unit = {}
)