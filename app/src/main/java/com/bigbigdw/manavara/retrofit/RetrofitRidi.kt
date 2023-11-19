package com.bigbigdw.manavara.retrofit

class RetrofitRidi {
    private val apiRidi = Retrofit.apiRidi
        fun getRidiRomance(
            map: MutableMap<String?, Any>,
            dataListener: RetrofitDataListener<String>
        ) {
        apiRidi.getRidi(queryMap = map).enqueue(baseCallback(dataListener))
    }
}
