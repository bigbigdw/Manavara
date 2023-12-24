package com.bigbigdw.manavara.main.event

import com.bigbigdw.manavara.best.event.EventBest
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.manavara.event.EventManavara

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

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventMain

    class SetScreen(
        val menu: String = "",
        val platform: String = "",
        val detail: String = "",
        val type: String = "",
    ) : EventMain

    class SetItemPickInfo(
        val menu: String = "",
        val platform: String = "",
        val detail: String = "",
        val type: String = "",
        val itemPickInfo : MutableMap<String, ItemBookInfo> = mutableMapOf()
    ) : EventMain
}

data class StateMain(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val json : ArrayList<ItemBookInfo> = ArrayList(),
    val storage : ArrayList<ItemBookInfo> = ArrayList(),
    val isPicked: Boolean = false,
    val menu: String = "",
    val platform: String = "",
    val detail: String = "",
    val type: String = "",
    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    val itemPickInfo : MutableMap<String, ItemBookInfo> = mutableMapOf()
)

