package com.bigbigdw.manavara.dataBase.event

import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
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
        val menuDesc : String = "",
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

    class SetGenreKeywordList(
        var genreKeywordList : ArrayList<ItemKeyword> = ArrayList(),
    ) : EventDataBase

    class SetGenreKeywordWeekList(
        var genreWeekList :  ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
        var genreKeywordList : ArrayList<ItemKeyword> = ArrayList(),
    ) : EventDataBase

    class SetSearchQuery(
        val searchQuery: String = "",
    ) : EventDataBase

    class SetSearch(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
        val filteredList: ArrayList<ItemBookInfo> = ArrayList(),
    ) : EventDataBase

    class SetItemBestDetailInfo(
        val itemBestDetailInfo : ItemBestDetailInfo = ItemBestDetailInfo()
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
    val menuDesc : String = "마나바라에 기록된 베스트 웹소설 리스트",
    val weekList : ArrayList<ArrayList<ItemBookInfo>> = ArrayList(),
    var weekTrophyList: ArrayList<ItemBestInfo> = ArrayList(),
    val filteredList: ArrayList<ItemBookInfo> = ArrayList(),
    val week: String = "",
    val month: String = "",
    var genreKeywordList : ArrayList<ItemKeyword> = ArrayList(),
    var genreKeywordWeekList :  ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
    val searchQuery: String = "",
    val itemBestDetailInfo : ItemBestDetailInfo = ItemBestDetailInfo()
)