package com.bigbigdw.manavara.best.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBestDetail
import com.bigbigdw.manavara.best.event.StateBestDetail
import com.bigbigdw.manavara.best.models.ItemBestComment
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.moavara.Retrofit.JoaraBestDetailCommentsResult
import com.bigbigdw.moavara.Retrofit.JoaraBestDetailResult
import com.bigbigdw.moavara.Retrofit.JoaraBestListResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class ViewModelBestDetail @Inject constructor() : ViewModel() {

    private val events = Channel<EventBestDetail>()

    val state: StateFlow<StateBestDetail> = events.receiveAsFlow()
        .runningFold(StateBestDetail(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateBestDetail())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateBestDetail, event: EventBestDetail): StateBestDetail {
        return when (event) {
            EventBestDetail.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventBestDetail.SetUserInfo -> {
                current.copy(userInfo = event.userInfo)
            }

            is EventBestDetail.SetItemBestDetailInfo -> {
                current.copy(itemBestDetailInfo = event.itemBestDetailInfo)
            }

            is EventBestDetail.SetItemBookInfo -> {
                current.copy(itemBestInfo = event.itemBestInfo)
            }

            is EventBestDetail.SetListComment -> {
                current.copy(listComment = event.listComment)
            }

            is EventBestDetail.SetListBestOther -> {
                current.copy(listBestOther = event.listBestOther)
            }

            is EventBestDetail.SetListBestInfo -> {
                current.copy(listBestInfo = event.listBestInfo)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setBestDetailInfo(platform: String, bookCode: String, context: Context) {

        Log.d("!!!!BESTDETAIL", "platform == $platform")

        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                setLayoutJoara(bookCode = bookCode, context = context, platform = platform)
            }
            "NAVER_BEST", "NAVER_CHALLENGE", "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY" -> {
                setLayoutNaver(bookCode = bookCode, platform = platform)
            }
            "NAVER_SERIES" -> {
                setLayoutNaverSeries(bookCode = bookCode, platform = platform)
            }
//            "Kakao" -> {
//                setLayoutKaKao()
//            }
//            "Kakao_Stage" -> {
//                setLayoutKaKaoStage()
//            }
//            "Ridi" -> {
//                setLayoutRidi()
//            }
//            "OneStore" -> {
//                setLayoutOneStory()
//            }
//            "Munpia" -> {
//                setLayoutMunpia()
//            }
//            "Toksoda" -> {
//                setLayoutToksoda()
//            }
        }

//        when (platform) {
//            "Naver_Today", "Naver_Challenge", "Naver" -> {
//                binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
//                binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
//            }
//            "Kakao", "Kakao_Stage", "OneStore", "Munpia" -> {
//                binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
//                binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
//            }
//            "Ridi" -> {
//                binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
//                binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
//            }
//            else -> {
//                binding.tabs.addTab(binding.tabs.newTab().setText("작품 분석"))
//                binding.tabs.addTab(binding.tabs.newTab().setText("댓글"))
//                binding.tabs.addTab(binding.tabs.newTab().setText("다른 작품"))
//            }
//        }
    }

    private fun setLayoutJoara(bookCode: String, context: Context, platform: String) {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(context)
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "1"

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    val itemBestDetailInfo: ItemBestDetailInfo

                    if (data.status == "1" && data.book != null) {

                        itemBestDetailInfo = ItemBestDetailInfo(
                            writer = data.book.writerName,
                            title = data.book.subject,
                            bookImg = data.book.bookImg.replace("http://", "https://"),
                            bookCode = bookCode,
                            intro = data.book.intro,
                            cntChapter = "총 ${data.book.cntChapter}화",
                            cntPageRead = data.book.cntPageRead,
                            cntFavorite = data.book.cntFavorite,
                            cntRecom = data.book.cntRecom,
                            cntTotalComment = data.book.cntTotalComment,
                            genre = data.book.category_ko_name,
                            tabInfo = arrayListOf(
                                "작품 댓글",
                                "작가의 다른 작품",
                                "평점 분석",
                                "선호작 분석",
                                "조회 분석",
                                "랭킹 분석",
                                "최근 분석",
                            ),
                            keyword = data.book.keyword,
                            platform = platform
                        )

                        viewModelScope.launch {
                            events.send(
                                EventBestDetail.SetItemBestDetailInfo(itemBestDetailInfo = itemBestDetailInfo)
                            )
                        }
                    }
                }
            })
    }

    private fun setLayoutNaverSeries(bookCode: String, platform: String) {
        Thread {

            val doc: Document =
                Jsoup.connect("https://series.naver.com/novel/detail.series?productNo=${bookCode}").get()

            val itemBestDetailInfo = ItemBestDetailInfo(
                writer = doc.select(".info_lst li")[2].text(),
                title = doc.select(".aside").select(".pic_area").select("img").attr("alt"),
                bookImg = doc.select(".aside").select(".pic_area").select("img").attr("src"),
                bookCode = bookCode,
                platform = platform,
                genre = doc.select(".info_lst li")[1].text(),
                intro = doc.select("div._synopsis")[1].text().replace("접기", ""),
                cntRecom = doc.select("div.score_area em").text(),
                tabInfo = arrayListOf(
                    "작가의 다른 작품",
                    "랭킹 분석",
                    "최근 분석",
                ),
            )

            viewModelScope.launch {
                events.send(
                    EventBestDetail.SetItemBestDetailInfo(itemBestDetailInfo = itemBestDetailInfo),
                )
            }

        }.start()
    }

    private fun setLayoutNaver(bookCode: String, platform: String) {
        Thread {

            Log.d("!!!!BESTDETAIL", "https://novel.naver.com/webnovel/list?novelId=${bookCode}")

            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

            val itemBestDetailInfo = ItemBestDetailInfo(
                writer = doc.select(".writer").text(),
                title = doc.select(".book_title").text(),
                bookImg = doc.select(".section_area_info .pic img").attr("src"),
                bookCode = bookCode,
                intro = doc.select(".section_area_info .dsc").text(),
                cntFavorite = doc.select(".info_book .like").text(),
                cntRecom = doc.select(".grade_area em").text(),
                genre = doc.select(".info_book .genre").text(),
                tabInfo = arrayListOf(
                    "작품 댓글",
                    "작가의 다른 작품",
                    "평점 분석",
                    "선호작 분석",
                    "조회 분석",
                    "랭킹 분석",
                    "최근 분석",
                ),
                platform = platform
            )

            viewModelScope.launch {
                events.send(
                    EventBestDetail.SetItemBestDetailInfo(itemBestDetailInfo = itemBestDetailInfo)
                )
            }

        }.start()
    }

    fun setManavaraBestInfo(bookCode: String, type: String, platform: String) {
        val mRootRef =
            FirebaseDatabase.getInstance().reference.child("BOOK").child(type).child(platform)
                .child(bookCode)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    val item = dataSnapshot.getValue(ItemBookInfo::class.java)

                    if (item != null) {
                        viewModelScope.launch {
                            events.send(
                                EventBestDetail.SetItemBookInfo(itemBestInfo = item)
                            )
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setBestDetailAnalyze(bookCode: String, type: String, platform: String) {
        val mRootRef =
            FirebaseDatabase.getInstance().reference.child("DATA").child(type).child(platform)
                .child(bookCode)

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    val items = ArrayList<ItemBestInfo>()

                    for(item in dataSnapshot.children) {
                        val bestInfo = item.getValue(ItemBestInfo::class.java)

                        if (bestInfo != null) {

                            if(bestInfo.date.isEmpty()){
                                bestInfo.date = item.key.toString()
                            }

                            items.add(bestInfo)
                        }
                    }

                    viewModelScope.launch {
                        events.send(
                            EventBestDetail.SetListBestInfo(listBestInfo = items)
                        )
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun gotoUrl(platform: String, bookCode: String, context: Context) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(bookCode = bookCode, platform = platform)))
        context.startActivity(intent)
    }

    private fun getUrl(platform: String, bookCode: String): String {

        return when (platform) {
            "MrBlue" -> {
                "https://www.mrblue.com/novel/${bookCode}"
            }

            "Naver_Today", "Naver_Challenge", "Naver" -> {
                "https://novel.naver.com/webnovel/list?novelId=${bookCode}"
            }

            "NAVER_SERIES" -> {
                "https://series.naver.com/novel/detail.series?productNo=${bookCode}"
            }

            "Ridi" -> {
                "https://ridibooks.com/books/${bookCode}"
            }

            "Kakao_Stage" -> {
                "https://pagestage.kakao.com/novels/${bookCode}"
            }

            "Kakao" -> {
                "https://page.kakao.com/home?seriesId=${bookCode}"
            }

            "OneStore" -> {
                "https://onestory.co.kr/detail/${bookCode}"
            }

            "JOARA", "JOARA_PREMIUM", "JOARA_NOBLESS" -> {
                "https://www.joara.com/book/${bookCode}"
            }

            "Munpia" -> {
                "https://novel.munpia.com/${bookCode}"
            }

            "Toksoda" -> {
                "https://www.tocsoda.co.kr/product/productView?brcd=${bookCode}"
            }

            else -> ""
        }
    }

    fun setComment(context: Context, bookCode: String, platform: String) {
        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                getCommentsJoara(context = context, bookCode = bookCode)
            }
//            "Kakao" -> {
//                getCommentsKakao()
//            }
//            "Kakao_Stage" -> {
//                getCommentsKakaoStage()
//            }
//            "OneStore" -> {
//                getCommentsOneStory()
//            }
//            "Munpia" -> {
//                getCommentsMunpia()
//            }
//            "Toksoda" -> {
//                getCommentsToksoda()
//            }
        }

    }

    private fun getCommentsJoara(context: Context, bookCode: String) {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["book_code"] = bookCode
        param["category"] = "1"
        param["page"] = "1"
        param["orderby"] = "redate"
        param["offset"] = "100"

        apiJoara.getBookCommentJoa(
            param,
            object : RetrofitDataListener<JoaraBestDetailCommentsResult> {
                override fun onSuccess(data: JoaraBestDetailCommentsResult) {
                    if (data.status == "1" && data.comments != null) {

                        val items = ArrayList<ItemBestComment>()

                        for (i in data.comments.indices) {
                            items.add(
                                ItemBestComment(
                                    comment = data.comments[i].comment,
                                    date = data.comments[i].created,
                                )
                            )
                        }

                        viewModelScope.launch {
                            events.send(
                                EventBestDetail.SetListComment(listComment = items)
                            )
                        }
                    }
                }
            })
    }

    fun setOtherBooks(context: Context, bookCode: String, platform: String) {
        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                getOthersJoa(context = context, bookCode = bookCode, platform = platform)
            }
            "NAVER_SERIES" -> {
                getOtherNaverSeries(bookCode = bookCode, platform = platform)
            }
//            "Kakao" -> {
//                getCommentsKakao()
//            }
//            "Kakao_Stage" -> {
//                getCommentsKakaoStage()
//            }
//            "OneStore" -> {
//                getCommentsOneStory()
//            }
//            "Munpia" -> {
//                getCommentsMunpia()
//            }
//            "Toksoda" -> {
//                getCommentsToksoda()
//            }
        }

    }

    private fun getOthersJoa(context: Context, bookCode: String, platform: String) {
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["book_code"] = bookCode
        param["category"] = "1"
        param["orderby"] = "redate"
        param["offset"] = "25"

        apiJoara.getBookOtherJoa(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    if (data.bookLists != null) {

                        val items = ArrayList<ItemBookInfo>()

                        for (i in data.bookLists.indices) {
                            items.add(
                                ItemBookInfo(
                                    writer = data.bookLists[i].writerName,
                                    title = data.bookLists[i].subject,
                                    bookImg = data.bookLists[i].bookImg.replace(
                                        "http://",
                                        "https://"
                                    ),
                                    bookCode = data.bookLists[i].bookCode,
                                    intro = data.bookLists[i].intro,
                                    cntChapter = "총 ${data.bookLists[i].cntChapter}화",
                                    cntPageRead = data.bookLists[i].cntPageRead,
                                    cntFavorite = data.bookLists[i].cntFavorite,
                                    cntRecom = data.bookLists[i].cntRecom,
                                    cntTotalComment = data.bookLists[i].cntTotalComment,
                                    genre = data.bookLists[i].category_ko_name,
                                    type = platform
                                )
                            )
                        }

                        viewModelScope.launch {
                            events.send(
                                EventBestDetail.SetListBestOther(listBestOther = items)
                            )
                        }
                    }
                }
            })
    }

    private fun getOtherNaverSeries(bookCode: String, platform: String) {
        Thread {

            val doc: Document =
                Jsoup.connect("https://series.naver.com/novel/detail.series?productNo=${bookCode}").get()

            val items = ArrayList<ItemBookInfo>()
            val books = doc.select("div.flick_view_area  ul.lst_thum li.book")

            for (i in books.indices) {

                Log.d("!!!!BESTDETAIL", "BOOK = ${books[i]}")
                Log.d("!!!!BESTDETAIL", "title = ${books[i].select("a").attr("title")}")
                Log.d("!!!!BESTDETAIL", "bookImg = ${books[i].select("img").attr("data-original")}")
                Log.d("!!!!BESTDETAIL", "bookCode = ${books[i].select("a").attr("href").replace("/novel/detail.series?productNo=", "")}")

                items.add(
                    ItemBookInfo(
                        writer = doc.select(".end_tit strong").first()?.nextElementSibling()?.text() ?: "",
                        title = books[i].select("a").attr("title"),
                        bookImg = books[i].select("img").attr("data-original"),
                        bookCode = books[i].select("a").attr("href").replace("/novel/detail.series?productNo=", ""),
                        type = platform
                    )
                )
            }

            viewModelScope.launch {
                events.send(
                    EventBestDetail.SetListBestOther(listBestOther = items)
                )
            }

        }.start()
    }
}