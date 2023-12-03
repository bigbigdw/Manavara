package com.bigbigdw.manavara.analyze.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemGenre
import com.bigbigdw.manavara.best.models.ItemKeyword

sealed interface EventAnalyze{
    object Loaded: EventAnalyze

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventAnalyze

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    ) : EventAnalyze

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventAnalyze

    class SetJsonNameList(
        val jsonNameList : List<String> = arrayListOf(),
    ) : EventAnalyze

    class SetScreen(
        val menu: String = "",
        val platform: String = "",
        val detail: String = "",
        val type: String = "",
    ) : EventAnalyze

    class SetKeywordDay(
        val keywordDay : ArrayList<ItemKeyword> = ArrayList()
    ) : EventAnalyze

    class SetKeywordWeek(
        val keywordDay : ArrayList<ItemKeyword> = ArrayList(),
        val keywordDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventAnalyze

    class SetWeekTrophyList(
        var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    ) : EventAnalyze

    class SetFilteredList(
        val filteredList: ArrayList<ItemBookInfo> = ArrayList()
    ) : EventAnalyze

    class SetDate(
        val week: String = "",
        val month: String = "",
    ) : EventAnalyze

    class SetGenreList(
        var genreList : ArrayList<ItemGenre> = ArrayList()
    ) : EventAnalyze

    class SetGenreWeekList(
        var genreWeekList :  ArrayList<ArrayList<ItemGenre>> = ArrayList(),
        var genreList : ArrayList<ItemGenre> = ArrayList(),
    ) : EventAnalyze
}

data class StateAnalyze(
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
    var genreList : ArrayList<ItemGenre> = ArrayList(),
    var genreWeekList :  ArrayList<ArrayList<ItemGenre>> = ArrayList(),
    val keywordDay : ArrayList<ItemKeyword> = ArrayList(),
    val keywordDayList : ArrayList<ArrayList<ItemKeyword>> = ArrayList()
)