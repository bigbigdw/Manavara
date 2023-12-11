package com.bigbigdw.manavara.best.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBestDetail
import com.bigbigdw.manavara.best.event.StateBestDetail
import com.bigbigdw.manavara.best.models.ItemBestComment
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.manavara.retrofit.result.BestToksodaSearchResult
import com.bigbigdw.manavara.retrofit.result.JoaraBestDetailCommentsResult
import com.bigbigdw.manavara.retrofit.result.JoaraBestListResult
import com.bigbigdw.manavara.retrofit.result.KakaoStageBestBookCommentResult
import com.bigbigdw.manavara.retrofit.result.OnestoreBookDetailComment
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.RetrofitKaKao
import com.bigbigdw.manavara.retrofit.RetrofitOnestory
import com.bigbigdw.manavara.retrofit.RetrofitToksoda
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

    fun setBestDetailInfo(itemBestDetailInfo: ItemBestDetailInfo) {
        viewModelScope.launch {
            events.send(
                EventBestDetail.SetItemBestDetailInfo(itemBestDetailInfo = itemBestDetailInfo)
            )
        }
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

                    for (item in dataSnapshot.children) {
                        val bestInfo = item.getValue(ItemBestInfo::class.java)

                        if (bestInfo != null) {

                            if (bestInfo.date.isEmpty()) {
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

    fun setComment(context: Context, bookCode: String, platform: String) {
        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                getCommentsJoara(context = context, bookCode = bookCode)
            }

            "KAKAO_STAGE" -> {
                getCommentsKakaoStage(bookCode = bookCode)
            }

            "ONESTORY_FANTAGY", "ONESTORY_ROMANCE", "ONESTORY_PASS_FANTAGY", "ONESTORY_PASS_ROMANCE" -> {
                getCommentsOneStory(bookCode = bookCode)
            }

            "MUNPIA_PAY", "MUNPIA_FREE" -> {
                getCommentsMunpia(bookCode = bookCode)
            }
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
                    if (data.comments != null) {

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

    private fun getCommentsKakaoStage(bookCode: String) {
        val apiKakaoStage = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["size"] = 100
        param["sort"] = "cacheField.likeCount,desc"
        param["sort"] = "id,desc"
        param["page"] = 0

        apiKakaoStage.getBestKakaoStageDetailComment(
            bookCode,
            "20",
            "cacheField.likeCount,desc",
            "id,desc",
            "0",
            object : RetrofitDataListener<KakaoStageBestBookCommentResult> {
                override fun onSuccess(data: KakaoStageBestBookCommentResult) {

                    val items = ArrayList<ItemBestComment>()

                    for (i in data.content.indices) {
                        items.add(
                            ItemBestComment(
                                comment = data.content[i].message,
                                date = data.content[i].createdAt,
                            )
                        )
                    }

                    viewModelScope.launch {
                        events.send(
                            EventBestDetail.SetListComment(listComment = items)
                        )
                    }
                }
            })
    }

    private fun getCommentsOneStory(bookCode: String) {

        val apiOnestory = RetrofitOnestory()
        val param: MutableMap<String?, Any> = HashMap()

        param["channelId"] = bookCode
        param["offset"] = "1"
        param["orderBy"] = "recommend"

        apiOnestory.getOneStoryBookDetailComment(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetailComment> {
                override fun onSuccess(data: OnestoreBookDetailComment) {

                    val items = ArrayList<ItemBestComment>()

                    for (i in data.params.commentList.indices) {
                        items.add(
                            ItemBestComment(
                                comment = data.params.commentList[i].commentDscr,
                                date = data.params.commentList[i].regDate,
                            )
                        )
                    }

                    viewModelScope.launch {
                        events.send(
                            EventBestDetail.SetListComment(listComment = items)
                        )
                    }
                }
            })
    }

    private fun getCommentsMunpia(bookCode: String) {
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

            val it = doc.select(".review .article-inner")
            val items = ArrayList<ItemBestComment>()

            for (i in it.indices) {
                items.add(
                    ItemBestComment(
                        comment =  doc.select(".review .article-inner").get(i).text(),
                        date = doc.select(".review .writer-area .date-de").get(i).text().replace("· ", ""),
                    )
                )
            }

            viewModelScope.launch {
                events.send(
                    EventBestDetail.SetListComment(listComment = items)
                )
            }
        }.start()
    }

    fun setOtherBooks(
        context: Context,
        bookCode: String,
        platform: String,
        writerLink: String = ""
    ) {
        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
                getOthersJoa(context = context, bookCode = bookCode, platform = platform)
            }
            "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
                getOtherNaverWebnovel(bookCode = bookCode, platform = platform, type = "OTHER")
            }
            "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN" -> {
                getOtherRidi(bookCode = writerLink, platform = platform)
            }
            "TOKSODA", "TOKSODA_FREE" -> {
                getOthersToksoda(writerLink = writerLink, platform = platform)
            }
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
            val books = if (type == "OTHER") {
                doc.select(".aside_section").first()?.select("li")
            } else {
                doc.select(".aside_section").first()?.nextElementSibling()?.select("li")
            }

            if (books != null) {
                for (i in books.indices) {

                    if (!books[i].select("a").attr("href").contains("#")) {
                        items.add(
                            ItemBookInfo(
                                writer = books[i].select(".author").text(),
                                title = books[i].select("img").attr("alt"),
                                bookImg = books[i].select("img").attr("src"),
                                bookCode = books[i].select("a").attr("href")
                                    .replace("/best/list?novelId=", "")
                                    .replace("/challenge/list?novelId=", "")
                                    .replace("/webnovel/list?novelId=", ""),
                                cntRecom = books[i].select(".list_info").select(".score_area")
                                    .text(),
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

                val bookImg = if (books[i].select(".thumbnail").attr("data-src")
                        .contains("cover_adult.png")
                ) {
                    books[i].select(".thumbnail").attr("data-src")
                } else {
                    books[i].select(".thumbnail").attr("data-src").replace("//", "https://")
                }

                if (!books[i].select("a").attr("href").contains("#")) {
                    items.add(
                        ItemBookInfo(
                            writer = books[i].select(".book_metadata_wrapper").select(".author")
                                .text(),
                            title = books[i].select(".book_metadata_wrapper").select(".title_text")
                                .text(),
                            bookImg = bookImg,
                            bookCode = books[i].select(".book_metadata_wrapper")
                                .attr("data-book-id"),
                            cntPageRead = books[i].select(".book_metadata_wrapper")
                                .select(".StarRate_ParticipantCount").text(),
                            cntRecom = books[i].select(".book_metadata_wrapper")
                                .select(".StarRate_Score").text(),
                            cntFavorite = "",
                            genre = books[i].select(".book_metadata_wrapper").select(".genre")
                                .text(),
                            cntChapter = books[i].select(".book_metadata_wrapper")
                                .select(".count_num").text(),
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

    private fun getOthersToksoda(writerLink: String, platform: String) {

        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()

        param["srchwrd"] = writerLink
        param["keyword"] = writerLink
        param["pageSize"] = "20"
        param["pageIndex"] = "0"
        param["ageGrade"] = "0"
        param["lgctgrCd"] = ""
        param["mdctgrCd"] = ""
        param["searchType"] = "T"
        param["sortType"] = "W"
        param["prdtType"] = ""
        param["eventYn"] = "N"
        param["realSearchType"] = "N"
        param["_"] = "1657267049443"

        apiToksoda.getSearch(
            param,
            object : RetrofitDataListener<BestToksodaSearchResult> {
                override fun onSuccess(data: BestToksodaSearchResult) {

                    val items = ArrayList<ItemBookInfo>()

                    if(data.resultList != null){
                        for (i in data.resultList.indices) {
                            items.add(
                                ItemBookInfo(
                                    writer = data.resultList[i].AUTHOR,
                                    title = data.resultList[i].BOOK_NM,
                                    bookImg = "https:${data.resultList[i].IMG_PATH}",
                                    bookCode = data.resultList[i].BARCODE,
                                    intro = data.resultList[i].INTRO,
                                    genre = data.resultList[i].LGCTGR_NM,
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

                    if (data.bookLists != null) {
                        for (i in data.bookLists.indices) {
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
                Jsoup.connect("https://series.naver.com/novel/detail.series?productNo=${bookCode}")
                    .get()

            val items = ArrayList<ItemBookInfo>()
            val books = doc.select("div.aside_air ul.aside_air_lst li")

            for (i in books.indices) {

                items.add(
                    ItemBookInfo(
                        writer = doc.select(".info_area").select(".info_group span")[1].text(),
                        title = books[i].select("img").attr("alt"),
                        bookImg = books[i].select("img").attr("src"),
                        bookCode = books[i].select("a").attr("href")
                            .replace("/best/list?novelId=", ""),
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