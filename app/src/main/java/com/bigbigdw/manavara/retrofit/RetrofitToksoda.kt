package com.bigbigdw.manavara.retrofit

import com.bigbigdw.moavara.Retrofit.BestBannerListResult
import com.bigbigdw.moavara.Retrofit.BestToksodaDetailCommentResult
import com.bigbigdw.moavara.Retrofit.BestToksodaDetailResult
import com.bigbigdw.moavara.Retrofit.BestToksodaResult
import com.bigbigdw.moavara.Retrofit.BestToksodaSearchResult

class RetrofitToksoda {
    private val apiToksoda = Retrofit.apiToksoda

    fun getBestList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaResult>) {
        apiToksoda.getBestList(map).enqueue(baseCallback(dataListener))
    }

    fun getBestDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaDetailResult>) {
        apiToksoda.getBestDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getBestDetailComment(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaDetailCommentResult>) {
        apiToksoda.getBestDetailComment(map).enqueue(baseCallback(dataListener))
    }

    fun getSearch(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaSearchResult>) {
        apiToksoda.getSearch(map).enqueue(baseCallback(dataListener))
    }

    fun getEventList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestBannerListResult>) {
        apiToksoda.getEventList(map).enqueue(baseCallback(dataListener))
    }

}