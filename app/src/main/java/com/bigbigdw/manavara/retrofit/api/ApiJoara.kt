package com.bigbigdw.manavara.retrofit.api

import com.bigbigdw.manavara.retrofit.result.JoaraBestDetailCommentsResult
import com.bigbigdw.manavara.retrofit.result.JoaraBestDetailResult
import com.bigbigdw.manavara.retrofit.result.JoaraBestListResult
import com.bigbigdw.manavara.retrofit.result.JoaraBoardResult
import com.bigbigdw.manavara.retrofit.result.JoaraEventDetailResult
import com.bigbigdw.manavara.retrofit.result.JoaraEventResult
import com.bigbigdw.manavara.retrofit.result.JoaraEventsResult
import com.bigbigdw.manavara.retrofit.result.JoaraLoginResult
import com.bigbigdw.manavara.retrofit.result.JoaraNoticeDetailResult
import com.bigbigdw.manavara.retrofit.result.JoaraNoticeResult
import com.bigbigdw.manavara.retrofit.result.JoaraSearchResult
import retrofit2.Call
import retrofit2.http.*


interface ApiJoara {

    @GET("v1/search/query_bc_v2.joa")
    fun getSearch(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraSearchResult>

    @GET("v1/banner/home_banner.joa")
    fun getJoaraEvent(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraEventResult>

    @GET("v1/board/event_detail.joa")
    fun getEventDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraEventDetailResult>

    @GET("v1/board/notice_detail.joa")
    fun getNoticeDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraNoticeDetailResult>

    @GET("v1/best/book.joa")
    fun getJoaraBookBest(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

    @GET("v1/book/detail.joa")
    fun getBookDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestDetailResult>

    @GET("v1/book/recommend_list_api.joa")
    fun getBookDetailRecom(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

    @GET("v1/board/book_comment.joa")
    fun getBookComment(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestDetailCommentsResult>

    @GET("v1/book/other")
    fun getBookOther(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

    @GET("v1/board/board_list.joa")
    fun getBoardListJoa(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBoardResult>

    @FormUrlEncoded
    @POST("v1/user/auth.joa")
    fun postLogin(@FieldMap queryMap: MutableMap<String?, Any>): Call<JoaraLoginResult>

    @GET("v1/board/notice_list.joa")
    fun getNoticeList(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraNoticeResult>

    @GET("v1/board/event.joa")
    fun getJoaraEventList(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraEventsResult>
}