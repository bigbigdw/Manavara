package com.bigbigdw.manavara.main.screen

import com.bigbigdw.manavara.R



sealed class ScreemBottomItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object NOVEL : ScreemBottomItem("웹소설", R.drawable.icon_novel_wht, R.drawable.icon_novel_gr, "NOVEL")
    object COMIC : ScreemBottomItem("웹툰", R.drawable.icon_webtoon_wht, R.drawable.icon_webtoon_gr, "COMIC")
    object FCM : ScreemBottomItem("이벤트", R.drawable.icon_fcm_wht, R.drawable.icon_fcm_gr, "FCM")
    object JSON : ScreemBottomItem("코드검색", R.drawable.icon_json_wht, R.drawable.icon_json_gr, "JSON")
    object TROPHY : ScreemBottomItem("커뮤니티", R.drawable.icon_trophy_wht, R.drawable.icon_trophy_gr, "TROPHY")
}