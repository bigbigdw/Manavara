package com.bigbigdw.manavara.manavara

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bigbigdw.manavara.manavara.models.CommunityBoard
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.result.JoaraBoardResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

private fun getBoardJoara(page : Int, context : Context) {
    val apiJoara = RetrofitJoara()
    val param = Param.getItemAPI(context)
    val items = ArrayList<CommunityBoard>()

    param["board"] = "free"
    param["page"] = page

    apiJoara.getBoardListJoa(
        param,
        object : RetrofitDataListener<JoaraBoardResult> {
            override fun onSuccess(it: JoaraBoardResult) {

                if (it.status == "1" && it.boards != null) {
                    for (i in it.boards.indices) {
                        val date = it.boards[i].created
                        items.add(
                            CommunityBoard(
                                it.boards[i].title,
                                it.boards[i].nid,
                                date.substring(4, 6) + "." + date.substring(6, 8),
                            )
                        )

                    }
                }
            }
        })

}

//getBoard("https://gall.dcinside.com/mgallery/board/lists/?id=tgijjdd", "tgijjdd")
//getBoard("https://gall.dcinside.com/mgallery/board/lists/?id=genrenovel", "genrenovel")
//getBoard("https://gall.dcinside.com/mgallery/board/lists?id=lovestory", "lovestory")
//getBoard("https://gall.dcinside.com/board/lists?id=fantasy_new2","fantasy_new2")

@SuppressLint("SuspiciousIndentation")
private fun getBoard(Url: String, ID : String) {

    val items = ArrayList<CommunityBoard>()

    Thread {
        val doc: Document = Jsoup.connect("${Url}&s_type=search_subject_memo&s_keyword=.EC.A1.B0.EC.95.84.EB.9D.BC").post()
        val DC: Elements = doc.select(".ub-content")

        try {

            for (i in DC.indices) {
                val title = DC.select(".gall_tit")[i].text()
                val date = DC.select(".gall_date")[i].text()
                val link = "https://gall.dcinside.com/mgallery/board/view/?id=${ID}&no=${DC[i].absUrl("data-no").replace("https://gall.dcinside.com/mgallery/board/","").replace("https://gall.dcinside.com/board/","").replace("https://gall.dcinside.com/mgallery/board/lists/","").replace("lists/","")}"

                if (doc.select(".gall_tit a")[i].absUrl("href").contains("https://gall.dcinside.com/mgallery/board/view/?id=")) {

                    items.add(
                        CommunityBoard(
                            title,
                            link,
                            date,
                        )
                    )
                }
            }


        } catch (e: IllegalStateException) {
        }

    }.start()
}