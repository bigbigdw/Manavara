package com.bigbigdw.manavara.util

import androidx.compose.ui.graphics.Color
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.ui.theme.colorJOARA
import com.bigbigdw.manavara.ui.theme.colorNAVER
import com.bigbigdw.manavara.ui.theme.colorNOBLESS
import com.bigbigdw.manavara.ui.theme.colorPREMIUM

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
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
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
    return when (platform) {
        "시리즈" -> {
            R.drawable.logo_naver
        }
        "프리미엄" -> {
            R.drawable.logo_joara_premium
        }
        "노블레스" -> {
            R.drawable.logo_joara_nobless
        }
        "조아라" -> {
            R.drawable.logo_joara
        }
        else -> {
            R.drawable.icon_best_wht
        }
    }
}

fun getPlatformColor(platform: String): Color {
    return when (platform) {
        "시리즈" -> {
            colorNAVER
        }
        "프리미엄" -> {
            colorPREMIUM
        }
        "노블레스" -> {
            colorNOBLESS
        }
        "조아라" -> {
            colorJOARA
        }
        else -> {
            Color.Black
        }
    }
}

fun getPlatformDescription(platform: String) : String {
    return if(platform == "시리즈"){
        "네이버 시리즈"
    } else if(platform == "프리미엄"){
        "조아라 프리미엄"
    }  else if(platform == "노블레스"){
        "조아라 노블레스"
    }  else if(platform == "조아라"){
        "조아라"
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

fun getJoaraGenre(genre : String) : String {
    when (genre) {
        "0" -> {
            return "ALL"
        }
        "1" -> {
            return "FANTAGY"
        }
        "2" -> {
            return "MARTIAL_ARTS"
        }
        "5" -> {
            return "MODREN_FANTAGY"
        }
        "22" -> {
            return "ROMANCE_FANTAGY"
        }
        "25" -> {
            return "ROMANCE"
        }
        else -> {
            return genre
        }
    }
}

fun getJoaraGenreKor(genre : String) : String {
    when (genre) {
        "ALL" -> {
            return "전체"
        }
        "FANTAGY" -> {
            return "판타지"
        }
        "MARTIAL_ARTS" -> {
            return "무협"
        }
        "MODREN_FANTAGY" -> {
            return "모판"
        }
        "ROMANCE_FANTAGY" -> {
            return "로판"
        }
        "ROMANCE" -> {
            return "로맨스"
        }
        else -> {
            return genre
        }
    }
}

val JoaraGenre = arrayListOf(
    "0",
    "1",
    "25",
    "2",
    "5",
    "22",
)

val JoaraGenreDir = arrayListOf(
    "ALL",
    "FANTAGY",
    "MARTIAL_ARTS",
    "MODREN_FANTAGY",
    "ROMANCE_FANTAGY",
    "ROMANCE",
)

fun getPlatformGenre(type : String, platform: String) : ArrayList<String>{

    val array: ArrayList<String> =
        if (platform == "NAVER_SERIES") {
            if (type == "COMIC") {
                NaverSeriesComicGenreDir
            } else {
                NaverSeriesNovelGenreDir
            }
        } else if (platform.contains("JOARA")) {
            JoaraGenreDir
        } else {
            NaverSeriesComicGenreDir
        }


    return array
}

val NaverSeriesComicGenreDir = arrayListOf(
    "ALL",
    "ACTION",
    "BL",
    "DRAMA",
    "MELO",
    "YOUNG",
)

val NaverSeriesNovelGenreDir = arrayListOf(
    "ALL",
    "ROMANCE",
    "ROMANCE_FANTASY",
    "FANTASY",
    "MODERN_FANTASY",
    "MARTIAL_ARTS",
)

fun getNaverSeriesGenre(genre : String) : String {
    when (genre) {
        "ALL" -> {
            return "ALL"
        }
        "99" -> {
            return "MELO"
        }
        "93" -> {
            return "DRAMA"
        }
        "90" -> {
            return "YOUNG"
        }
        "88" -> {
            return "ACTION"
        }
        "107" -> {
            return "BL"
        }
        "201" -> {
            return "ROMANCE"
        }
        "207" -> {
            return "ROMANCE_FANTASY"
        }
        "202" -> {
            return "FANTASY"
        }
        "208" -> {
            return "MODERN_FANTASY"
        }
        "206" -> {
            return "MARTIAL_ARTS"
        }
        else -> {
            return "없음"
        }
    }
}

fun getNaverSeriesGenreEngToKor(genre : String) : String {
    return when (genre) {
        "ALL" -> {
            "전체"
        }
        "MELO" -> {
            "멜로"
        }
        "DRAMA" -> {
            "드라마"
        }
        "YOUNG" -> {
            "소년"
        }
        "ACTION" -> {
            "액션"
        }
        "BL" -> {
            "BL"
        }
        "ROMANCE" -> {
            "로맨스"
        }
        "ROMANCE_FANTASY" -> {
            "로판"
        }
        "FANTASY" -> {
            "판타지"
        }
        "MODERN_FANTASY" -> {
            "현판"
        }
        "MARTIAL_ARTS" -> {
            "무협"
        }
        else -> {
            "없음"
        }
    }
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
