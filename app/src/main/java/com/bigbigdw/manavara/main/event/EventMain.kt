package com.bigbigdw.manavara.main.event

import com.bigbigdw.manavara.best.event.EventBest
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventMain{
    object Loaded: EventMain

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventMain

    class SetJson(
        val json : ArrayList<ItemBookInfo> = ArrayList(),
    ) : EventMain

    class SetStorage(
        val storage : ArrayList<ItemBookInfo> = ArrayList()
    ) : EventMain

    class SetIsPicked(
        val isPicked: Boolean = false,
    ) : EventMain

}

data class StateMain(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val json : ArrayList<ItemBookInfo> = ArrayList(),
    val storage : ArrayList<ItemBookInfo> = ArrayList(),
    val isPicked: Boolean = false,
)

