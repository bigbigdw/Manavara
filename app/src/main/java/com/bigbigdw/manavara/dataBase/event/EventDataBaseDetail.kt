package com.bigbigdw.manavara.dataBase.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword

sealed interface EventDataBaseDetail{
    object Loaded: EventDataBaseDetail

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventDataBaseDetail

    class SetScreen(
        val menu: String = "",
        val key: String = "",
    ) : EventDataBaseDetail

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    ) : EventDataBaseDetail

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventDataBaseDetail

    class SetInit(
        val platform: String = "",
        val type: String = "",
        val title: String = "",
        val json: String = "",
        val mode: String = "",
    ) : EventDataBaseDetail

    class SetJsonNameList(
        val jsonNameList : List<String> = arrayListOf(),
    ) : EventDataBaseDetail

    class SetGenreList(
        var genreList : ArrayList<ItemKeyword> = ArrayList(),
        val genreMonthList: ArrayList<ArrayList<ItemKeyword>> = ArrayList()
    ) : EventDataBaseDetail

    class SetGenreMap(
        var ItemKeywordMap: MutableMap<String, ArrayList<ItemKeyword>> = mutableMapOf(),
    ) : EventDataBaseDetail

    class SetGenreBook(
        val jsonNameList : List<String> = arrayListOf(),
        var genreKeywordList : ArrayList<ItemKeyword> = ArrayList(),
        val genreKeywordMonthList: ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
        val menu: String = "",
        val key: String = "",
    ) : EventDataBaseDetail
}

data class StateDataBaseDetail(
    val Loaded: Boolean = false,
    val menu: String = "",
    val platform: String = "",
    val type: String = "",
    val title: String = "",
    val json: String = "",
    val key: String = "",
    val mode: String = "",
    var genreKeywordList : ArrayList<ItemKeyword> = ArrayList(),
    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
    val genreKeywordMonthList: ArrayList<ArrayList<ItemKeyword>> = ArrayList(),
    var itemGenreKeywordMap: MutableMap<String, ArrayList<ItemKeyword>> = mutableMapOf(),
    val jsonNameList : List<String> = arrayListOf(),
)