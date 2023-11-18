package com.bigbigdw.manavara.retrofit

import com.bigbigdw.moavara.Retrofit.Retrofit
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.baseCallback

class RetrofitRidi {
    private val apiRidi = Retrofit.apiRidi
        fun getRidiRomance(
            map: MutableMap<String?, Any>,
            dataListener: RetrofitDataListener<String>
        ) {
        apiRidi.getRidi(queryMap = map).enqueue(baseCallback(dataListener))
    }
}
