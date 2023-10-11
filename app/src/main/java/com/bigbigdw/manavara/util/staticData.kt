package com.bigbigdw.manavara.util

import androidx.compose.ui.graphics.Color
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.ui.theme.colorCHALLENGE
import com.bigbigdw.manavara.ui.theme.colorJOARA
import com.bigbigdw.manavara.ui.theme.colorKAKAO
import com.bigbigdw.manavara.ui.theme.colorMUNPIA
import com.bigbigdw.manavara.ui.theme.colorNAVER
import com.bigbigdw.manavara.ui.theme.colorNOBLESS
import com.bigbigdw.manavara.ui.theme.colorONESTORY
import com.bigbigdw.manavara.ui.theme.colorPREMIUM
import com.bigbigdw.manavara.ui.theme.colorRIDI
import com.bigbigdw.manavara.ui.theme.colorTOKSODA

fun comicKor(): List<String> {
    return listOf(
        "조아라",
        "노블레스",
        "프리미엄",
        "시리즈",
        "챌린지리그",
        "베스트리그",
        "스테이지",
        "리디북스",
        "원스토리",
        "문피아",
        "톡소다",
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
        "스테이지",
        "원스토리",
        "문피아",
        "톡소다",
    )
}

fun novelListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "NAVER_SERIES",
        "NAVER_CHALLENGE",
        "NAVER_BEST",
        "KAKAO_STAGE",
        "ONESTORY",
        "MUNPIA",
        "TOKSODA",
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
            "NAVER_CHALLENGE"
        }
        "베스트리그" -> {
            "NAVER_BEST"
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
    return when (platform) {
        "조아라" -> {
            R.drawable.logo_joara
        }
        "노블레스" -> {
            R.drawable.logo_joara_nobless
        }
        "프리미엄" -> {
            R.drawable.logo_joara_premium
        }
        "시리즈" -> {
            R.drawable.logo_naver
        }
        "챌린지리그" -> {
            R.drawable.logo_naver_challenge
        }
        "베스트리그" -> {
            R.drawable.logo_naver_challenge
        }
        "스테이지" -> {
            R.drawable.logo_kakaostage
        }

        "원스토리" -> {
            R.drawable.logo_onestore
        }
        "문피아" -> {
            R.drawable.logo_munpia
        }
        "톡소다" -> {
            R.drawable.logo_toksoda
        }
        else -> {
            R.drawable.icon_best_wht
        }
    }
}

fun getPlatformColor(platform: String): Color {
    return when (platform) {
        "조아라" -> {
            colorJOARA
        }
        "노블레스" -> {
            colorNOBLESS
        }
        "프리미엄" -> {
            colorPREMIUM
        }
        "시리즈" -> {
            colorNAVER
        }
        "챌린지리그" -> {
            colorCHALLENGE
        }
        "베스트리그" -> {
            colorCHALLENGE
        }
        "스테이지" -> {
            colorKAKAO
        }

        "원스토리" -> {
            colorONESTORY
        }
        "문피아" -> {
            colorMUNPIA
        }
        "톡소다" -> {
            colorTOKSODA
        }
        else -> {
            Color.Black
        }
    }
}

fun getPlatformDescription(platform: String) : String {
    return when (platform) {
        "시리즈" -> {
            "네이버 시리즈"
        }
        "프리미엄" -> {
            "조아라 프리미엄"
        }
        "노블레스" -> {
            "조아라 노블레스"
        }
        "조아라" -> {
            "조아라"
        }
        "챌린지리그" -> {
            "네이버 챌린지 리그"
        }
        "베스트리그" -> {
            "네이버 베스트 리그"
        }
        "스테이지" -> {
            "카카오 스테이지"
        }
        "원스토리" -> {
            "원스토리"
        }
        "문피아" -> {
            "문피아"
        }
        "톡소다" -> {
            "톡소다"
        }
        else -> {
            "하하"
        }
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

fun getWeekDate(date : String) : Int {
    return when (date) {
        "일요일" -> {
            0
        }
        "월요일" -> {
            1
        }
        "화요일" -> {
            2
        }
        "수요일" -> {
            3
        }
        "목요일" -> {
            4
        }
        "금요일" -> {
            5
        }
        "토요일" -> {
            6
        }
        else -> {
            0
        }
    }
}

fun geMonthDate(date : String) : Int {
    return when (date) {
        "1주차" -> {
            0
        }
        "2주차" -> {
            1
        }
        "3주차" -> {
            2
        }
        "4주차" -> {
            3
        }
        "5주차" -> {
            4
        }
        "6주차" -> {
            5
        }
        else -> {
            0
        }
    }
}
