package com.bigbigdw.manavara.firebase

import retrofit2.Call
import retrofit2.http.*

interface FirebaseService {
    @Headers("Content-Type: application/json", "Authorization: key=AAAAEMXK_wY:APA91bEzc06yCwN6L7odP9DCxTSmgzpbPzydvw4FeAzzRzSijtkeT5dNZj3TRZCmfe3UTZZFV_5VmRTIGyz5FKiPhJ44mbSrV2VegvnA6P_QPDpP2jwVUwrLC5ae-Jb-4_S7z3WjPaEK")
    @POST("/fcm/send")
    fun postRetrofit(
        @Body body : DataFCMBody
    ): Call<FWorkManagerResult>
}

