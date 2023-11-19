package com.bigbigdw.manavara.retrofit.api

import com.bigbigdw.manavara.retrofit.result.BestBannerListResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaDetailCommentResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaResult
import com.bigbigdw.manavara.retrofit.result.BestToksodaSearchResult
import com.bigbigdw.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*

interface ApiToksoda {

    @GET("getMainChargeProductList")
    fun getBestList(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaResult>

    @GET("product/selectProductDetail")
    fun getBestDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaDetailResult>

    @GET("/product/selectEpisodeCommentList")
    fun getBestDetailComment(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaDetailCommentResult>

    @GET("common/search/smartmakerLCPOpen")
    fun getSearch(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaSearchResult>

    @GET("/banner/getBannerList")
    fun getEventList(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestBannerListResult>

    @GET("/common/search/smartmakerLCPOpen")
    fun getEventDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestBannerListResult>
}