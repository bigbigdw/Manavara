package com.bigbigdw.manavara.manavara.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.dataBase.event.EventDataBase
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.novelListEng

sealed interface EventManavara{
    object Loaded: EventManavara

    class SetScreen(
        val menu: String = "",
        val platform: String = "",
        val detail: String = "",
        val type: String = "",
    ) : EventManavara

    class SetPickList(
        val pickCategory : ArrayList<String> = ArrayList(),
        val pickItemList : ArrayList<ItemBookInfo> = ArrayList(),
    ) : EventManavara

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventManavara
}

data class StateManavara(
    val Loaded: Boolean = false,
    val menu: String = "",
    val platform: String = "",
    val detail: String = "",
    val type: String = "",

    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),

    val pickCategory : ArrayList<String> = ArrayList(),
    val pickItemList : ArrayList<ItemBookInfo> = ArrayList(),
)