package com.bigbigdw.manavara.best.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemBestDetailInfo(
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var type: String = "",
    var intro: String = "",
    var cntPageRead: String = "",
    var cntFavorite: String = "",
    var cntRecom: String = "",
    var cntTotalComment: String = "",
    var cntChapter: String = "",
    var genre: String = "",
    var tabInfo: ArrayList<String> = arrayListOf(),
    var keyword: ArrayList<String> = arrayListOf(),
)
@Serializable
data class ItemBookInfo(
    @SerialName("writer")
    var writer: String = "",
    @SerialName("title")
    var title: String = "",
    @SerialName("bookImg")
    var bookImg: String = "",
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("type")
    var type: String = "",
    @SerialName("intro")
    var intro: String = "",
    @SerialName("cntPageRead")
    var cntPageRead: String = "",
    @SerialName("cntFavorite")
    var cntFavorite: String = "",
    @SerialName("cntRecom")
    var cntRecom: String = "",
    @SerialName("cntTotalComment")
    var cntTotalComment: String = "",
    @SerialName("cntChapter")
    var cntChapter: String = "",
    @SerialName("total")
    var total:  Int = 0,
    @SerialName("totalCount")
    var totalCount:  Int = 0,
    @SerialName("totalWeek")
    var totalWeek:  Int = 0,
    @SerialName("totalWeekCount")
    var totalWeekCount:  Int = 0,
    @SerialName("totalMonth")
    var totalMonth:  Int = 0,
    @SerialName("totalMonthCount")
    var totalMonthCount:  Int = 0,
    @SerialName("currentDiff")
    var currentDiff:  Int = 0,
    @SerialName("number")
    var number: Int = 0,
    @SerialName("point")
    var point: Int = 0,
    @SerialName("genre")
    var genre: String = "",
)
@Serializable
data class ItemBestInfo (
    @SerialName("number")
    var number: Int = -1,
    @SerialName("point")
    var point: Int = -1,
    @SerialName("cntPageRead")
    var cntPageRead: String = "",
    @SerialName("cntFavorite")
    var cntFavorite: String = "",
    @SerialName("cntRecom")
    var cntRecom: String = "",
    @SerialName("cntTotalComment")
    var cntTotalComment: String = "",
    @SerialName("total")
    var total:  Int = 0,
    @SerialName("totalCount")
    var totalCount:  Int = 0,
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("currentDiff")
    var currentDiff:  Int = 0,
)

@Serializable
class ItemKeyword(
    @SerialName("title")
    var title: String = "",
    @SerialName("value")
    var value: String = ""
)