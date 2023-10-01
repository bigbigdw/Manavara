package com.bigbigdw.manavara.main.models

data class UserInfo (
    var userNickName: String = "",
    var userEmail: String = "",
    var userFcmToken : String = "",
    var userUID : String = "",
)

data class DataFCMBodyNotification(
    var title: String = "",
    var body: String = "",
    var click_action : String = "",
)
