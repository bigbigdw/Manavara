package com.bigbigdw.manavara.main.screen

import com.bigbigdw.manavara.R



sealed class ScreemBottomItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object NOVEL : ScreemBottomItem("웹소설", R.drawable.icon_novel_wht, R.drawable.icon_novel_gr, "NOVEL")
    object COMIC : ScreemBottomItem("웹툰", R.drawable.icon_webtoon_wht, R.drawable.icon_webtoon_gr, "COMIC")
    object MANAVARA : ScreemBottomItem("마나바라", R.drawable.ic_launcher_gr, R.drawable.ic_launcher_gr, "MANAVARA")
    object NOVELDB : ScreemBottomItem("소설DB", R.drawable.icon_analyze_wht, R.drawable.icon_analyze_gr, "NOVELDB")
    object WEBTOONDB : ScreemBottomItem("웹툰DB", R.drawable.icon_collection_wht, R.drawable.icon_collection_gr, "WEBTOONDB")

}