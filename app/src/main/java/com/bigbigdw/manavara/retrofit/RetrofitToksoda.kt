package com.bigbigdw.manavara.retrofit

import com.bigbigdw.manavara.retrofit.result.BestBannerListResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaDetailCommentResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaSearchResult

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