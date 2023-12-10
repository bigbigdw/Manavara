package com.bigbigdw.manavara.best

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBestDetail
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.RetrofitKaKao
import com.bigbigdw.manavara.retrofit.RetrofitOnestore
import com.bigbigdw.manavara.retrofit.RetrofitToksoda
import com.bigbigdw.manavara.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavara.retrofit.result.JoaraBestDetailResult
import com.bigbigdw.manavara.retrofit.result.KakaoStageBestBookResult
import com.bigbigdw.manavara.retrofit.result.OnestoreBookDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun setBestDetailInfo(platform: String, bookCode: String, context: Context, callbacks : (ItemBestDetailInfo) -> Unit) {

    when (platform) {
        "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
            setLayoutJoara(bookCode = bookCode, context = context, platform = platform, callbacks = callbacks)
        }

        "NAVER_WEBNOVEL_FREE", "NAVER_WEBNOVEL_PAY", "NAVER_BEST", "NAVER_CHALLENGE" -> {
            setLayoutNaver(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }

        "NAVER_SERIES" -> {
            setLayoutNaverSeries(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }

        "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN" -> {
            setLayoutRidi(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }

        "KAKAO_STAGE" -> {
            setLayoutKakaoStage(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }

        "ONESTORY_FANTAGY", "ONESTORY_ROMANCE", "ONESTORY_PASS_FANTAGY", "ONESTORY_PASS_ROMANCE" -> {
            setLayoutOneStory(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }

        "MUNPIA_PAY", "MUNPIA_FREE" -> {
            setLayoutMunpia(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }

        "TOKSODA", "TOKSODA_FREE", -> {
            setLayoutToksoda(bookCode = bookCode, platform = platform, callbacks = callbacks)
        }
    }
}

private fun setLayoutJoara(
    bookCode: String,
    context: Context,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {
    val apiJoara = RetrofitJoara()
    val JoaraRef = Param.getItemAPI(context)
    JoaraRef["book_code"] = bookCode
    JoaraRef["category"] = "1"

    apiJoara.getBookDetailJoa(
        JoaraRef,
        object : RetrofitDataListener<JoaraBestDetailResult> {
            override fun onSuccess(data: JoaraBestDetailResult) {

                val itemBestDetailInfo: ItemBestDetailInfo

                if (data.book != null) {

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

                    callbacks.invoke(itemBestDetailInfo)

                }
            }
        })
}

private fun setLayoutNaverSeries(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {
    Thread {

        val doc: Document =
            Jsoup.connect("https://series.naver.com/novel/detail.series?productNo=${bookCode}")
                .get()

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

        callbacks.invoke(itemBestDetailInfo)

    }.start()
}

private fun setLayoutNaver(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {
    Thread {

        val doc: Document =
            Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

        val keywordList = arrayListOf<String>()

        for (i in doc.select(".tag_collection").indices) {
            keywordList.add(doc.select(".tag_collection")[i].text())
        }

        val itemBestDetailInfo = ItemBestDetailInfo(
            writer = doc.select(".info_area").select(".info_group span")[1].text(),
            title = doc.select(".section_area_info").select(".info_top").select(".title")
                .text(),
            bookImg = doc.select(".section_area_info").select("img").attr("src"),
            bookCode = bookCode,
            intro = doc.select(".info_area").select(".info_bottom").select("span").text(),
            cntFavorite = doc.select(".info_book .like").text(),
            cntRecom = doc.select(".info_area").select(".info_group").select(".score_area")
                .first()?.text()?.replace("별점", "") ?: "",
            cntPageRead = doc.select(".info_area").select(".info_group").select(".score_area")
                .first()?.nextElementSibling()?.text()?.replace("다운로드 ", "") ?: "",
            genre = doc.select(".info_area").select(".info_group span")[0].text(),
            cntChapter = doc.select(".cont_sub").select(".component_head")
                .select(".past_number").text().replace("(", "").replace(")", ""),
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

        callbacks.invoke(itemBestDetailInfo)

    }.start()
}

private fun setLayoutRidi(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {
    Thread {

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

        val keywordList = arrayListOf<String>()

        for (i in doc.select(".keyword_list li").indices) {
            keywordList.add(doc.select(".keyword_list li")[i].select(".keyword").text())
        }

        val bookImg = if (doc.select(".header_thumbnail_wrap").select(".thumbnail").attr("src")
                .contains("cover_adult.png")
        ) {
            doc.select(".header_thumbnail_wrap").select(".thumbnail").attr("src")
        } else {
            doc.select(".header_thumbnail_wrap").select(".thumbnail").attr("src")
                .replace("//", "https://")
        }

        val itemBestDetailInfo = ItemBestDetailInfo(
            writer = doc.select(".header_info_wrap").select(".js_author_detail_link").text(),
            writerLink = doc.select(".metadata_writer").select("a").attr("href")
                .replace("/author", "https://ridibooks.com/author"),
            title = doc.select(".header_info_wrap").select("h1.info_title_wrap").text(),
            bookImg = bookImg,
            bookCode = bookCode,
            intro = doc.select(".detail_introduce_book").select(".introduce_paragraph").text(),
            cntFavorite = doc.select(".header_thumbnail_wrap").select(".js_preference_count")
                .text(),
            cntRecom = doc.select(".header_info_wrap").select(".StarRate_Score").text(),
            cntPageRead = doc.select(".header_info_wrap").select(".StarRate_ParticipantCount")
                .text(),
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

        callbacks.invoke(itemBestDetailInfo)

    }.start()
}

private fun setLayoutKakaoStage(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {

    val apiKakaoStage = RetrofitKaKao()

    apiKakaoStage.getBestKakaoStageDetail(
        bookCode,
        object : RetrofitDataListener<KakaoStageBestBookResult> {
            override fun onSuccess(data: KakaoStageBestBookResult) {

                val itemBestDetailInfo = ItemBestDetailInfo(
                    writer = data.nickname.name,
                    title = data.title,
                    bookImg = data.thumbnail.url,
                    bookCode = bookCode,
                    intro = data.synopsis,
                    cntChapter = "총 ${data.publishedEpisodeCount}화",
                    cntPageRead = data.visitorCount,
                    cntFavorite = data.favoriteCount,
                    cntRecom = data.episodeLikeCount,
                    genre = data.subGenre.name,
                    tabInfo = arrayListOf(
                        "작품 댓글",
                        "평점 분석",
                        "선호작 분석",
                        "조회 분석",
                        "랭킹 분석",
                        "최근 분석",
                    ),
                    platform = platform
                )

                callbacks.invoke(itemBestDetailInfo)
            }
        })
}

private fun setLayoutMunpia(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {

    Thread {
        val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

        val itemBestDetailInfo = ItemBestDetailInfo(
            writer = doc.select(".member-trigger strong").text(),
            title = doc.select(".detail-box h2 a").text().replace(doc.select(".detail-box h2 a span").text() + " ", ""),
            bookImg = "https:${doc.select(".cover-box img").attr("src")}",
            bookCode = bookCode,
            intro = doc.select(".story").text(),
            cntFavorite = doc.select(".meta-etc dd").next().next()[2]?.text() ?: "",
            cntRecom = doc.select(".meta-etc dd").next().next()[1]?.text() ?: "",
            genre = doc.select(".meta-path strong").text(),
            tabInfo = arrayListOf(
                "작품 댓글",
                "평점 분석",
                "선호작 분석",
                "랭킹 분석",
                "최근 분석",
            ),
            platform = platform
        )

        callbacks.invoke(itemBestDetailInfo)
    }.start()

}

private fun setLayoutOneStory(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {

    val apiOnestory = RetrofitOnestore()
    val param: MutableMap<String?, Any> = HashMap()

    param["channelId"] = bookCode
    param["bookpassYn"] = "N"

    apiOnestory.getOneStoreDetail(
        bookCode,
        param,
        object : RetrofitDataListener<OnestoreBookDetail> {
            override fun onSuccess(data: OnestoreBookDetail) {

                val keywordList = arrayListOf<String>()

                for (i in data.params.tagList.indices) {
                    keywordList.add(data.params.tagList[i].tagNm)
                }

                val itemBestDetailInfo = ItemBestDetailInfo(
                    writer = data.params.artistNm,
                    title = data.params.prodNm,
                    bookImg = data.params.orgFilePos,
                    bookCode = bookCode,
                    intro = data.params.menuNm,
                    cntPageRead = data.params.pageViewTotal,
                    cntFavorite = data.params.favoriteCount,
                    cntRecom = data.params.ratingAvgScore,
                    cntTotalComment = data.params.commentCount,
                    keyword = keywordList,
                    tabInfo = arrayListOf(
                        "작품 댓글",
                        "평점 분석",
                        "조회 분석",
                        "랭킹 분석",
                        "최근 분석",
                    ),
                    platform = platform
                )

                callbacks.invoke(itemBestDetailInfo)

            }
        })
}

private fun setLayoutToksoda(
    bookCode: String,
    platform: String,
    callbacks: (ItemBestDetailInfo) -> Unit
) {

    val apiToksoda = RetrofitToksoda()
    val param: MutableMap<String?, Any> = HashMap()

    param["brcd"] = bookCode
    param["_"] = "1657265744728"

    apiToksoda.getBestDetail(
        param,
        object : RetrofitDataListener<BestToksodaDetailResult> {
            override fun onSuccess(data: BestToksodaDetailResult) {

                val keywordList = arrayListOf<String>()

                if(data.result.hashTagList != null){
                    for (item in data.result.hashTagList) {
                        keywordList.add(item.hashtagNm)
                    }
                }

                val itemBestDetailInfo = ItemBestDetailInfo(
                    writer = data.result.athrnm,
                    writerLink = data.result.athrnm,
                    title = data.result.wrknm,
                    bookImg = "https:${data.result.imgPath}",
                    bookCode = bookCode,
                    intro = data.result.lnIntro ?: "",
                    cntPageRead = data.result.inqrCnt,
                    cntFavorite = data.result.intrstCnt,
                    cntRecom = data.result.goodCnt,
                    genre = data.result.lgctgrNm,
                    keyword = keywordList,
                    tabInfo = arrayListOf(
                        "작가의 다른 작품",
                        "평점 분석",
                        "선호작 분석",
                        "조회 분석",
                        "랭킹 분석",
                        "최근 분석",
                    ),
                    platform = platform
                )

                callbacks.invoke(itemBestDetailInfo)
            }
        })
}

fun gotoUrl(platform: String, bookCode: String, context: Context) {
    val intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(bookCode = bookCode, platform = platform)))
    context.startActivity(intent)
}

fun getUrl(platform: String, bookCode: String): String {

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

        "KAKAO_STAGE" -> {
            "https://pagestage.kakao.com/novels/${bookCode}"
        }

        "ONESTORY_FANTAGY", "ONESTORY_ROMANCE", "ONESTORY_PASS_FANTAGY", "ONESTORY_PASS_ROMANCE" -> {
            "https://onestory.co.kr/detail/${bookCode}"
        }

        "JOARA", "JOARA_PREMIUM", "JOARA_NOBLESS" -> {
            "https://www.joara.com/book/${bookCode}"
        }

        "MUNPIA_PAY", "MUNPIA_FREE" -> {
            "https://novel.munpia.com/${bookCode}"
        }

        "TOKSODA", "TOKSODA_FREE" -> {
            "https://www.tocsoda.co.kr/product/productView?brcd=${bookCode}"
        }

        else -> ""
    }
}