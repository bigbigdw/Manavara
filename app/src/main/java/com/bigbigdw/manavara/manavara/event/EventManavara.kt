package com.bigbigdw.manavara.manavara.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.manavara.models.ItemAlert

sealed interface EventManavara{
    object Loaded: EventManavara

    class SetScreen(
        val menu: String = "웹소설 PICK 작품들 보기",
        val platform: String = "전체",
        val detail: String = "",
        val type: String = "NOVEL",
    ) : EventManavara

    class SetPickItems(
        val pickCategory : ArrayList<String> = ArrayList(),
    ) : EventManavara

    class SetPickList(
        val pickCategory : ArrayList<String> = ArrayList(),
        val pickItemList : ArrayList<ItemBookInfo> = ArrayList(),
        val platform: String = "",
    ) : EventManavara

    class SetPickShareList(
        val pickCategory : ArrayList<String> = ArrayList(),
        val pickShareItemList : MutableMap<String, ArrayList<ItemBookInfo>> = mutableMapOf(),
        val platform: String = "",
    ) : EventManavara

    class SetIsMakePickList(
        val isMakePickList: Boolean = false,
    ) : EventManavara

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventManavara

    class SetFcmList(
        val fcmList : ArrayList<ItemAlert> = ArrayList(),
    ) : EventManavara

    class SetItemBookInfoList(
        val itemBookInfoList: List<ItemBookInfo>? = ArrayList(),
    ) : EventManavara

}

data class StateManavara(
    val Loaded: Boolean = false,
    val menu: String = "웹소설 PICK 작품들 보기",
    val platform: String = "",
    val detail: String = "",
    val type: String = "NOVEL",

    val itemBestInfoTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    val itemBookInfo: ItemBookInfo = ItemBookInfo(),

    val pickCategory: ArrayList<String> = ArrayList(),
    val pickItemList: ArrayList<ItemBookInfo> = ArrayList(),
    val pickShareItemList: MutableMap<String, ArrayList<ItemBookInfo>> = mutableMapOf(),
    val isMakePickList: Boolean = false,

    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    val fcmList: ArrayList<ItemAlert> = ArrayList(),

    val itemBookInfoList: List<ItemBookInfo>? = ArrayList()
)