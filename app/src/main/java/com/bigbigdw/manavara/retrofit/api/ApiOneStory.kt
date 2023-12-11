package com.bigbigdw.manavara.retrofit.api

import com.bigbigdw.manavara.retrofit.result.OneStoreBookResult
import com.bigbigdw.manavara.retrofit.result.OnestoreBookDetail
import com.bigbigdw.manavara.retrofit.result.OnestoreBookDetailComment
import retrofit2.Call
import retrofit2.http.*

interface ApiOneStory {

    @GET("api/display/product/RNK050700001")
    fun getBestOneStore(@QueryMap queryMap: MutableMap<String?, Any>): Call<OneStoreBookResult>

    @GET("api/display/product/RNK050800001")
    fun getBestOneStorePass(@QueryMap queryMap: MutableMap<String?, Any>): Call<OneStoreBookResult>

    @GET("api/detail/{bookcode}")
    fun getOneStoryBookDetail(@Path("bookcode") id: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<OnestoreBookDetail>

    @GET("/api/comment/{bookcode}")
    fun getOneStoryBookDetailComment(@Path("bookcode") id: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<OnestoreBookDetailComment>
}