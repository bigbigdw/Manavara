package com.bigbigdw.manavara.manavara.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ItemAlert (
    @PrimaryKey
    @SerialName("date")
    var date: String = "",
    @SerialName("title")
    var title: String = "",
    @SerialName("body")
    var body: String = "",
)