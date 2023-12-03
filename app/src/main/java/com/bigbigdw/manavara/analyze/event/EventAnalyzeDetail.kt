package com.bigbigdw.manavara.analyze.event

import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemGenre
import com.bigbigdw.manavara.best.models.ItemKeyword

sealed interface EventAnalyzeDetail{
    object Loaded: EventAnalyzeDetail

    class SetItemBookInfoMap(
        var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
    ) : EventAnalyzeDetail

    class SetScreen(
        val menu: String = "",
    ) : EventAnalyzeDetail

    class SetInit(
        val platform: String = "",
        val type: String = "",
        val title: String = "",
        val json: String = "",
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
    var genreList : ArrayList<ItemGenre> = ArrayList(),
    var itemBookInfoMap: MutableMap<String, ItemBookInfo> = mutableMapOf(),
)