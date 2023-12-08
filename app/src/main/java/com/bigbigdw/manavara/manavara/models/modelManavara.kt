package com.bigbigdw.manavara.manavara.models

class EventData(
    var link: String = "",
    var imgfile: String = "",
    var title: String = "",
    var data: String = "",
    var date: String = "",
    var number: Int = 0,
    var type: String = "",
    var memo: String = ""
)

data class CommunityBoard(
    var title: String = "",
    var link: String = "",
    var date: String = ""
)