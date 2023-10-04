package com.bigbigdw.manavara.util

fun novelKor(): List<String> {
    return listOf(
        "조아라",
        "노블레스",
        "프리미엄",
        "네이버시리즈",
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
        "네이버시리즈" -> {
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
            "없음"
        }
    }
}