package com.bigbigdw.manavara.main.event

import com.bigbigdw.manavara.main.models.ItemBookInfo
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventBest{
    object Loaded: EventBest

    class SetItemBestInfoList(
        var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
    ) : EventBest

    class SetItemBookInfoList(
        var itemBestInfoList: MutableMap<String?, Any> = mutableMapOf()
    ) : EventBest
}

data class StateBest(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
    var itemBestInfoList: MutableMap<String?, Any> = mutableMapOf()
)