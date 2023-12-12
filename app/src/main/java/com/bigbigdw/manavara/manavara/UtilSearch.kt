package com.bigbigdw.manavara.manavara

import android.content.Context
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.RetrofitKaKao
import com.bigbigdw.manavara.retrofit.RetrofitToksoda
import com.bigbigdw.manavara.retrofit.result.BestToksodaSearchResult
import com.bigbigdw.manavara.retrofit.result.JoaraSearchResult
import com.bigbigdw.manavara.retrofit.result.KakaoStageSearchResult
import com.bigbigdw.manavara.util.DBDate
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

fun searchJoara(page: Int, text: String, context : Context) {
    val apiJoara = RetrofitJoara()
    val param = Param.getItemAPI(context)
    val searchItems = ArrayList<ItemBookInfo>()

    param["page"] = page
    param["query"] = text
    param["collection"] = ""
    param["search"] = "subject"
    param["kind"] = ""
    param["category"] = ""
    param["min_chapter"] = ""
    param["max_chapter"] = ""
    param["interval"] = ""
    param["orderby"] = ""
    param["except_query"] = ""
    param["except_search"] = ""
    param["expr_point"] = ""
    param["score_point"] = "1"

    apiJoara.getSearchJoara(
        param,
        object : RetrofitDataListener<JoaraSearchResult> {
            override fun onSuccess(data: JoaraSearchResult) {

                val books = data.books

                if (books != null) {
                    for (i in books.indices) {
                        searchItems.add(
                            ItemBookInfo(
                                writer = books[i].writer_name,
                                title = books[i].subject,
                                bookImg = books[i].book_img.replace("http://","https://"),
                                bookCode = books[i].bookCode,
                                intro = books[i].intro,
                                cntPageRead = books[i].cntPageRead,
                                cntFavorite = books[i].cntFavorite,
                                cntRecom = books[i].cntRecom,
                                cntChapter = "총 ${books[i].cntChapter}화",
                                cntTotalComment = books[i].cntTotalComment,
                                genre = books[i].categoryKoName,
                                type = "JOARA",
                            )
                        )
                    }
                }
            }
        })
}

fun searchKakaoStage(page: Int, text: String) {
    val apiKakaoStage = RetrofitKaKao()
    val param: MutableMap<String?, Any> = HashMap()
    val searchItems = ArrayList<ItemBookInfo>()

    param["genreIds"] = "1,2,3,4,5,6,7"
    param["keyword"] = text
    param["size"] = 20
    param["adult"] = "false"
    param["kakaopageSeries"] = "true"
    param["page"] = page

    apiKakaoStage.getSearchKakaoStage(
        param,
        object : RetrofitDataListener<KakaoStageSearchResult> {
            override fun onSuccess(data: KakaoStageSearchResult) {

                val results = data.content

                for (items in results) {
                    searchItems.add(
                        ItemBookInfo(
                            writer = items.nickname.name,
                            title = items.title,
                            bookImg = items.thumbnail.url,
                            bookCode = items.stageSeriesNumber,
                            intro = items.synopsis,
                            cntPageRead = items.viewCount,
                            cntFavorite = items.favoriteCount,
                            cntChapter =  "총 ${items.publishedEpisodeCount}화",
                            genre = items.subGenre.name,
                            type = "KAKAO_STAGE",
                        )
                    )
                }
            }
        })
}

fun searchNaver(text: String, platform: String) {
    Thread {
        var doc: Document? = null
        val searchItems = ArrayList<ItemBookInfo>()

        when (platform) {
            "NAVER" -> {
                doc =
                    Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=webnovel&target=novel")
                        .post()
            }
            "NAVER_TODAY" -> {
                doc =
                    Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=best&target=novel")
                        .post()
            }
            "NAVER_CHALLENGE" -> {
                doc =
                    Jsoup.connect("https://novel.naver.com/search?keyword=${text}&section=challenge&target=novel")
                        .post()
            }
        }

        val Naver: Elements = (doc?.select(".srch_cont .list_type2 li") ?: "") as Elements

        for (items in Naver) {
            searchItems.add(
                ItemBookInfo(
                    writer =  items.select(".league").text(),
                    title = items.select(".ellipsis").text(),
                    bookImg = items.select("div img").attr("src"),
                    bookCode =  items.select("a").attr("href").replace("/webnovel/list?novelId=","").replace("/best/list?novelId=", "").replace("/challenge/list?novelId=", ""),
                    genre = items.select(".bullet_comp").text(),
                    type = platform,
                )
            )
        }
    }.start()
}

fun searchMunpia(text: String) {
    Thread {
        val searchItems = ArrayList<ItemBookInfo>()
        val doc: Document =
            Jsoup.connect("https://novel.munpia.com/page/hd.platinum/view/search/keyword/${text}/order/search_result")
                .post()

        val Munpia: Elements = doc.select(".article_wrap .article")

        var info3 = ""
        var info4 = ""
        var info5 = ""

        for (items in Munpia) {

            searchItems.add(
                ItemBookInfo(
                    writer = items.select(".author").text(),
                    title = items.select(".detail a").text(),
                    bookImg = "https://${items.select(".thumb img").attr("src")}",
                    bookCode = items.select(".detail a").attr("href").replace("https://novel.munpia.com/",""),
                    intro = items.select(".synopsis").text(),
                    cntPageRead =  items.select(".info span").first()?.text().toString(),
                    cntFavorite = items.select(".info span").next().get(0)?.text() ?: "",
                    cntRecom = items.select(".info span").next().get(1)?.text() ?: "",
                    cntChapter = items.select(".info span").next().get(2)?.text() ?: "",
                    type = "MUNPIA",
                )
            )
        }
    }.start()
}

fun searchToksoda(text: String, page : Int) {
    val apiToksoda = RetrofitToksoda()
    val param: MutableMap<String?, Any> = HashMap()
    val searchItems = ArrayList<ItemBookInfo>()

    param["srchwrd"] = text
    param["keyword"] = text
    param["pageSize"] = "20"
    param["pageIndex"] = (page * 20)
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

                data.resultList.let {
                    if (it != null) {
                        for (i in it.indices) {

                            searchItems.add(
                                ItemBookInfo(
                                    writer = it[i].AUTHOR,
                                    title = it[i].BOOK_NM,
                                    bookImg = "https:${it[i].IMG_PATH}",
                                    bookCode = it[i].BARCODE,
                                    intro = it[i].HASHTAG_NM,
                                    cntPageRead = it[i].HASHTAG_NM,
                                    cntFavorite = it[i].PUB_NM,
                                    cntRecom = it[i].LGCTGR_NM,
                                    cntChapter = it[i].INQR_CNT,
                                    cntTotalComment = it[i].INQR_CNT,
                                    genre = it[i].INTRST_CNT,
                                    type = "TOKSODA",
                                )
                            )
                        }
                    }
                }
            }
        })
}