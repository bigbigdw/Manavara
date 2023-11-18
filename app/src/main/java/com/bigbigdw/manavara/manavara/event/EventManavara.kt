package com.bigbigdw.manavara.manavara.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.util.novelListEng

sealed interface EventManavara{
    object Loaded: EventManavara

    class SetItemBestInfoList(
        var itemBookInfoList: ArrayList<ItemBookInfo> = ArrayList(),
    ) : EventManavara

    class SetItemBookInfoList(
        var itemBestInfoList: MutableMap<String, ItemBookInfo> = mutableMapOf()
    ) : EventManavara

    class SetWeekTrophyList(
        var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventManavara

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventManavara

    class SetWeekList(
        val weekList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    ) : EventManavara

    class SetMonthList(
        val monthList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    ) : EventManavara

    class SetMonthTrophyList(
        var monthTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventManavara

    class SetGenreDay(
        val genreDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventManavara

    class SetGenreWeek(
        val genreDay : ArrayList<ItemKeyword> = ArrayList(),
        val genreDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventManavara

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventManavara

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventManavara

    class SetBest(
        val platform : String = "",
        val bestType : String = "",
        val type : String = "",
        val menu : String = "",
    ) : EventManavara
}

data class StateManavara(
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