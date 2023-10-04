package com.bigbigdw.manavara.main.screen

import com.bigbigdw.manavara.R



sealed class ScreemBottomItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object BEST : ScreemBottomItem("베스트", R.drawable.icon_best, R.drawable.icon_best_gr, "BEST")
    object SETTING : ScreemBottomItem("세팅", R.drawable.icon_setting, R.drawable.icon_setting_gr, "SETTING")
    object FCM : ScreemBottomItem("FCM", R.drawable.icon_fcm, R.drawable.icon_fcm_gr, "FCM")
    object JSON : ScreemBottomItem("JSON", R.drawable.icon_json, R.drawable.icon_json_gr, "JSON")
    object TROPHY : ScreemBottomItem("트로피", R.drawable.icon_trophy, R.drawable.icon_trophy_gr, "TROPHY")
}