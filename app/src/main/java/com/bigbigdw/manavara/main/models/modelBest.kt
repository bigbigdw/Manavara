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
    @SerialName("current")
    var current: Int = 0,
    @SerialName("type")
    var type: String = "",
    @SerialName("info1")
    var info1: String = "",
    @SerialName("info2")
    var info2: String = "",
    @SerialName("info3")
    var info3: String = "",
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
)
data class ItemBestInfo (
    @SerialName("number")
    var number: Int = 0,
    @SerialName("info1")
    var info1: String = "",
    @SerialName("total")
    var total:  Int = 1,
    @SerialName("totalCount")
    var totalCount:  Int = 1,
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("currentDiff")
    var currentDiff:  Int = 1,
)