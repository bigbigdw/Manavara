package com.bigbigdw.manavara.main.event

import com.bigbigdw.manavara.main.models.ItemBestInfo
import com.bigbigdw.manavara.main.models.ItemBookInfo
import com.bigbigdw.manavara.main.models.ItemKeyword
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventBest{
    object Loaded: EventBest

    class SetItemBestInfoList(
        var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
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

    class SetWeekList(
        val weekList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    ) : EventBest

    class SetMonthList(
        val monthList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    ) : EventBest

    class SetMonthTrophyList(
        var monthTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventBest

    class SetGenreDay(
        val genreDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventBest

    class SetGenreWeek(
        val genreDay : ArrayList<ItemKeyword> = ArrayList(),
        val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventBest
}

data class StateBest(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
    var itemBestInfoList: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    val weekList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    val monthList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    var monthTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    val genreDay : ArrayList<ItemKeyword> = ArrayList(),
    val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
)