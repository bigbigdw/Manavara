import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.firebase.DataFCMBody
import com.bigbigdw.manavara.firebase.DataFCMBodyData
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.firebase.FCMAlert
import com.bigbigdw.manavara.firebase.FWorkManagerResult
import com.bigbigdw.manavara.firebase.FirebaseService
import com.bigbigdw.manavara.main.ActivityMain
import com.bigbigdw.manavara.main.models.UserInfo
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
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorFDC24E
import com.bigbigdw.manavara.ui.theme.colorFFAC59
import com.bigbigdw.manavara.util.DBDate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("SuspiciousIndentation")
fun convertItemBook(bestItemData: ItemBookInfo): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("writer", bestItemData.writer)
    jsonObject.addProperty("title", bestItemData.title)
    jsonObject.addProperty("bookImg", bestItemData.bookImg)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    jsonObject.addProperty("type", bestItemData.type)
    jsonObject.addProperty("intro", bestItemData.intro)
    jsonObject.addProperty("cntPageRead", bestItemData.cntPageRead)
    jsonObject.addProperty("cntFavorite", bestItemData.cntFavorite)
    jsonObject.addProperty("cntRecom", bestItemData.cntRecom)
    jsonObject.addProperty("cntTotalComment", bestItemData.cntTotalComment)
    jsonObject.addProperty("cntChapter", bestItemData.cntChapter)
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
        intro = jsonObject.optString("intro"),
        cntPageRead = jsonObject.optString("cntPageRead"),
        cntFavorite = jsonObject.optString("cntFavorite"),
        cntRecom = jsonObject.optString("cntRecom"),
        cntTotalComment = jsonObject.optString("cntTotalComment"),
        cntChapter = jsonObject.optString("cntChapter"),
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

fun convertItemBestJson(jsonObject: JSONObject): ItemBestInfo {

    return ItemBestInfo(
        point = jsonObject.optInt("point"),
        number = jsonObject.optInt("number"),
        cntPageRead = jsonObject.optString("cntPageRead"),
        cntFavorite = jsonObject.optString("cntFavorite"),
        cntRecom = jsonObject.optString("cntRecom"),
        cntTotalComment = jsonObject.optString("cntTotalComment"),
        total = jsonObject.optInt("total"),
        totalCount = jsonObject.optInt("totalCount"),
        bookCode = jsonObject.optString("bookCode"),
        currentDiff = jsonObject.optInt("currentDiff"),
    )
}

fun convertItemBest(bestItemData: ItemBestInfo): JsonObject {
    val jsonObject = JsonObject()
    jsonObject.addProperty("number", bestItemData.number)
    jsonObject.addProperty("point", bestItemData.point)
    jsonObject.addProperty("cntPageRead", bestItemData.cntPageRead)
    jsonObject.addProperty("cntFavorite", bestItemData.cntFavorite)
    jsonObject.addProperty("cntRecom", bestItemData.cntRecom)
    jsonObject.addProperty("cntTotalComment", bestItemData.cntTotalComment)
    jsonObject.addProperty("total", bestItemData.total)
    jsonObject.addProperty("totalCount", bestItemData.totalCount)
    jsonObject.addProperty("bookCode", bestItemData.bookCode)
    jsonObject.addProperty("currentDiff", bestItemData.currentDiff)
    return jsonObject
}

fun convertItemKeywordJson(itemBestKeyword: ItemKeyword): JsonObject {
    val jsonObject = JsonObject()

    jsonObject.addProperty("title", itemBestKeyword.title)
    jsonObject.addProperty("value", itemBestKeyword.value)
    return jsonObject
}

@SuppressLint("SuspiciousIndentation")
fun convertItemKeyword(jsonObject: JSONObject): ItemKeyword {

    return ItemKeyword(
        title = jsonObject.optString("title"),
        value = jsonObject.optString("value")
    )
}

fun getBookCount(context: Context, type: String, platform: String) {
    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("BOOK").child(type).child(platform)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            val dataStore = DataStoreManager(context)

            if (dataSnapshot.exists()) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (type == "NOVEL") {
                        dataStore.setDataStoreString(
                            getPlatformDataKeyNovel(platform),
                            dataSnapshot.childrenCount.toString()
                        )
                    } else {
                        dataStore.setDataStoreString(
                            getPlatformDataKeyComic(platform),
                            dataSnapshot.childrenCount.toString()
                        )
                    }
                }
            } else {
                Log.d("HIHIHI", "FAIL == NOT EXIST")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun postFCM(context: Context, fcmBody: DataFCMBody) {

    val fcm = Intent(context.applicationContext, FirebaseMessaging::class.java)
    context.startService(fcm)

    val call = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com")
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(FirebaseService::class.java)
        .postRetrofit(
            fcmBody
        )

    call.enqueue(object : Callback<FWorkManagerResult?> {
        override fun onResponse(
            call: Call<FWorkManagerResult?>,
            response: retrofit2.Response<FWorkManagerResult?>
        ) {
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    miningAlert(
                        title = fcmBody.notification?.title ?: "",
                        message = fcmBody.notification?.body ?: "",
                        path = "USER"
                    )
                }
            } else {

            }
        }

        override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {

        }
    })
}

fun postFCMAlert(context: Context, getFCM: DataFCMBodyNotification) {

    val fcmBody = DataFCMBody(
        "/topics/cs",
        "high",
        DataFCMBodyData("ALERT_ALL", ""),
        DataFCMBodyNotification(
            getFCM.title,
            getFCM.body,
            ""
        ),
    )

    postFCM(context = context, fcmBody = fcmBody)

    miningAlert(title = getFCM.title, message = getFCM.body)
}

private fun miningAlert(
    title: String,
    message: String,
    activity: String = "",
    data: String = "",
    path: String = "CS"
) {

    FirebaseDatabase.getInstance().reference.child("MESSAGE").child(path)
        .child(DBDate.dateMMDDHHMM()).setValue(
            FCMAlert(DBDate.dateMMDDHHMM(), title, message, data = data, activity = activity)
        )
}

fun checkMining(context: Context) {
    val mRootRef =
        FirebaseDatabase.getInstance().reference.child("MINING")

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            if (dataSnapshot.exists()) {

                val dataStore = DataStoreManager(context)

                val item = dataSnapshot.getValue(String::class.java)

                if (item != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStore.setDataStoreString(DataStoreManager.MINING, item)
                    }
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

fun getRandomColor(): Color {
    val list = arrayListOf(
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

    val randomIndex = (0 until list.size).random()
    return list[randomIndex]
}