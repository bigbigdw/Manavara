package com.bigbigdw.manavara.main.event

import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventMain{
    object Loaded: EventMain

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventMain

    class SetPlatformRangeNovel(
        var platformRangeNovel: ArrayList<String> = ArrayList()
    ) : EventMain

    class SetPlatformRangeComic(
        var platformRangeComic: ArrayList<String> = ArrayList()
    ) : EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var platformRangeNovel: ArrayList<String> = ArrayList(),
    var platformRangeComic: ArrayList<String> = ArrayList()
)