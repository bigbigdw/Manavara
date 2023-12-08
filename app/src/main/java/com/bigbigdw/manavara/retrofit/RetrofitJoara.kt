package com.bigbigdw.manavara.retrofit

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

class RetrofitJoara {
    private val apiJoara = Retrofit.apiJoara

    fun getBookDetailJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestDetailResult>) {
        apiJoara.getBookDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getBookDetailRecom(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getBookDetailRecom(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraBookBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getJoaraBookBest(map).enqueue(baseCallback(dataListener))
    }

    fun getBookCommentJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestDetailCommentsResult>) {
        apiJoara.getBookComment(map).enqueue(baseCallback(dataListener))
    }

    fun getBookOtherJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getBookOther(map).enqueue(baseCallback(dataListener))
    }

    fun getBoardListJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBoardResult>) {
        apiJoara.getBoardListJoa(map).enqueue(baseCallback(dataListener))
    }

    fun getNoticeDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraNoticeDetailResult>) {
        apiJoara.getNoticeDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraEventDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraEventDetailResult>) {
        apiJoara.getEventDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraEvent(map: MutableMap<String?, Any>, dataListener: Any) {
        apiJoara.getJoaraEvent(map).enqueue(baseCallback(dataListener))
    }

    fun getSearchJoara(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraSearchResult>) {
        apiJoara.getSearch(map).enqueue(baseCallback(dataListener))
    }

    fun postLogin(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraLoginResult>) {
        apiJoara.postLogin(map).enqueue(baseCallback(dataListener))
    }

    fun getNoticeList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraNoticeResult>) {
        apiJoara.getNoticeList(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraEventList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraEventsResult>) {
        apiJoara.getJoaraEventList(map).enqueue(baseCallback(dataListener))
    }

}