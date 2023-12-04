package com.bigbigdw.manavara.analyze.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemGenre
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.manavara.event.EventManavara

sealed interface EventAnalyzeDetail{
    object Loaded: EventAnalyzeDetail

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventAnalyzeDetail

    class SetScreen(
        val menu: String = "",
        val key: String = "",
    ) : EventAnalyzeDetail

    class SetItemBookInfo(
        val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    ) : EventAnalyzeDetail

    class SetItemBestInfoTrophyList(
        val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
        val itemBookInfo : ItemBookInfo = ItemBookInfo()
    ) : EventAnalyzeDetail

    class SetInit(
        val platform: String = "",
        val type: String = "",
        val title: String = "",
        val json: String = "",
        val mode: String = "",
    ) : EventAnalyzeDetail

    class SetGenreList(
        var genreList : ArrayList<ItemGenre> = ArrayList()
    ) : EventAnalyzeDetail
}

data class StateAnalyzeDetail(
    val Loaded: Boolean = false,
    val menu: String = "",
    val platform: String = "",
    val type: String = "",
    val title: String = "",
    val json: String = "",
    val key: String = "",
    val mode: String = "",
    var genreList : ArrayList<ItemGenre> = ArrayList(),
    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    val itemBookInfo : ItemBookInfo = ItemBookInfo(),
    val itemBestInfoTrophyList : ArrayList<ItemBestInfo> = ArrayList(),
)