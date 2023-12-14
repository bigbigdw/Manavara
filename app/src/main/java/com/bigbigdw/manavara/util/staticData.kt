package com.bigbigdw.manavara.util

import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.models.MenuInfo
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color2EA259
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4996E8
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color536FD2
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color64C157
import com.bigbigdw.manavara.ui.theme.color79B4F8
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.color808CF8
import com.bigbigdw.manavara.ui.theme.color80BF78
import com.bigbigdw.manavara.ui.theme.color8AA6BD
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.color91CEC7
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorABD436
import com.bigbigdw.manavara.ui.theme.colorCHALLENGE
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorFDC24E
import com.bigbigdw.manavara.ui.theme.colorFFAC59
import com.bigbigdw.manavara.ui.theme.colorJOARA
import com.bigbigdw.manavara.ui.theme.colorKAKAO
import com.bigbigdw.manavara.ui.theme.colorMUNPIA
import com.bigbigdw.manavara.ui.theme.colorNAVER
import com.bigbigdw.manavara.ui.theme.colorNOBLESS
import com.bigbigdw.manavara.ui.theme.colorONESTORY
import com.bigbigdw.manavara.ui.theme.colorPREMIUM
import com.bigbigdw.manavara.ui.theme.colorRIDI
import com.bigbigdw.manavara.ui.theme.colorTOKSODA

fun comicListKor(): List<String> {
    return listOf(
        "시리즈",
    )
}
fun novelListKor(): List<String> {
    return listOf(
        "조아라",
        "노블레스",
        "프리미엄",
        "시리즈",
        "네이버 유료",
        "네이버 무료",
        "챌린지리그",
        "베스트리그",
        "리디 판타지",
        "리디 로맨스",
        "리디 로판",
        "스테이지",
        "원스토리 판타지",
        "원스토리 로맨스",
        "PASS 판타지",
        "PASS 로맨스",
        "문피아 유료",
        "문피아 무료",
        "톡소다",
        "톡소다 자유연재",
    )
}

fun novelListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "NAVER_SERIES",
        "NAVER_WEBNOVEL_PAY",
        "NAVER_WEBNOVEL_FREE",
        "NAVER_CHALLENGE",
        "NAVER_BEST",
        "RIDI_FANTAGY",
        "RIDI_ROMANCE",
        "RIDI_ROFAN",
        "KAKAO_STAGE",
        "ONESTORY_FANTAGY",
        "ONESTORY_ROMANCE",
        "ONESTORY_PASS_FANTAGY",
        "ONESTORY_PASS_ROMANCE",
        "MUNPIA_PAY",
        "MUNPIA_FREE",
        "TOKSODA",
        "TOKSODA_FREE",
    )
}

fun comicListEng(): List<String> {
    return listOf(
        "NAVER_SERIES"
    )
}

fun genreListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "RIDI_FANTAGY",
        "RIDI_ROMANCE",
        "RIDI_ROFAN",
        "KAKAO_STAGE",
        "MUNPIA_PAY",
        "MUNPIA_FREE",
        "TOKSODA",
        "TOKSODA_FREE",
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
        "네이버 유료" -> {
            "NAVER_WEBNOVEL_PAY"
        }
        "네이버 무료" -> {
            "NAVER_WEBNOVEL_FREE"
        }
        "리디 판타지" -> {
            "RIDI_FANTAGY"
        }
        "리디 로맨스" -> {
            "RIDI_ROMANCE"
        }
        "리디 로판" -> {
            "RIDI_ROFAN"
        }
        "스테이지" -> {
            "KAKAO_STAGE"
        }
        "원스토리 판타지" -> {
            "ONESTORY_FANTAGY"
        }
        "원스토리 로맨스" -> {
            "ONESTORY_ROMANCE"
        }
        "PASS 판타지" -> {
            "ONESTORY_PASS_FANTAGY"
        }
        "PASS 로맨스" -> {
            "ONESTORY_PASS_ROMANCE"
        }
        "문피아 유료" -> {
            "MUNPIA_PAY"
        }
        "문피아 무료" -> {
            "MUNPIA_FREE"
        }
        "톡소다" -> {
            "TOKSODA"
        }
        "톡소다 자유연재" -> {
            "TOKSODA_FREE"
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
        "NAVER_CHALLENGE" -> {
            "챌린지리그"
        }
        "NAVER_BEST" -> {
            "베스트리그"
        }
        "NAVER_SERIES" -> {
            "시리즈"
        }
        "NAVER_WEBNOVEL_PAY" -> {
            "네이버 유료"
        }
        "NAVER_WEBNOVEL_FREE" -> {
            "네이버 무료"
        }
        "RIDI_FANTAGY" -> {
            "리디 판타지"
        }
        "RIDI_ROMANCE" -> {
            "리디 로맨스"
        }
        "RIDI_ROFAN" -> {
            "리디 로판"
        }
        "KAKAO_STAGE" -> {
            "스테이지"
        }
        "ONESTORY_FANTAGY" -> {
            "원스토리 판타지"
        }
        "ONESTORY_ROMANCE" -> {
            "원스토리 로맨스"
        }
        "ONESTORY_PASS_FANTAGY" -> {
            "PASS 판타지"
        }
        "ONESTORY_PASS_ROMANCE" -> {
            "PASS 로맨스"
        }
        "MUNPIA_PAY" -> {
            "문피아 유료"
        }
        "MUNPIA_FREE" -> {
            "문피아 무료"
        }
        "TOKSODA" -> {
            "톡소다"
        }
        "TOKSODA_FREE" -> {
            "톡소다 자유연재"
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
        "네이버 유료" -> {
            R.drawable.logo_naver
        }
        "네이버 무료" -> {
            R.drawable.logo_naver
        }
        "챌린지리그" -> {
            R.drawable.logo_naver_challenge
        }
        "베스트리그" -> {
            R.drawable.logo_naver_challenge
        }
        "리디 판타지" -> {
            R.drawable.logo_ridibooks
        }
        "리디 로맨스" -> {
            R.drawable.logo_ridibooks
        }
        "리디 로판" -> {
            R.drawable.logo_ridibooks
        }
        "스테이지" -> {
            R.drawable.logo_kakaostage
        }
        "원스토리 판타지" -> {
            R.drawable.logo_onestore
        }
        "원스토리 로맨스" -> {
            R.drawable.logo_onestore
        }
        "PASS 판타지" -> {
            R.drawable.logo_onestore
        }
        "PASS 로맨스" -> {
            R.drawable.logo_onestore
        }
        "문피아 유료" -> {
            R.drawable.logo_munpia
        }
        "문피아 무료" -> {
            R.drawable.logo_munpia
        }
        "톡소다" -> {
            R.drawable.logo_toksoda
        }
        "톡소다 자유연재" -> {
            R.drawable.logo_toksoda
        }
        else -> {
            R.drawable.icon_best_wht
        }
    }
}

fun getPlatformLogoEng(platform: String) : Int {
    return when (platform) {
        "JOARA" -> {
            R.drawable.logo_joara
        }
        "JOARA_NOBLESS" -> {
            R.drawable.logo_joara_nobless
        }
        "JOARA_PREMIUM" -> {
            R.drawable.logo_joara_premium
        }
        "NAVER_SERIES" -> {
            R.drawable.logo_naver
        }
        "NAVER_WEBNOVEL_PAY" -> {
            R.drawable.logo_naver
        }
        "NAVER_WEBNOVEL_FREE" -> {
            R.drawable.logo_naver
        }
        "NAVER_CHALLENGE" -> {
            R.drawable.logo_naver_challenge
        }
        "NAVER_BEST" -> {
            R.drawable.logo_naver_challenge
        }
        "RIDI_FANTAGY" -> {
            R.drawable.logo_ridibooks
        }
        "RIDI_ROMANCE" -> {
            R.drawable.logo_ridibooks
        }
        "RIDI_ROFAN" -> {
            R.drawable.logo_ridibooks
        }
        "KAKAO_STAGE" -> {
            R.drawable.logo_kakaostage
        }
        "ONESTORY_FANTAGY" -> {
            R.drawable.logo_onestore
        }
        "ONESTORY_ROMANCE" -> {
            R.drawable.logo_onestore
        }
        "ONESTORY_PASS_FANTAGY" -> {
            R.drawable.logo_onestore
        }
        "ONESTORY_PASS_ROMANCE" -> {
            R.drawable.logo_onestore
        }
        "MUNPIA_PAY" -> {
            R.drawable.logo_munpia
        }
        "MUNPIA_FREE" -> {
            R.drawable.logo_munpia
        }
        "TOKSODA" -> {
            R.drawable.logo_toksoda
        }
        "TOKSODA_FREE" -> {
            R.drawable.logo_toksoda
        }
        else -> {
            R.drawable.ic_launcher
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
        "네이버 유료" -> {
            colorNAVER
        }
        "네이버 무료" -> {
            colorNAVER
        }
        "챌린지리그" -> {
            colorCHALLENGE
        }
        "베스트리그" -> {
            colorCHALLENGE
        }
        "리디 판타지" -> {
            colorRIDI
        }
        "리디 로맨스" -> {
            colorRIDI
        }
        "리디 로판" -> {
            colorRIDI
        }
        "스테이지" -> {
            colorKAKAO
        }
        "원스토리 판타지" -> {
            colorONESTORY
        }
        "원스토리 로맨스" -> {
            colorONESTORY
        }
        "PASS 판타지" -> {
            colorONESTORY
        }
        "PASS 로맨스" -> {
            colorONESTORY
        }
        "문피아 유료" -> {
            colorMUNPIA
        }
        "문피아 무료" -> {
            colorMUNPIA
        }
        "톡소다" -> {
            colorTOKSODA
        }
        "톡소다 자유연재" -> {
            colorTOKSODA
        }
        else -> {
            Color.Black
        }
    }
}

fun getPlatformColorEng(platform: String): Color {
    return when (platform) {
        "JOARA" -> {
            colorJOARA
        }
        "JOARA_NOBLESS" -> {
            colorNOBLESS
        }
        "JOARA_PREMIUM" -> {
            colorPREMIUM
        }
        "NAVER_SERIES" -> {
            colorNAVER
        }
        "NAVER_WEBNOVEL_PAY" -> {
            colorNAVER
        }
        "NAVER_WEBNOVEL_FREE" -> {
            colorNAVER
        }
        "NAVER_CHALLENGE" -> {
            colorCHALLENGE
        }
        "NAVER_BEST" -> {
            colorCHALLENGE
        }
        "RIDI_FANTAGY" -> {
            colorRIDI
        }
        "RIDI_ROMANCE" -> {
            colorRIDI
        }
        "RIDI_ROFAN" -> {
            colorRIDI
        }
        "KAKAO_STAGE" -> {
            colorKAKAO
        }
        "ONESTORY_FANTAGY" -> {
            colorONESTORY
        }
        "ONESTORY_ROMANCE" -> {
            colorONESTORY
        }
        "ONESTORY_PASS_FANTAGY" -> {
            colorONESTORY
        }
        "ONESTORY_PASS_ROMANCE" -> {
            colorONESTORY
        }
        "MUNPIA_PAY" -> {
            colorMUNPIA
        }
        "MUNPIA_FREE" -> {
            colorMUNPIA
        }
        "TOKSODA" -> {
            colorTOKSODA
        }
        "TOKSODA_FREE" -> {
            colorTOKSODA
        }
        else -> {
            Color.Black
        }
    }
}

fun getPlatformDescriptionEng(platform: String) : String {
    return when (platform) {
        "JOARA" -> {
            "조아라"
        }
        "JOARA_NOBLESS" -> {
            "조아라 노블레스"
        }
        "JOARA_PREMIUM" -> {
            "조아라 프리미엄"
        }
        "NAVER_SERIES" -> {
            "네이버 시리즈"
        }
        "NAVER_WEBNOVEL_PAY" -> {
            "네이버 유료"
        }
        "NAVER_WEBNOVEL_FREE" -> {
            "네이버 무료"
        }
        "NAVER_CHALLENGE" -> {
            "네이버 챌린지 리그"
        }
        "NAVER_BEST" -> {
            "네이버 베스트 리그"
        }
        "RIDI_FANTAGY" -> {
            "리디북스 판타지"
        }
        "RIDI_ROMANCE" -> {
            "리디북스 로맨스"
        }
        "RIDI_ROFAN" -> {
            "리디 로판"
        }
        "KAKAO_STAGE" -> {
            "카카오 스테이지"
        }
        "ONESTORY_FANTAGY" -> {
            "원스토리 판타지"
        }
        "ONESTORY_ROMANCE" -> {
            "원스토리 로맨스"
        }
        "ONESTORY_PASS_FANTAGY" -> {
            "PASS 판타지"
        }
        "ONESTORY_PASS_ROMANCE" -> {
            "PASS 로맨스"
        }
        "MUNPIA_PAY" -> {
            "문피아 유료"
        }
        "MUNPIA_FREE" -> {
            "문피아 무료"
        }
        "TOKSODA" -> {
            "톡소다"
        }
        "TOKSODA_FREE" -> {
            "톡소다 자유연재"
        }
        else -> {
            "하하"
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
        "네이버 유료" -> {
            "네이버 웹소설 유료"
        }
        "네이버 무료" -> {
            "네이버 웹소설 무료"
        }
        "챌린지리그" -> {
            "네이버 챌린지 리그"
        }
        "베스트리그" -> {
            "네이버 베스트 리그"
        }
        "리디 판타지" -> {
            "리디북스 판타지"
        }
        "리디 로맨스" -> {
            "리디북스 로맨스"
        }
        "리디 로판" -> {
            "리디 로맨스 판타지"
        }
        "스테이지" -> {
            "카카오 스테이지"
        }
        "원스토리 판타지" -> {
            "원스토리 판타지"
        }
        "원스토리 로맨스" -> {
            "원스토리 로맨스"
        }
        "PASS 판타지" -> {
            "원스토리 PASS 판타지"
        }
        "원스토리 PASS 로맨스" -> {
            "원스토리 PASS 로맨스"
        }
        "문피아 유료" -> {
            "문피아 유료"
        }
        "문피아 무료" -> {
            "문피아 무료"
        }
        "톡소다" -> {
            "톡소다"
        }
        "톡소다 자유연재" -> {
            "톡소다 자유연재"
        }
        else -> {
            platform
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

fun weekListOneWord(): List<String> {
    return listOf(
        "일",
        "월",
        "화",
        "수",
        "목",
        "금",
        "토"
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

fun getPlatformDataKeyNovel(platform : String) : Preferences.Key<String> {
    return when (platform) {
        "JOARA" -> {
           DataStoreManager.JOARA
        }
        "JOARA_NOBLESS" -> {
            DataStoreManager.JOARA_NOBLESS
        }
        "JOARA_PREMIUM" -> {
            DataStoreManager.JOARA_PREMIUM
        }
        "NAVER_CHALLENGE" -> {
            DataStoreManager.NAVER_CHALLENGE
        }
        "NAVER_BEST" -> {
            DataStoreManager.NAVER_BEST
        }
        "NAVER_SERIES" -> {
            DataStoreManager.NAVER_SERIES_NOVEL
        }
        "NAVER_WEBNOVEL_PAY" -> {
            DataStoreManager.NAVER_WEBNOVEL_PAY
        }
        "NAVER_WEBNOVEL_FREE" -> {
            DataStoreManager.NAVER_WEBNOVEL_FREE
        }
        "RIDI_FANTAGY" -> {
            DataStoreManager.RIDI_FANTAGY
        }
        "RIDI_ROMANCE" -> {
            DataStoreManager.RIDI_ROMANCE
        }
        "RIDI_ROFAN" -> {
            DataStoreManager.RIDI_ROFAN
        }
        "KAKAO_STAGE" -> {
            DataStoreManager.KAKAO_STAGE
        }
        "ONESTORY_FANTAGY" -> {
            DataStoreManager.ONESTORY_FANTAGY
        }
        "ONESTORY_ROMANCE" -> {
            DataStoreManager.ONESTORY_ROMANCE
        }
        "ONESTORY_PASS_FANTAGY" -> {
            DataStoreManager.ONESTORY_PASS_FANTAGY
        }
        "ONESTORY_PASS_ROMANCE" -> {
            DataStoreManager.ONESTORY_PASS_ROMANCE
        }
        "MUNPIA_PAY" -> {
            DataStoreManager.MUNPIA_PAY
        }
        "MUNPIA_FREE" -> {
            DataStoreManager.MUNPIA_FREE
        }
        "TOKSODA" -> {
            DataStoreManager.TOKSODA
        }
        "TOKSODA_FREE" -> {
            DataStoreManager.TOKSODA_FREE
        }
        else -> {
            DataStoreManager.TEST
        }
    }
}

fun keywordListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "ONESTORY_FANTAGY",
        "ONESTORY_ROMANCE",
        "ONESTORY_PASS_FANTAGY",
        "ONESTORY_PASS_ROMANCE",
        "TOKSODA",
        "TOKSODA_FREE",
        "RIDI_FANTAGY",
        "RIDI_ROMANCE", "RIDI_ROFAN",
        "NAVER_WEBNOVEL_FREE",
        "NAVER_WEBNOVEL_PAY",
        "NAVER_BEST",
        "NAVER_CHALLENGE",
    )
}


fun getPlatformDataKeyComic(platform : String) : Preferences.Key<String> {
    return when (platform) {
        "NAVER_SERIES" -> {
            DataStoreManager.NAVER_SERIES_COMIC
        }
        else -> {
            DataStoreManager.TEST
        }
    }
}

val colorList = arrayListOf(
    color4AD7CF,
    color5372DE,
    color998DF9,
    colorEA927C,
    colorABD436,
    colorF17FA0,
    color21C2EC,
    color31C3AE,
    color7C81FF,
    color64C157,
    colorF17666,
    color536FD2,
    color4996E8,
    colorFDC24E,
    color80BF78,
    color91CEC7,
    color79B4F8,
    color8AA6BD,
    color2EA259,
    color808CF8,
    colorFFAC59,
    color8F8F8F
)

fun getBestDetailLogoMobile(menu: String) : Int {
    return when (menu) {
        "작품 정보" -> {
            R.drawable.icon_novel_wht
        }
        "작품 댓글" -> {
            R.drawable.icon_comment_wht
        }
        "작가의 다른 작품" -> {
            R.drawable.icon_auther_other_wht
        }
        "평점 분석" -> {
            R.drawable.icon_recom_wht
        }
        "선호작 분석" -> {
            R.drawable.icon_fav_wht
        }
        "조회 분석" -> {
            R.drawable.icon_watch_wht
        }
        "댓글 분석" -> {
            R.drawable.icon_commentlist_wht
        }
        "랭킹 분석" -> {
            R.drawable.icon_trophy_wht
        }
        "최근 분석" -> {
            R.drawable.logo_transparents
        }
        else -> {
            R.drawable.icon_best_wht
        }
    }
}

fun getBestDetailLogo(menu: String) : Int {
    return when (menu) {
        "작품 정보" -> {
            R.drawable.icon_novel_gr
        }
        "작품 댓글" -> {
            R.drawable.icon_comment
        }
        "작가의 다른 작품" -> {
            R.drawable.icon_auther_other
        }
        "평점 분석" -> {
            R.drawable.icon_recom
        }
        "선호작 분석" -> {
            R.drawable.icon_fav
        }
        "조회 분석" -> {
            R.drawable.icon_watch
        }
        "댓글 분석" -> {
            R.drawable.icon_commentlist
        }
        "랭킹 분석" -> {
            R.drawable.icon_trophy_gr
        }
        "최근 분석" -> {
            R.drawable.logo_transparents
        }
        else -> {
            R.drawable.icon_best_gr
        }
    }
}

fun getBestDetailDescription(menu: String) : String {
    return when (menu) {
        "작품 정보" -> {
           "작품이 가진 정보"
        }
        "작품 댓글" -> {
            "작품에 달린 댓글"
        }
        "작가의 다른 작품" -> {
            "작가가 쓴 다른 작품 조회"
        }
        "평점 분석" -> {
            "마나바라에서 수집한 평점 현황"
        }
        "선호작 분석" -> {
            "마나바라에서 수집한 선호작 현황"
        }
        "조회 분석" -> {
            "마나바라에서 수집한 조회 현황"
        }
        "댓글 분석" -> {
            "마나바라에서 수집한 댓글 현황"
        }
        "랭킹 분석" -> {
            "마나바라에서 수집한 랭킹 현황"
        }
        "최근 분석" -> {
            "근 일주일간 모든 분석 유형 현황"
        }
        else -> {
            ""
        }
    }
}

val menuListManavara = arrayListOf(
    MenuInfo(needLine = false, image = R.drawable.ic_launcher, menu = "나의 기록", body = "https://m.comic.naver.com/event/yearend/2023"),
    MenuInfo(needLine = false, image = R.drawable.ic_launcher, menu = "내가 분석한 작품", body = "내가 데이터를 모으고 있는 작품들 보기"),
    MenuInfo(needLine = false, image = R.drawable.ic_launcher, menu = "유저 옵션", body = "마나바라 유저 옵션"),
    MenuInfo(needLine = true, image = R.drawable.ic_launcher, menu = "메세지함", body = "공지사항 및 알림 등 메세지 확인"),
    MenuInfo(needLine = false, image = R.drawable.ic_launcher, menu = "나의 PICK 보기", body = "내가 엄선한 작품들 보기"),
    MenuInfo(needLine = true, image = R.drawable.ic_launcher, menu = "다른 PICK 보기", body = "다른 사람이 엄선한 작품들 보기"),
    MenuInfo(needLine = false, image = R.drawable.ic_launcher, menu = "이벤트", body = "플랫폼별 진행중인 이벤트 확인"),
    MenuInfo(needLine = false, image = R.drawable.ic_launcher, menu = "커뮤니티", body = "조아라 자유게시판 및 DC 커뮤니티 확인"),
)

val menuListDatabase = arrayListOf(
    MenuInfo(needLine = false, image = R.drawable.icon_novel_wht, menu = "마나바라 베스트 웹소설 DB", body = "마나바라에 기록된 베스트 웹소설 리스트"),
    MenuInfo(needLine = true, image = R.drawable.icon_novel_wht, menu = "신규 작품", body = "최근에 등록된 작품 확인"),
    MenuInfo(needLine = false, image = R.drawable.icon_best_wht, menu = "주차별 웹소설 베스트", body = "주차별 웹소설 베스트 리스트"),
    MenuInfo(needLine = true, image = R.drawable.icon_best_wht, menu = "월별 웹소설 베스트", body = "월별 웹소설 베스트 리스트"),
    MenuInfo(needLine = false, image = R.drawable.icon_genre_wht, menu = "투데이 장르 현황", body = "웹소설 플랫폼별 투데이 장르 리스트"),
    MenuInfo(needLine = false, image = R.drawable.icon_genre_wht, menu = "주간 장르 현황", body = "웹소설 주간 주차별 장르 리스트"),
    MenuInfo(needLine = false, image = R.drawable.icon_genre_wht, menu = "월간 장르 현황", body = "웹소설 플랫폼별 월간 장르 리스트"),
    MenuInfo(needLine = false, image = R.drawable.icon_genre_wht, menu = "주차별 장르 현황", body = "웹소설 플랫폼별 주차별 장르 리스트"),
    MenuInfo(needLine = false, image = R.drawable.icon_genre_wht, menu = "월별 장르 현황", body = "웹소설 플랫폼별 월별 장르 리스트"),
    MenuInfo(needLine = false, image = R.drawable.icon_genre_wht, menu = "장르 리스트 작품", body = "장르별 작품 리스트 보기"),
    MenuInfo(needLine = true, image = R.drawable.icon_genre_wht, menu = "장르 리스트 현황", body = "장르별 랭킹 변동 현황"),
    MenuInfo(needLine = false, image = R.drawable.icon_keyword_wht, menu = "투데이 키워드 현황", body = "웹소설 주차별 투데이 키워드 현황"),
    MenuInfo(needLine = false, image = R.drawable.icon_keyword_wht, menu = "주간 키워드 현황", body = "웹소설 주간 키워드 현황"),
    MenuInfo(needLine = false, image = R.drawable.icon_keyword_wht, menu = "월간 키워드 현황", body = "웹소설 월간 키워드 현황"),
    MenuInfo(needLine = false, image = R.drawable.icon_keyword_wht, menu = "주차별 키워드 현황", body = "웹소설 주차별 월별 키워드 현황"),
    MenuInfo(needLine = false, image = R.drawable.icon_keyword_wht, menu = "월별 키워드 현황", body = "웹소설 플랫폼별 월별 키워드 현황"),
    MenuInfo(needLine = false, image = R.drawable.icon_keyword_wht, menu = "키워드 리스트 작품", body = "웹소설 키워드 리스트 작품 보기"),
    MenuInfo(needLine = true, image = R.drawable.icon_keyword_wht, menu = "키워드 리스트 현황", body = "웹소설 키워드 리스트 작품 보기"),
    MenuInfo(needLine = false, image = R.drawable.icon_search_wht, menu = "마나바라 DB 검색", body = "마나바라 DB에 저장된 작품 검색"),
    MenuInfo(needLine = false, image = R.drawable.icon_search_wht, menu = "작품 검색", body = "플랫폼과 무관하게 작품 검색 진행"),
    MenuInfo(needLine = false, image = R.drawable.icon_search_wht, menu = "북코드 검색", body = "북코드로 작품 찾기"),
)