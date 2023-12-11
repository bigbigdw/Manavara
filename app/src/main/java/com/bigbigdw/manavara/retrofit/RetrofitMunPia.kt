package com.bigbigdw.manavara.retrofit

import com.bigbigdw.manavara.retrofit.result.BestMunpiaResult

class RetrofitMunPia {
    private val apiMoonPia = Retrofit.apiMoonPia

    fun postMunPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMunpiaResult>) {
        apiMoonPia.postMunPiaBest(map).enqueue(baseCallback(dataListener))
    }

}