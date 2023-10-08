package com.bigbigdw.manavara.main.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    var total:  Int = 1,
    @SerialName("totalCount")
    var totalCount:  Int = 1,
    @SerialName("totalWeek")
    var totalWeek:  Int = 1,
    @SerialName("totalWeekCount")
    var totalWeekCount:  Int = 1,
    @SerialName("totalMonth")
    var totalMonth:  Int = 1,
    @SerialName("totalMonthCount")
    var totalMonthCount:  Int = 1,
    @SerialName("currentDiff")
    var currentDiff:  Int = 1,
    @SerialName("number")
    var number: Int = 0,
    @SerialName("point")
    var point: Int = 0,
)
@Serializable
data class ItemBestInfo (
    @SerialName("number")
    var number: Int = 0,
    @SerialName("point")
    var point: Int = 0,
    @SerialName("cntPageRead")
    var cntPageRead: String = "",
    @SerialName("cntFavorite")
    var cntFavorite: String = "",
    @SerialName("cntRecom")
    var cntRecom: String = "",
    @SerialName("cntTotalComment")
    var cntTotalComment: String = "",
    @SerialName("total")
    var total:  Int = 1,
    @SerialName("totalCount")
    var totalCount:  Int = 1,
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("currentDiff")
    var currentDiff:  Int = 1,
)