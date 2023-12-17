package com.bigbigdw.manavara.best.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventBest{
    object Loaded: EventBest

    class SetItemBestInfoList(
        var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventBest

    class SetItemBookInfoList(
        var itemBestInfoList: MutableMap<String, ItemBookInfo> = mutableMapOf()
    ) : EventBest

    class SetWeekTrophyList(
        var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventBest

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventBest

    class SetWeekMonthList(
        val weekMonthList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
        var weekMonthTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventBest

    class SetGenreDay(
        val genreDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventBest

    class SetGenreWeek(
        val genreDay : ArrayList<ItemKeyword> = ArrayList(),
        val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventBest

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventBest

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventBest

    class SetScreen(
        val menu: String = "",
        val platform: String = "",
        val detail: String = "",
        val type: String = "",
    ) : EventBest
}

data class StateBest(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
    var itemBestInfoList: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    var weekMonthTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    val weekMonthList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    val genreDay : ArrayList<ItemKeyword> = ArrayList(),
    val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),

    val detail : String = "",
    val menu: String = "",
    val platform: String = "",
    val type: String = "",
)