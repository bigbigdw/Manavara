import android.annotation.SuppressLint
import com.bigbigdw.manavara.main.models.ItemBestInfo
import com.bigbigdw.manavara.main.models.ItemBookInfo
import com.google.gson.JsonObject
import org.json.JSONObject

@SuppressLint("SuspiciousIndentation")
fun convertItemBook(bestItemData : ItemBookInfo) : JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("writer", bestItemData.writer)
    jsonObject.addProperty("title", bestItemData.title)
    jsonObject.addProperty("bookImg", bestItemData.bookImg)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    jsonObject.addProperty("type", bestItemData.type)
    jsonObject.addProperty("info1", bestItemData.info1)
    jsonObject.addProperty("info2", bestItemData.info2)
    jsonObject.addProperty("info3", bestItemData.info3)
    jsonObject.addProperty("number", bestItemData.number)
    jsonObject.addProperty("point", bestItemData.point)
    jsonObject.addProperty("total", bestItemData.total)
    jsonObject.addProperty("totalCount", bestItemData.totalCount)
    jsonObject.addProperty("totalWeek", bestItemData.totalWeek)
    jsonObject.addProperty("totalWeekCount", bestItemData.totalWeekCount)
    jsonObject.addProperty("totalMonth", bestItemData.totalMonth)
    jsonObject.addProperty("totalMonthCount", bestItemData.totalMonthCount)
    jsonObject.addProperty("currentDiff", bestItemData.currentDiff)
    return jsonObject
}

@SuppressLint("SuspiciousIndentation")
fun convertItemBookJson(jsonObject: JSONObject): ItemBookInfo {

    return ItemBookInfo(
        writer = jsonObject.optString("writer"),
        title = jsonObject.optString("title"),
        bookImg = jsonObject.optString("bookImg"),
        bookCode = jsonObject.optString("bookCode"),
        type = jsonObject.optString("type"),
        info1 = jsonObject.optString("info1"),
        info2 = jsonObject.optString("info2"),
        info3 = jsonObject.optString("info3"),
        point = jsonObject.optInt("point"),
        number = jsonObject.optInt("number"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        totalWeek = jsonObject.optInt("totalWeek"),
        totalWeekCount = jsonObject.optInt("totalWeekCount"),
        totalMonth = jsonObject.optInt("totalMonth"),
        totalMonthCount = jsonObject.optInt("totalMonthCount"),
        currentDiff = jsonObject.optInt("currentDiff"),
    )
}

fun convertItemBestJson(jsonObject : JSONObject) : ItemBestInfo {

    return ItemBestInfo(
        point = jsonObject.optInt("point"),
        number = jsonObject.optInt("number"),
        info1 = jsonObject.optString("info1"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        bookCode = jsonObject.optString("bookCode"),
        currentDiff = jsonObject.optInt("currentDiff"),
    )
}

fun convertItemBest(bestItemData : ItemBestInfo) : JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("number", bestItemData.number)
    jsonObject.addProperty("point", bestItemData.point)
    jsonObject.addProperty("info1", bestItemData.info1)
    jsonObject.addProperty("total", bestItemData.total)
    jsonObject.addProperty("totalCount", bestItemData.totalCount)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    jsonObject.addProperty("currentDiff", bestItemData.currentDiff)
    return jsonObject
}


fun getPlatformGenre(type : String, platform: String) : ArrayList<String>{
    var array = ArrayList<String>()

    array = if(platform == "NAVER_SERIES"){
        if(type == "COMIC"){
            NaverSeriesComicGenreDir
        } else {
            NaverSeriesNovelGenreDir
        }
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