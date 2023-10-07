package com.bigbigdw.manavara.util

import androidx.compose.ui.graphics.Color
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.ui.theme.colorNAVER

fun comicKor(): List<String> {
    return listOf(
        "조아라",
        "노블레스",
        "프리미엄",
        "시리즈",
        "챌린지리그",
        "베스트리그",
//            "카카오페이지",
        "스테이지",
//            "리디북스",
        "원스토리",
        "문피아",
        "톡소다",
//            "미스터블루"
    )
}
fun novelListKor(): List<String> {
    return listOf(
        "조아라",
        "노블레스",
        "프리미엄",
        "시리즈",
        "챌린지리그",
        "베스트리그",
//            "카카오페이지",
        "스테이지",
//            "리디북스",
        "원스토리",
        "문피아",
        "톡소다",
//            "미스터블루"
    )
}

fun novelListEng(): List<String> {
    return listOf(
        "NAVER_SERIES",
        "노블레스",
        "프리미엄",
        "NAVER_SERIES",
        "챌린지리그",
        "베스트리그",
//            "카카오페이지",
        "스테이지",
//            "리디북스",
        "원스토리",
        "문피아",
        "톡소다",
//            "미스터블루"
    )
}


fun changePlatformNameEng(platform : String) : String {
    return when (platform) {
        "조아라" -> {
            "JOARA"
        }
        "노블레스" -> {
            "JOARA_NOBLESS"
        }
        "프리미엄" -> {
            "JOARA_PREMIUM"
        }
        "챌린지리그" -> {
            "CHALLENGE_LEAGUE"
        }
        "베스트리그" -> {
            "BEST_LEAGUE"
        }
        "시리즈" -> {
            "NAVER_SERIES"
        }
        "스테이지" -> {
            "KAKAO_STAGE"
        }
        "원스토리" -> {
            "ONESTORY"
        }
        "문피아" -> {
            "MUNPIA"
        }
        "톡소다" -> {
            "TOKSODA"
        }
        else -> {
            platform
        }
    }
}

fun changePlatformNameKor(platform : String) : String {
    return when (platform) {
        "JOARA" -> {
            "조아라"
        }
        "JOARA_NOBLESS" -> {
            "노블레스"
        }
        "JOARA_PREMIUM" -> {
            "프리미엄"
        }
        "CHALLENGE_LEAGUE" -> {
            "챌린지리그"
        }
        "BEST_LEAGUE" -> {
            "베스트리그"
        }
        "NAVER_SERIES" -> {
            "시리즈"
        }
        "KAKAO_STAGE" -> {
            "스테이지"
        }
        "ONESTORY" -> {
            "원스토리"
        }
        "MUNPIA" -> {
            "문피아"
        }
        "TOKSODA" -> {
            "톡소다"
        }
        else -> {
            platform
        }
    }
}

fun changeDetailNameKor(detail : String) : String {
    return if(detail.contains("TODAY_BEST")){
        detail.replace("TODAY_BEST", "투데이 베스트")
    } else if(detail.contains("WEEK_BEST")){
        detail.replace("WEEK_BEST", "주간 베스트")
    }  else if(detail.contains("MONTH_BEST")){
        detail.replace("MONTH_BEST", "월간 베스트")
    } else {
        detail
    }
}

fun getPlatformLogo(platform: String) : Int {
    return if(platform == "시리즈"){
        R.drawable.logo_naver
    } else {
        R.drawable.icon_best_wht
    }
}

fun getPlatformColor(platform: String) : Color {
    return if(platform == "시리즈"){
        colorNAVER
    } else {
        Color.Black
    }
}

fun getPlatformDescription(platform: String) : String {
    return if(platform == "시리즈"){
        "네이버 시리즈"
    } else {
        "하하"
    }
}

fun weekListAll(): List<String> {
    return listOf(
        "전체",
        "일요일",
        "월요일",
        "화요일",
        "수요일",
        "목요일",
        "금요일",
        "토요일"
    )
}

fun weekList(): List<String> {
    return listOf(
        "일요일",
        "월요일",
        "화요일",
        "수요일",
        "목요일",
        "금요일",
        "토요일"
    )
}