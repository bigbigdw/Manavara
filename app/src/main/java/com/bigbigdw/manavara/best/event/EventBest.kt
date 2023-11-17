package com.bigbigdw.manavara.best.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.novelListEng

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

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventBest

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventBest

    class SetPlatform(
        val platform : String = novelListEng()[0],
    ) : EventBest

    class SetBestType(
        val bestType : String = "TODAY_BEST",
    ) : EventBest

    class SetType(
        val type : String = "NOVEL",
    ) : EventBest

    class SetMenu(
        val menu : String = "TODAY"
    ) : EventBest

    class SetBest(
        val platform : String = novelListEng()[0],
        val bestType : String = "TODAY_BEST",
        val type : String = "NOVEL",
        val menu : String = "TODAY"
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
    val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
    val platform : String = novelListEng()[0],
    val bestType : String = "TODAY_BEST",
    val type : String = "NOVEL",
    val menu : String = "TODAY"
)