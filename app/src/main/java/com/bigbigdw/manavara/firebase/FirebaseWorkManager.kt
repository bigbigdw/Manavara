package com.bigbigdw.manavara.firebase

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavara.best.models.ItemBookMining
import com.bigbigdw.manavara.best.setBestDetailInfo
import com.bigbigdw.manavara.room.DBBookInfo
import com.bigbigdw.manavara.room.DBMining
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.DataStoreManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val WORKER = "WORKER"
    }

    override fun doWork(): Result {

        val workerName =inputData.getString(WORKER)

        val year = DBDate.dateMMDDHHMMss().substring(0, 4)
        val month = DBDate.dateMMDDHHMMss().substring(4, 6)
        val day = DBDate.dateMMDDHHMMss().substring(6, 8)
        val hour = DBDate.dateMMDDHHMMss().substring(8, 10)
        val min = DBDate.dateMMDDHHMMss().substring(10, 12)
        val sec = DBDate.dateMMDDHHMMss().substring(12, 14)

        if(inputData.getString(WORKER)?.contains("MINING") == true){

            CoroutineScope(Dispatchers.IO).launch() {
                withContext(Dispatchers.IO) {
                    val roomDao: DBBookInfo?
                    val miningDao: DBMining?

                    roomDao = Room.databaseBuilder(
                        applicationContext,
                        DBBookInfo::class.java,
                        "NOVEL"
                    ).build()

                    miningDao = Room.databaseBuilder(
                        applicationContext,
                        DBMining::class.java,
                        "NOVEL_MINING"
                    ).allowMainThreadQueries().build()

                    val list = roomDao.bookInfoDao().getAll()

                    for(item in list){
                        runBlocking {
                            setBestDetailInfo(
                                platform = item.platform,
                                bookCode = item.bookCode,
                                context = applicationContext
                            ){

                                miningDao.miningDao().insert(
                                    ItemBookMining(
                                        bookCode = it.bookCode,
                                        platform = it.platform,
                                        cntPageRead = it.cntPageRead,
                                        cntRecom = it.cntRecom,
                                        cntTotalComment = it.cntTotalComment,
                                        cntChapter = it.cntChapter,
                                    )
                                )
                            }
                        }
                    }
                }
            }

            postFCM(
                data = "마이닝 작품 최신화",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "MINING",
            )

        }  else {

            postFCM(
                data = workerName ?: "",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "MINING",
            )
        }

        return Result.success()
    }

    private fun postFCM(data: String, time: String, activity: String = "") {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("getFCMToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            val fcmBody = DataFCMBody(
                token = token,
                priority = "high",
                data = DataFCMBodyData("마나바라 세팅", data),
                notification = DataFCMBodyNotification(
                    title = "마나바라 세팅",
                    body = "$time $data",
                    click_action = "best"
                ),
            )

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
                    response: Response<FWorkManagerResult?>
                ) {
                    if (response.isSuccessful) {
                        Log.d("FCM", "성공")
                    } else {
                        Log.d("FCM", "실패2")
                    }
                }

                override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                    Log.d("FCM", "실패");
                }
            })
        })
    }
}