package com.bigbigdw.manavara.retrofit

import com.bigbigdw.moavara.Retrofit.BestMoonpiaResult

class RetrofitMoonPia {
    private val apiMoonPia = Retrofit.apiMoonPia

    fun postMoonPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMoonpiaResult>) {
        apiMoonPia.postMoonPiaBest(map).enqueue(baseCallback(dataListener))
    }

}