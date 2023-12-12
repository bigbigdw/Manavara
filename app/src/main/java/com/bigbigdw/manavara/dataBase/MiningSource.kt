package com.bigbigdw.manavara.dataBase

import android.content.Context
import android.util.Log
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.RetrofitKaKao
import com.bigbigdw.manavara.retrofit.RetrofitMunPia
import com.bigbigdw.manavara.retrofit.RetrofitOnestory
import com.bigbigdw.manavara.retrofit.RetrofitRidi
import com.bigbigdw.manavara.retrofit.RetrofitToksoda
import com.bigbigdw.manavara.retrofit.result.BestMunpiaResult
import com.bigbigdw.manavara.retrofit.result.BestResultKakaoStageNovel
import com.bigbigdw.manavara.retrofit.result.BestToksodaResult
import com.bigbigdw.manavara.retrofit.result.JoaraBestListResult
import com.bigbigdw.manavara.retrofit.result.OneStoreBookResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements



    private fun getItemAPI(mContext: Context?): MutableMap<String?, Any> {

        val Param: MutableMap<String?, Any> = java.util.HashMap()

        mContext ?: return Param

        Param["api_key"] = "android_iK3303O982ab8e2391dp9498"
        Param["ver"] = "2.8.5"
        Param["device"] = "android"
        Param["deviceuid"] = "fp9v-165SxOz0bpxl7JxZW%3AAPA91bH7o-VcJ6LKjIDZ44LGUJiymDpM-ks7CJ3nnnQBr1zDTKsc4vaa1OPRIZ6jPEVrT5q4hJ6Q5mdhxB4GMIB9XQL5Lbd4JWlBauQQ8REZpc0y6fbjpE7V1mq1xTP4CUMTtQqvtABJ"
        Param["devicetoken"] = "NULL"

        return Param
    }

    fun miningNaverSeriesComic(
        platform: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ){
        runBlocking {
            launch {
                Thread{
                    try{

                        val bookList = ArrayList<ItemBookInfo>()

                        for(pageCount in 1..5) {
                            val doc: Document =
                                Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=${pageCount + 1}")
                                    .post()
                            val naverSeries: Elements = doc.select(".comic_top_lst li")

                            for (i in naverSeries.indices) {
                                val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")

                                val item = ItemBookInfo(
                                    writer = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: "",
                                    title = naverSeries.select(".comic_cont h3 a")[i].text(),
                                    bookImg = naverSeries.select("a img")[i].absUrl("src"),
                                    bookCode = bookCode,
                                    cntRecom = naverSeries.select(".comic_cont .info .score_num")[i].text(),
                                    cntChapter = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: "",
                                    intro = naverSeries.select(".comic_cont .dsc")[i].text(),
                                    type = platform,
                                )

                                bookList.add(item)
                            }

                        }

                        callBack.invoke(bookList)
                    } catch (e : Exception){
                        Log.d("DO_MINING", "miningNaverSeriesComic $e")
                    }
                }.start()
            }
        }
    }

    fun miningNaverSeriesNovel(
        platform: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ){
        runBlocking {
            launch {
                Thread{
                    try {

                        val bookList = ArrayList<ItemBookInfo>()

                        for(pageCount in 1..5) {
                            val doc: Document = Jsoup.connect("https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=${pageCount}").post()
                            val naverSeries: Elements = doc.select(".comic_top_lst li")
                            val bookCode = naverSeries.select(".comic_cont a")[pageCount].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")

                            for (i in naverSeries.indices) {

                                val item = ItemBookInfo(
                                    writer = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: "",
                                    title = naverSeries.select(".comic_cont h3 a")[i].text(),
                                    bookImg = naverSeries.select("a img")[i].absUrl("src"),
                                    bookCode = bookCode,
                                    cntRecom = naverSeries.select(".comic_cont .info .score_num")[i].text(),
                                    cntChapter = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: "",
                                    intro = naverSeries.select(".comic_cont .dsc")[i].text(),
                                    type = platform,
                                )

                                bookList.add(item)
                            }

                        }

                        callBack.invoke(bookList)
                    }catch (e : Exception){
                        Log.d("DO_MINING", "miningNaverSeriesNovel $e")
                    }
                }.start()
            }
        }
    }

    fun bookListJoara(
        context: Context,
        platform: String,
        callbacks: (ArrayList<ItemBookInfo>) -> Unit,
    ){
        try {
            val bookList = ArrayList<ItemBookInfo>()
            val apiJoara = RetrofitJoara()
            val param = getItemAPI(context)

            param["page"] = 1
            param["best"] = "today"
            param["store"] = if(platform == "JOARA"){
                ""
            } else if(platform == "JOARA_NOBLESS"){
                "nobless"
            } else{
                "premium"
            }
            param["category"] = "0"
            param["offset"] = "100"

            apiJoara.getJoaraBookList(
                param,
                object : RetrofitDataListener<JoaraBestListResult> {
                    override fun onSuccess(data: JoaraBestListResult) {

                        val books = data.bookLists

                        if (books != null) {

                            for (i in books.indices) {

                                val item = ItemBookInfo(
                                    writer = books[i].writerName,
                                    title = books[i].subject,
                                    bookImg = books[i].bookImg.replace("http://", "https://"),
                                    bookCode = books[i].bookCode,
                                    cntRecom = books[i].cntRecom,
                                    cntChapter = "총 ${books[i].cntChapter}화",
                                    cntPageRead = books[i].cntPageRead,
                                    cntFavorite = books[i].cntFavorite,
                                    intro = books[i].intro,
                                    type = platform,
                                    genre = books[i].category_ko_name
                                )

                                bookList.add(item)
                            }

                            callbacks.invoke(bookList)
                        }
                    }
                })
        }catch (e : Exception){
            Log.d("DO_MINING", "miningJoara $e")
        }
    }

    fun bookListNaver(
        platform: String,
        mining: String,
        platformType: String,
        callbacks: (ArrayList<ItemBookInfo>) -> Unit,
    ){

        runBlocking {
            launch {
                Thread{
                    try{
                        val bookList = ArrayList<ItemBookInfo>()

                        val doc: Document = Jsoup.connect("https://novel.naver.com/${mining}/ranking?genre=999&periodType=DAILY").post()
                        val naverSeries: Elements = if(platformType == "FREE"){
                            doc.select(".ranking_wrap_left .ranking_list li")
                        } else {
                            doc.select(".ranking_wrap_right .ranking_list li")
                        }
                        val ref: MutableMap<String?, Any> = HashMap()

                        for (i in naverSeries.indices) {
                            val bookCode = naverSeries.select("a")[i].absUrl("href").replace("https://novel.naver.com/${mining}/list?novelId=", "")

                            val item = ItemBookInfo(
                                writer = naverSeries.select(".info_group .author")[i].text(),
                                title = naverSeries.select(".title_group .title")[i].text(),
                                bookImg = naverSeries.select("div img")[i].absUrl("src"),
                                bookCode = bookCode,
                                cntRecom = naverSeries.select(".score_area")[i].text().replace("별점", ""),
                                cntChapter = naverSeries[i].select(".info_group .count").first()!!.text(),
                                cntPageRead = naverSeries[i].select(".info_group .count").next().first()!!.text(),
                                cntFavorite = naverSeries.select(".meta_data_group .count")[i].text(),
                                type = platform,
                            )

                            bookList.add(item)

                        }

                        callbacks.invoke(bookList)
                    }catch (e : Exception){
                        Log.d("DO_MINING", "miningNaver $e")
                    }

                }.start()
            }
        }
    }

    fun miningOnestory(
        platform: String,
        platformType: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ) {

        for(page in 1..4){
            try {
                val bookList = ArrayList<ItemBookInfo>()

                val apiOneStory = RetrofitOnestory()
                val param: MutableMap<String?, Any> = HashMap()
                param["menuId"] = platformType
                param["startKey"] = when (page) {
                    1 -> {
                        ""
                    }
                    2 -> {
                        "61/0"
                    }
                    3 -> {
                        "125/0"
                    }
                    else -> {
                        "183/0"
                    }
                }

                apiOneStory.getBestOneStore(
                    param,
                    object : RetrofitDataListener<OneStoreBookResult> {
                        override fun onSuccess(data: OneStoreBookResult) {

                            val productList = data.params?.productList

                            if (productList != null) {
                                for (i in productList.indices) {

                                    val bookCode = productList[i].prodId

                                    val item = ItemBookInfo(
                                        writer =  productList[i].artistNm,
                                        title = productList[i].prodNm,
                                        bookImg = "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl,
                                        bookCode = bookCode,
                                        cntRecom = productList[i].avgScore,
                                        cntTotalComment = productList[i].commentCount,
                                        cntPageRead = productList[i].totalCount,
                                        type = platform,
                                    )

                                    bookList.add(item)
                                }

                                callBack.invoke(bookList)
                            }
                        }
                    })
            } catch (exception: Exception) {
                Log.d("EXCEPTION", "ONESTORE")
            }
        }
    }

    fun miningOnestoryPass(
        platform: String,
        platformType: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ) {

        for(page in 1..4){
            try {
                val bookList = ArrayList<ItemBookInfo>()
                val apiOneStory = RetrofitOnestory()
                val param: MutableMap<String?, Any> = HashMap()

                param["menuId"] = platformType
                param["freepassGrpCd"] = "PD013333"

                param["startKey"] = when (page) {
                    1 -> {
                        ""
                    }
                    2 -> {
                        "367/0"
                    }
                    3 -> {
                        "1439/0"
                    }
                    else -> {
                        "1957/0"
                    }
                }

                apiOneStory.getBestOneStorePass(
                    param,
                    object : RetrofitDataListener<OneStoreBookResult> {
                        override fun onSuccess(data: OneStoreBookResult) {

                            val productList = data.params?.productList

                            if (productList != null) {
                                for (i in productList.indices) {

                                    val bookCode = productList[i].prodId

                                    val item = ItemBookInfo(
                                        writer =  productList[i].artistNm,
                                        title = productList[i].prodNm,
                                        bookImg = "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl,
                                        bookCode = bookCode,
                                        cntRecom = productList[i].avgScore,
                                        cntTotalComment = productList[i].commentCount,
                                        cntPageRead = productList[i].totalCount,
                                        type = platform,
                                    )

                                    bookList.add(item)
                                }
                            }

                            callBack.invoke(bookList)
                        }
                    })
            } catch (exception: Exception) {
                Log.d("EXCEPTION", "ONESTORE")
            }
        }
    }

    fun miningKakaoStage(
        platform: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ) {
        val bookList = ArrayList<ItemBookInfo>()
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = "7,1,2,3,4,5,6"
        param["recentHours"] = "72"

        apiKakao.getBestKakaoStage(
            param,
            object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                    data.let {

                        val list = it

                        for (i in list.indices) {

                            val novel = list[i].novel
                            val bookCode = novel?.stageSeriesNumber ?: ""

                            val item = ItemBookInfo(
                                genre = list[i].novel?.subGenre?.name ?: "",
                                writer = novel!!.nickname!!.name,
                                title = novel.title,
                                bookImg = novel.thumbnail!!.url,
                                bookCode = bookCode,
                                intro = novel.synopsis,
                                cntRecom = novel.episodeLikeCount,
                                cntPageRead = novel.visitorCount,
                                cntChapter =  "총 ${novel.publishedEpisodeCount}화",
                                cntFavorite = novel.favoriteCount,
                                type = platform,
                            )

                            bookList.add(item)

                        }

                        callBack.invoke(bookList)

                    }
                }
            })
    }

    fun miningMunpia(
        platform: String,
        platformType: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ) {

        for(page in 1..4){
            val bookList = ArrayList<ItemBookInfo>()
            val apiMoonPia = RetrofitMunPia()
            val param: MutableMap<String?, Any> = HashMap()

            param["section"] = platformType
            param["exclusive"] = ""
            param["outAdult"] = "true"
            param["offset"] = when (page) {
                1 -> {
                    ""
                }
                2 -> {
                    "28"
                }
                3 -> {
                    "53"
                }
                else -> {
                    "78"
                }
            }

            apiMoonPia.postMunPiaBest(
                param,
                object : RetrofitDataListener<BestMunpiaResult> {
                    override fun onSuccess(data: BestMunpiaResult) {

                        data.api?.items.let {

                            if (it != null) {
                                for (i in it.indices) {

                                    val bookCode = it[i].nvSrl

                                    val item = ItemBookInfo(
                                        genre = it[i].nvGnMainTitle,
                                        writer = it[i].author,
                                        title = it[i].nvTitle,
                                        bookImg = "https://cdn1.munpia.com${it[i].nvCover}",
                                        bookCode = bookCode,
                                        intro = it[i].nvStory,
                                        cntRecom = it[i].nsrData?.hit!!,
                                        cntFavorite = it[i].nsrData?.number!!,
                                        cntChapter =  it[i].nsrData?.prefer!!,
                                        type = platform,
                                    )

                                    bookList.add(item)

                                }

                                callBack.invoke(bookList)
                            }
                        }

                    }
                })
        }
    }

    fun miningToksoda(
        platform: String,
        platformType: String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ) {

        for(page in 1..5){
            val bookList = ArrayList<ItemBookInfo>()
            val apiToksoda = RetrofitToksoda()
            val param: MutableMap<String?, Any> = HashMap()

            param["page"] = page
            param["lgctgrCd"] = "all"
            param["over19Yn"] = "N"
            param["sumtalkYn"] = "N"
            param["rookieYn"] = "N"
            param["statsClsfCd"] = "00073"
            param["freePblserlYn"] = platformType
            param["_"] = "1696853385623"

            apiToksoda.getBestList(
                param,
                object : RetrofitDataListener<BestToksodaResult> {
                    override fun onSuccess(data: BestToksodaResult) {

                        data.resultList?.let { it ->
                            for (i in it.indices) {

                                val bookCode = it[i].brcd

                                val item = ItemBookInfo(
                                    genre = it[i].lgctgrNm,
                                    writer = it[i].athrnm,
                                    title = it[i].wrknm,
                                    bookImg = "https:${it[i].imgPath}",
                                    bookCode = bookCode,
                                    intro = it[i].lnIntro,
                                    cntRecom = it[i].intrstCnt,
                                    cntPageRead = it[i].inqrCnt,
                                    cntFavorite = it[i].goodAllCnt,
                                    cntChapter =  "총 ${it[i].whlEpsdCnt}화",
                                    type = platform,
                                )

                                bookList.add(item)

                            }

                            callBack.invoke(bookList)
                        }
                    }
                })
        }
    }

    fun miningRidi(
        mining: String,
        platform : String,
        callBack: (ArrayList<ItemBookInfo>) -> Unit,
    ) {
        try {
            val bookList = ArrayList<ItemBookInfo>()
            val apiRidi = RetrofitRidi()
            val param: MutableMap<String?, Any> = HashMap()

            param["tab"] = "books"
            param["category_id"] = mining
            param["platform"] = "web"
            param["offset"] = "0"
            param["limit"] = "100"
            param["order_by"] = "popular"

            apiRidi.getRidiRomance(
                map = param,
                object : RetrofitDataListener<String> {
                    override fun onSuccess(data: String) {
                        val baseJSONObject = JSONObject(data)
                        val productList = baseJSONObject.optJSONObject("data")?.optJSONArray("items")

                        if (productList != null) {
                            for (i in 0 until productList.length()) {

                                val jsonObject = productList.getJSONObject(i).optJSONObject("book")

                                if (jsonObject != null) {
                                    val bookCode = jsonObject.optString("bookId")
                                    val ratings = jsonObject.optJSONArray("ratings")
                                    var ratingCount = 0F
                                    var ratePoints = 0F

                                    if (ratings != null) {
                                        for (j in 0 until ratings.length()) {
                                            val rate = ratings.getJSONObject(j)
                                            ratingCount += rate.optInt("count")
                                            ratePoints += rate.optInt("count") * rate.optInt("rating")
                                        }
                                    }

                                    val item = ItemBookInfo(
                                        genre = JSONObject(jsonObject.optJSONArray("categories")?.get(0).toString()).optString("name") ?: "",
                                        writer = JSONObject(jsonObject.optJSONArray("authors")?.get(0).toString()).optString("name") ?: "",
                                        title = if((jsonObject.optJSONObject("serial")?.optString("title")?.length ?: 0) == 0){
                                            jsonObject.optString("title")
                                        } else {
                                            jsonObject.optJSONObject("serial")?.optString("title") ?: ""
                                        },
                                        bookImg = jsonObject.optJSONObject("cover")?.optString("xxlarge") ?: "",
                                        bookCode = bookCode,
                                        intro = jsonObject.optJSONObject("introduction")?.optString("description") ?: "",
                                        cntRecom = if (ratingCount == 0F) {
                                            "0"
                                        } else {
                                            String.format("%.1f", ratePoints / ratingCount)
                                        },
                                        cntPageRead = ratingCount.toInt().toString(),
                                        cntChapter =  "총 ${jsonObject.optJSONObject("serial")?.optString("total") ?: ""}화",
                                        type = platform,
                                    )

                                    bookList.add(item)

                                }
                            }
                        }
                        callBack.invoke(bookList)
                    }
                })
        } catch (exception: Exception) {
            Log.d("DO_MINING", "RIDI")
        }
    }
