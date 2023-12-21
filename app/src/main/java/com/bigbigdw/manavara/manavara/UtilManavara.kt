package com.bigbigdw.manavara.manavara

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.manavara.models.CommunityBoard
import com.bigbigdw.manavara.manavara.models.EventData
import com.bigbigdw.manavara.manavara.models.ItemAlert
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.manavara.retrofit.RetrofitDataListener
import com.bigbigdw.manavara.retrofit.RetrofitJoara
import com.bigbigdw.manavara.retrofit.RetrofitKaKao
import com.bigbigdw.manavara.retrofit.RetrofitToksoda
import com.bigbigdw.manavara.retrofit.result.BestBannerListResult
import com.bigbigdw.manavara.retrofit.result.JoaraBoardResult
import com.bigbigdw.manavara.retrofit.result.JoaraEventResult
import com.bigbigdw.manavara.retrofit.result.KakaoStageEventList
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.DataStoreManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


fun getPickList(context: Context, uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3", type : String, root : String = "MY", callbacks: (ArrayList<String>, ArrayList<ItemBookInfo>) -> Unit){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch {
        dataStore.getDataStoreString(DataStoreManager.UID).collect{
            val rootRef = FirebaseDatabase.getInstance().reference
                .child("USER")
                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
//                .child(it ?: uid)
                .child("PICK")
                .child(root)
                .child(type)

            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory : ArrayList<String> = ArrayList()
                    val pickItemList : ArrayList<ItemBookInfo> = ArrayList()

                    if (dataSnapshot.exists()) {

                        pickCategory.add("전체")

                        for(category in dataSnapshot.children){
                            pickCategory.add(category.key ?: "")

                            for(pickItem in category.children){
                                val item = pickItem.getValue(ItemBookInfo::class.java)

                                if(item != null){
                                    pickItemList.add(item)
                                }
                            }
                        }

                    }

                    callbacks.invoke(pickCategory, pickItemList)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun setSharePickList(
    context: Context,
    listName: String,
    type : String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    pickCategory: ArrayList<String>,
    pickItemList: ArrayList<ItemBookInfo>,
    initTitle: () -> Unit
){

    val filteredMap = mutableMapOf<String, ItemBookInfo>()
    val pickItemListMap = mutableMapOf<String, ItemBookInfo>()
    val dataStore = DataStoreManager(context)

    for(item in pickItemList){
        pickItemListMap[item.bookCode] = item
    }

    for(bookCode in pickCategory){
        val item = pickItemListMap[bookCode]
        if(item != null){
            filteredMap[bookCode] = item
        }
    }

    CoroutineScope(Dispatchers.Main).launch {
        dataStore.getDataStoreString(DataStoreManager.UID).collect{
            FirebaseDatabase.getInstance().reference
                .child("USER")
                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
//                .child(it ?: uid)
                .child("PICK")
                .child("SHARE")
                .child(type)
                .child(listName)
                .setValue(filteredMap)

            FirebaseDatabase.getInstance().reference
                .child("PICK_SHARE")
                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
//                .child(it ?: uid)
                .child(type)
                .child(listName)
                .setValue(filteredMap)
        }
    }

    initTitle()
}

fun getUserPickShareList(
    context : Context,
    type: String,
    root : String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    callbacks: (ArrayList<String>, MutableMap<String, ArrayList<ItemBookInfo>>) -> Unit
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch{
        dataStore.getDataStoreString(DataStoreManager.UID).collect{

            val userpickRef = FirebaseDatabase.getInstance().reference
                .child("USER")
                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                //                    .child(it ?: uid)
                .child("PICK")

            val rootRef = when (root) {
                "MY" -> {
                    userpickRef.child("MY").child(type)
                }
                "MY_SHARE" -> {
                    userpickRef.child("SHARE").child(type)
                }
                "PICK_SHARE" -> {
                    FirebaseDatabase.getInstance().reference
                        .child("PICK_SHARE")
                        .child("ecXPTeFiDnV732gOiaD8u525NnE3")
        //                    .child(it ?: uid)
                        .child(type)
                }
                else -> {
                    userpickRef.child("DOWNLOADED").child(type)
                }
            }

            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory = ArrayList<String>()
                    val pickItemList = mutableMapOf<String, ArrayList<ItemBookInfo>>()

                    if (dataSnapshot.exists()) {

                        try {
                            for (category in dataSnapshot.children) {
                                pickCategory.add(category.key ?: "")
                                val itemList = ArrayList<ItemBookInfo>()

                                for (pickItem in category.children) {
                                    val item = pickItem.getValue(ItemBookInfo::class.java)

                                    if (item != null) {
                                        itemList.add(item)
                                    }
                                }

                                pickItemList[category.key ?: ""] = itemList
                            }

                            callbacks.invoke(pickCategory, pickItemList)
                        } catch (e: Exception) {
                            callbacks.invoke(pickCategory, pickItemList)
                        }
                    } else {
                        callbacks.invoke(pickCategory, pickItemList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun editSharePickList(
    context: Context,
    type : String,
    status : String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    listName: String,
    pickItemList: ArrayList<ItemBookInfo>?,
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch {
        dataStore.getDataStoreString(DataStoreManager.UID).collect {
            if(status == "DELETE"){

                val pickUser = FirebaseDatabase.getInstance().reference
                    .child("USER")
                    .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                    .child("PICK")

                pickUser.child("DOWNLOADED")
                    .child(type)
                    .child(listName)
                    .removeValue()

                pickUser.child("SHARE")
//                    .child(it ?: uid)
                    .child(type)
                    .child(listName)
                    .removeValue()

            } else {
                val pickItemListMap = mutableMapOf<String, ItemBookInfo>()

                if (pickItemList != null) {
                    for(item in pickItemList){
                        item.belong = "SHARE_DOWNLOADED"
                        pickItemListMap[item.bookCode] = item
                    }
                }

                FirebaseDatabase.getInstance().reference
                    .child("USER")
                    .child("ecXPTeFiDnV732gOiaD8u525NnE3")
//                    .child(it ?: uid)
                    .child("PICK")
                    .child("DOWNLOADED")
                    .child(type)
                    .child(listName)
                    .setValue(pickItemListMap)
            }
        }
    }
}

fun getUserPickShareListALL(
    context : Context,
    type: String,
    uid : String = "ecXPTeFiDnV732gOiaD8u525NnE3",
    callbacks: (ArrayList<String>, MutableMap<String, ArrayList<ItemBookInfo>>) -> Unit
){

    val dataStore = DataStoreManager(context)

    CoroutineScope(Dispatchers.Main).launch{
        dataStore.getDataStoreString(DataStoreManager.UID).collect{
            val rootRef = FirebaseDatabase.getInstance().reference
                .child("USER")
                .child("ecXPTeFiDnV732gOiaD8u525NnE3")
                //                    .child(it ?: uid)
                .child("PICK")


            rootRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pickCategory = ArrayList<String>()
                    val pickItemList = mutableMapOf<String, ArrayList<ItemBookInfo>>()
                    val myList = dataSnapshot.child("MY").child(type)
                    val downloadList = dataSnapshot.child("DOWNLOADED").child(type)
                    val shareList = dataSnapshot.child("SHARE").child(type)

                    if (myList.exists()) {

                        pickCategory.add("내 작품들")
                        val itemList = ArrayList<ItemBookInfo>()

                        for (category in myList.children) {

                            for (pickItem in category.children) {
                                val item = pickItem.getValue(ItemBookInfo::class.java)
                                item?.belong = "MY"

                                if (item != null) {
                                    itemList.add(item)
                                }
                            }

                            pickItemList["내 작품들"] = itemList
                        }
                    }

                    if (downloadList.exists()) {

                        for (category in downloadList.children) {

                            pickCategory.add(category.key ?: "")

                            val itemList = ArrayList<ItemBookInfo>()

                            for (pickItem in category.children) {
                                val item = pickItem.getValue(ItemBookInfo::class.java)
                                item?.belong = "DOWNLOADED"

                                if (item != null) {
                                    itemList.add(item)
                                }
                            }

                            pickItemList[category.key ?: ""] = itemList
                        }
                    }

                    if (shareList.exists()) {

                        for (category in shareList.children) {

                            pickCategory.add(category.key ?: "")

                            val itemList = ArrayList<ItemBookInfo>()

                            for (pickItem in category.children) {
                                val item = pickItem.getValue(ItemBookInfo::class.java)
                                item?.belong = "SHARE"

                                if (item != null) {
                                    itemList.add(item)
                                }
                            }

                            pickItemList[category.key ?: ""] = itemList
                        }
                    }

                    callbacks.invoke(pickCategory, pickItemList)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}

fun getFCMList(child: String = "NOTICE", callbacks: (ArrayList<ItemAlert>) -> Unit) {

    val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child(child)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val fcmlist = ArrayList<ItemAlert>()

                for (item in dataSnapshot.children) {
                    val fcm: ItemAlert? =
                        dataSnapshot.child(item.key ?: "").getValue(ItemAlert::class.java)

                    if (fcm != null) {
                        fcmlist.add(fcm)
                    }
                }

                fcmlist.reverse()

                callbacks.invoke(fcmlist)

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}