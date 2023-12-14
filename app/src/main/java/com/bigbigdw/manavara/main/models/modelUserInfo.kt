package com.bigbigdw.manavara.main.models

data class UserInfo (
    var userNickName: String = "",
    var userEmail: String = "",
    var userFcmToken : String = "",
    var userUID : String = "",
    var userStatus : String = "LOCKED",
)

data class MenuInfo (
    var needLine: Boolean = false,
    var image : Int = 0,
    var menu : String = "",
    var body : String = "",
)