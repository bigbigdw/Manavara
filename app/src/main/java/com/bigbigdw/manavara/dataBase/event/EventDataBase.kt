package com.bigbigdw.manavara.dataBase.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword

sealed interface EventDataBase{
    object Loaded: EventDataBase

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventDataBase

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    ) : EventDataBase

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventDataBase

    class SetJsonNameList(
        val jsonNameList : List<String> = arrayListOf(),
    ) : EventDataBase

    class SetScreen(
        val menu: String = "",
        val platform: String = "",
        val detail: String = "",
        val type: String = "",
    ) : EventDataBase

    class SetKeywordDay(
        val keywordDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventDataBase

    class SetKeywordWeek(
        val keywordDay : ArrayList<ItemKeyword> = ArrayList(),
        val keywordDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventDataBase

    class SetWeekTrophyList(
        var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventDataBase

    class SetFilteredList(
        val filteredList: ArrayList<ItemBookInfo> = ArrayList()
    ) : EventDataBase

    class SetDate(
        val week: String = "",
        val month: String = "",
    ) : EventDataBase

    class SetGenreList(
        var genreList : ArrayList<ItemKeyword> = ArrayList()
    ) : EventDataBase

    class SetGenreWeekList(
        var genreWeekList :  ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
        var genreList : ArrayList<ItemKeyword> = ArrayList(),
    ) : EventDataBase
}

data class StateDataBase(
    val Loaded: Boolean = false,
    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    val jsonNameList : List<String> = arrayListOf(),
    val menu: String = "베스트 웹소설 DB",
    val platform: String = "",
    val detail: String = "",
    val type: String = "NOVEL",
    val weekList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    val filteredList: ArrayList<ItemBookInfo> = ArrayList(),
    val week: String = "",
    val month: String = "",
    var genreList : ArrayList<ItemKeyword> = ArrayList(),
    var genreWeekList :  ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
    val keywordDay : ArrayList<ItemKeyword> = ArrayList(),
    val keywordDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
)