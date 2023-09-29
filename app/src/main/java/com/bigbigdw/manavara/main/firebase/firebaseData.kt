package com.bigbigdw.manavara.main.firebase

class DataFCMBody(
    var to: String? = "/topics/all",
    var priority: String? = "high",
    var data: DataFCMBodyData? = null,
    var notification: DataFCMBodyNotification? = null,
)

class DataFCMBodyData(
    var activity: String = "",
    var data: String = "",
)

data class DataFCMBodyNotification(
    var title: String = "",
    var body: String = "",
    var click_action : String = "",
)

data class FCMAlert (
    var date: String = "",
    var title: String = "",
    var body: String = "",
    var data : String = "",
    var activity : String = "",
)
