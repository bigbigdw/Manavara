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
            "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
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
            "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN", -> {
                setLayoutRidi(bookCode = bookCode, platform = platform)
            }
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
                                "비슷한 작품",
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
                    "평점 분석",
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

            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

            val keywordList = arrayListOf<String>()

            for(i in doc.select(".tag_collection").indices){
                keywordList.add(doc.select(".tag_collection")[i].text())
            }

            val itemBestDetailInfo = ItemBestDetailInfo(
                writer = doc.select(".info_area").select(".info_group span")[1].text(),
                title = doc.select(".section_area_info").select(".info_top").select(".title").text(),
                bookImg = doc.select(".section_area_info").select("img").attr("src"),
                bookCode = bookCode,
                intro = doc.select(".info_area").select(".info_bottom").select("span").text(),
                cntFavorite = doc.select(".info_book .like").text(),
                cntRecom = doc.select(".info_area").select(".info_group").select(".score_area").first()?.text()?.replace("별점", "") ?: "",
                cntPageRead = doc.select(".info_area").select(".info_group").select(".score_area").first()?.nextElementSibling()?.text()?.replace("다운로드 ", "") ?: "",
                genre = doc.select(".info_area").select(".info_group span")[0].text(),
                cntChapter = doc.select(".cont_sub").select(".component_head").select(".past_number").text().replace("(","").replace(")",""),
                keyword = keywordList,
                tabInfo = arrayListOf(
                    "작가의 다른 작품",
                    "비슷한 작품",
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

    private fun setLayoutRidi(bookCode: String, platform: String) {
        Thread {

            val doc: Document =
                Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

            val keywordList = arrayListOf<String>()

            for(i in doc.select(".keyword_list li").indices){
                keywordList.add(doc.select(".keyword_list li")[i].select(".keyword").text())
            }

            val bookImg = if(doc.select(".header_thumbnail_wrap").select(".thumbnail").attr("src").contains("cover_adult.png")){
                doc.select(".header_thumbnail_wrap").select(".thumbnail").attr("src")
            } else {
                doc.select(".header_thumbnail_wrap").select(".thumbnail").attr("src").replace("//","https://")
            }

            val itemBestDetailInfo = ItemBestDetailInfo(
                writer = doc.select(".header_info_wrap").select(".js_author_detail_link").text(),
                writerLink = doc.select(".metadata_writer").select("a").attr("href").replace("/author","https://ridibooks.com/author"),
                title = doc.select(".header_info_wrap").select("h1.info_title_wrap").text(),
                bookImg = bookImg,
                bookCode = bookCode,
                intro = doc.select(".detail_introduce_book").select(".introduce_paragraph").text(),
                cntFavorite = doc.select(".header_thumbnail_wrap").select(".js_preference_count").text(),
                cntRecom = doc.select(".header_info_wrap").select(".StarRate_Score").text(),
                cntPageRead = doc.select(".header_info_wrap").select(".StarRate_ParticipantCount").text(),
                genre = doc.select(".header_info_wrap").select("a").first()?.text() ?: "",
                cntChapter = doc.select(".header_info_wrap").select(".book_count").text(),
                keyword = keywordList,
                tabInfo = arrayListOf(
                    "작가의 다른 작품",
                    "평점 분석",
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

            "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
                "https://novel.naver.com/webnovel/list?novelId=${bookCode}"
            }

            "NAVER_SERIES" -> {
                "https://series.naver.com/novel/detail.series?productNo=${bookCode}"
            }

            "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN" -> {
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

    fun setOtherBooks(context: Context, bookCode: String, platform: String, writerLink : String = "") {
        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                getOthersJoa(context = context, bookCode = bookCode, platform = platform)
            }
            "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
                getOtherNaverWebnovel(bookCode = bookCode, platform = platform, type = "OTHER")
            }
            "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN", -> {
                getOtherRidi(bookCode = writerLink, platform = platform)
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

    private fun getOtherNaverWebnovel(bookCode: String, platform: String, type: String) {
        Thread {

            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

            val items = ArrayList<ItemBookInfo>()
            val books = if(type == "OTHER"){
                doc.select(".aside_section").first()?.select("li")
            } else {
                doc.select(".aside_section").first()?.nextElementSibling()?.select("li")
            }

            if(books != null){
                for (i in books.indices) {

                    if(!books[i].select("a").attr("href").contains("#")){
                        items.add(
                            ItemBookInfo(
                                writer = books[i].select(".author").text(),
                                title = books[i].select("img").attr("alt"),
                                bookImg = books[i].select("img").attr("src"),
                                bookCode = books[i].select("a").attr("href")
                                    .replace("/best/list?novelId=", "")
                                    .replace("/challenge/list?novelId=", "")
                                    .replace("/webnovel/list?novelId=", ""),
                                cntRecom = books[i].select(".list_info").select(".score_area").text(),
                                cntFavorite = books[i].select(".list_info").select(".count").text(),
                                type = platform
                            )
                        )
                    }
                }
            }

            viewModelScope.launch {
                events.send(
                    EventBestDetail.SetListBestOther(listBestOther = items)
                )
            }

        }.start()
    }

    private fun getOtherRidi(bookCode: String, platform: String) {
        Thread {

            val doc: Document = Jsoup.connect(bookCode).post()

            val items = ArrayList<ItemBookInfo>()
            val books = doc.select(".author_books_wrapper").select(".book_macro_landscape ")

            for (i in books.indices) {

                val bookImg = if(books[i].select(".thumbnail").attr("data-src").contains("cover_adult.png")){
                    books[i].select(".thumbnail").attr("data-src")
                } else {
                    books[i].select(".thumbnail").attr("data-src").replace("//","https://")
                }

                if(!books[i].select("a").attr("href").contains("#")){
                    items.add(
                        ItemBookInfo(
                            writer = books[i].select(".book_metadata_wrapper").select(".author").text(),
                            title =  books[i].select(".book_metadata_wrapper").select(".title_text").text(),
                            bookImg = bookImg,
                            bookCode = books[i].select(".book_metadata_wrapper").attr("data-book-id"),
                            cntPageRead = books[i].select(".book_metadata_wrapper").select(".StarRate_ParticipantCount").text(),
                            cntRecom = books[i].select(".book_metadata_wrapper").select(".StarRate_Score").text(),
                            cntFavorite = "",
                            genre = books[i].select(".book_metadata_wrapper").select(".genre").text(),
                            cntChapter = books[i].select(".book_metadata_wrapper").select(".count_num").text(),
                            type = platform
                        )
                    )
                }
            }

            viewModelScope.launch {
                events.send(
                    EventBestDetail.SetListBestOther(listBestOther = items)
                )
            }

        }.start()
    }

    fun setBestDetailRecom(platform: String, bookCode: String, context: Context) {

        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                setJoaraRecom(bookCode = bookCode, context = context, platform = platform)
            }
            "NAVER_SERIES" -> {
                setNaverSeriesRecom(bookCode = bookCode, platform = platform)
            }
            "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
                getOtherNaverWebnovel(bookCode = bookCode, platform = platform, type = "RECOM")
            }
        }
    }

    private fun setJoaraRecom(bookCode: String, context: Context, platform: String) {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(context)
        JoaraRef["book_code"] = bookCode
        JoaraRef["recommend_type"] = "book_code"
        JoaraRef["model_type"] = "all"

        apiJoara.getBookDetailRecom(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val itemList = ArrayList<ItemBookInfo>()

                    if(data.bookLists != null){
                        for(i in data.bookLists.indices){
                            itemList.add(
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
                    }

                    viewModelScope.launch {
                        events.send(
                            EventBestDetail.SetListBestOther(listBestOther = itemList)
                        )
                    }
                }
            })
    }

    private fun setNaverSeriesRecom(bookCode: String, platform: String) {
        Thread {

            val doc: Document =
                Jsoup.connect("https://series.naver.com/novel/detail.series?productNo=${bookCode}").get()

            val items = ArrayList<ItemBookInfo>()
            val books = doc.select("div.aside_air ul.aside_air_lst li")

            for (i in books.indices) {

                items.add(
                    ItemBookInfo(
                        writer = doc.select(".info_area").select(".info_group span")[1].text(),
                        title = books[i].select("img").attr("alt"),
                        bookImg = books[i].select("img").attr("src"),
                        bookCode = books[i].select("a").attr("href").replace("/best/list?novelId=", ""),
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

//val itemBestDetailInfo = ItemBestDetailInfo(
//    writer = "",
//    title = "",
//    bookImg = "",
//    bookCode = bookCode,
//    intro = "",
//    cntFavorite = "",
//    cntRecom = "",
//    cntPageRead = "",
//    genre = "",
//    cntChapter = "",
//    keyword = keywordList,
//    tabInfo = arrayListOf(),
//    platform = platform
//)

//ItemBookInfo(
//writer = "",
//title = "",
//bookImg = "",
//bookCode = "",
//cntRecom = "",
//cntFavorite = "",
//type = platform
//)