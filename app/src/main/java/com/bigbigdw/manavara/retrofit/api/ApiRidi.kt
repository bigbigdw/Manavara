package com.bigbigdw.manavara.retrofit.api

import retrofit2.Call
import retrofit2.http.*

interface ApiRidi {
    @Headers("Accept-Encoding: identity")
    @GET("v2/category/books")
    fun getRidi( @QueryMap queryMap: MutableMap<String?, Any>): Call<String>
}