package com.bigbigdw.manavara.best.screen

import com.bigbigdw.manavara.R



sealed class ScreemBottomItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object NOVEL : ScreemBottomItem("웹소설", R.drawable.icon_novel_wht, R.drawable.icon_novel_gr, "NOVEL")
    object COMIC : ScreemBottomItem("웹툰", R.drawable.icon_webtoon_wht, R.drawable.icon_webtoon_gr, "COMIC")
    object MANAVARA : ScreemBottomItem("마나바라", R.drawable.ic_launcher_gr, R.drawable.ic_launcher_gr, "MANAVARA")
    object PICK : ScreemBottomItem("마이픽", R.drawable.icon_json_wht, R.drawable.icon_json_gr, "PICK")
    object EVENT : ScreemBottomItem("이벤트", R.drawable.icon_trophy_wht, R.drawable.icon_trophy_gr, "EVENT")
}