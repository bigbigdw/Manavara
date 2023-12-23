package com.bigbigdw.manavara.manavara.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemAlert (
    @SerialName("date")
    var date: String = "",
    @SerialName("title")
    var title: String = "",
    @SerialName("body")
    var body: String = "",
    @SerialName("data")
    var data : String = "",
    @SerialName("activity")
    var activity : String = "",
)
