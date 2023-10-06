package com.bigbigdw.manavara.util

fun comicKor(): List<String> {
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
            "네이버시리즈"
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