package com.bigbigdw.manavara.manavara

import android.content.Context
import android.util.Log
import com.bigbigdw.manavara.manavara.models.EventData
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.RetrofitKaKao
import com.bigbigdw.manavara.retrofit.RetrofitToksoda
import com.bigbigdw.manavara.retrofit.result.BestBannerListResult
import com.bigbigdw.manavara.retrofit.result.JoaraEventResult
import com.bigbigdw.manavara.retrofit.result.KakaoStageEventList
import com.bigbigdw.manavara.util.DBDate
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URLDecoder
import java.net.URLEncoder

fun setEvent(platform : String, context : Context) {

    when (platform) {
        "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
            getEventJoara(context)
        }
        "RIDI_FANTAGY" -> {
            getEventRidi("RIDI_FANTAGY")
        }
        "RIDI_ROMANCE" -> {
            getEventRidi("RIDI_ROMANCE")
        }
        "RIDI_ROFAN" -> {
            getEventRidi("RIDI_ROFAN")
        }
        "KAKAO_STAGE" -> {
            getEventKakaoStage()
        }
        "MUNPIA_PAY", "MUNPIA_FREE" -> {
            getEventMunpia()
        }
        "TOKSODA", "TOKSODA_FREE" -> {
            getEventToksoda()
        }
    }
}

private fun getEventJoara(context: Context) {

    val apiJoara = RetrofitJoara()
    val param = Param.getItemAPI(context)
    param["page"] = "0"
    param["banner_type"] = "app_home_top_banner"
    param["category"] = "0"

    apiJoara.getJoaraEvent(
        param,
        object : RetrofitDataListener<JoaraEventResult> {
            override fun onSuccess(data: JoaraEventResult) {

                val banner = data.banner
                val items = ArrayList<EventData>()

                if (banner != null) {

                    for (i in banner.indices) {

                        try {
                            items.add(
                                EventData(
                                    banner[i].joaralink,
                                    banner[i].imgfile.replace("http://", "https://"),
                                    "",
                                    "",
                                    DBDate.dateMMDD(),
                                    999,
                                    "JOARA",
                                    ""
                                ),
                            )
                        } catch (e: IndexOutOfBoundsException) {
                            Log.d("EVENT", "JOARA $e")
                        }
                    }
                }
            }
        })
}

private fun getEventRidi(platform: String) {
    Thread {
        val doc: Document = if(platform == "RIDI_FANTAGY"){
            Jsoup.connect("https://ridibooks.com/event/fantasy_serial").get()
        } else if(platform == "RIDI_ROMANCE"){
            Jsoup.connect("https://ridibooks.com/event/romance_serial").get()
        } else {
            Jsoup.connect("https://ridibooks.com/event/romance_fantasy_serial").get()
        }

        val ridiKeyword: Elements = doc.select("ul .event_list")
        val items = ArrayList<EventData>()

        for (i in ridiKeyword.indices) {
            try {
                items.add(
                    EventData(
                        doc.select(".event_title a")[i].absUrl("href")
                            .replace("https://ridibooks.com/event/", ""),
                        doc.select(".image_link img")[i].absUrl("src"),
                        doc.select(".event_title a")[i].text(),
                        "",
                        DBDate.dateMMDD(),
                        999,
                        platform,
                        ""
                    )
                )
            } catch (e: IndexOutOfBoundsException) {
                Log.d("EVENT", "$platform $e")
            }
        }


    }.start()
}

private fun getEventKakaoStage() {
    val apiKakaoStage = RetrofitKaKao()
    val param: MutableMap<String?, Any> = HashMap()
    val items = ArrayList<EventData>()

    param["page"] = "0"
    param["progress"] = "true"
    param["size"] = "9"

    apiKakaoStage.getKakaoStageEventList(
        param,
        object : RetrofitDataListener<KakaoStageEventList> {
            override fun onSuccess(data: KakaoStageEventList) {
                for (i in data.content.indices) {
                    try {

                        items.add(
                            EventData(
                                data.content[i].id,
                                data.content[i].desktopListImage?.image?.url ?: "",
                                data.content[i].title,
                                data.content[i].desktopDetailImage?.image?.url
                                    ?: "",
                                DBDate.dateMMDD(),
                                999,
                                "KAKAO_STAGE",
                                ""
                            )
                        )
                    } catch (e: IndexOutOfBoundsException) {
                        Log.d("EVENT", "KAKAO_STAGE $e")
                    }
                }
            }
        })
}

private fun getEventMunpia() {
    Thread {
        val items = ArrayList<EventData>()
        val doc: Document = Jsoup.connect("https://square.munpia.com/event").get()
        val MunpiaWrap: Elements = doc.select(".light .entries tbody tr a img")

        for (i in MunpiaWrap.indices) {

            try {
                items.add(
                    EventData(
                        URLEncoder.encode(
                            doc.select(".light .entries tbody tr td a")[i].attr(
                                "href"
                            ), "utf-8"
                        ),
                        "https:${
                            doc.select(".light .entries tbody tr a img")[i].attr(
                                "src"
                            )
                        }",
                        doc.select(".light .entries .subject td a")[i].text(),
                        "",
                        DBDate.dateMMDD(),
                        999,
                        "MUNPIA_FREE",
                        ""
                    )
                )
            } catch (e: IndexOutOfBoundsException) {
                Log.d("EVENT", "KAKAO_STAGE $e")
            }
        }
    }.start()
}

private fun getEventToksoda() {
    val apiToksoda = RetrofitToksoda()
    val param: MutableMap<String?, Any> = HashMap()
    val items = ArrayList<EventData>()

    param["bnnrPstnCd"] = "00023"
    param["bnnrClsfCd"] = "00320"
    param["expsClsfCd"] = "0007"
    param["fileNo"] = "1"
    param["pageRowCount"] = "10"
    param["_"] = "1657271613642"

    apiToksoda.getEventList(
        param,
        object : RetrofitDataListener<BestBannerListResult> {
            override fun onSuccess(data: BestBannerListResult) {

                if (data.resultList != null) {
                    for (i in data.resultList.indices) {

                        try {
                            items.add(
                                EventData(
                                    data.resultList[2 * i].linkInfo.replace("https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=",""),
                                    "https:${data.resultList[i].imgPath}",
                                    data.resultList[2 * i].bnnrNm,
                                    "",
                                    DBDate.dateMMDD(),
                                    999,
                                    "Toksoda",
                                    ""
                                )
                            )
                        } catch (e: IndexOutOfBoundsException) {
                            Log.d("EVENT", "TOKSODA $e")
                        }
                    }
                }
            }
        })
}

private fun getUrl(link :String ,platform: String): String {
    when (platform) {
        "JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM" -> {
            if(link.contains("joaralink://event?event_id=")){
                return "https://www.joara.com/event/" + link.replace("joaralink://event?event_id=", "")
            } else if(link.contains("joaralink://notice?notice_id=")) {
                return "https://www.joara.com/notice/" + link.replace("joaralink://notice?notice_id=", "")
            } else {
                return ""
            }
        }
        "RIDI_FANTAGY", "RIDI_ROMANCE", "RIDI_ROFAN" -> {
            return if (link.contains("https://ridibooks.com/books/")) {
                link
            } else {
                "https://ridibooks.com/event/${link}"
            }
        }
        "KAKAO_STAGE" -> {
            return "https://pagestage.kakao.com/events/${link}"
        }
        "MUNPIA_PAY", "MUNPIA_FREE" -> {
            return "https://square.munpia.com/${URLDecoder.decode(link, "utf-8")}"
        }
        "TOKSODA", "TOKSODA_FREE" -> {
            return "https://www.tocsoda.co.kr/event/eventDetail?eventmngSeq=${link}"
        }
        else -> return ""
    }
}