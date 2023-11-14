package com.bigbigdw.manavara.best.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavara.best.event.EventBestDetail
import com.bigbigdw.manavara.best.event.StateBestDetail
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.main.event.EventMain
import com.bigbigdw.manavara.retrofit.Param
import com.bigbigdw.moavara.Retrofit.JoaraBestDetailResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelBestDetail @Inject constructor() : ViewModel() {

    private val events = Channel<EventBestDetail>()

    val state: StateFlow<StateBestDetail> = events.receiveAsFlow()
        .runningFold(StateBestDetail(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateBestDetail())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateBestDetail, event: EventBestDetail): StateBestDetail {
        return when(event){
            EventBestDetail.Loaded -> {
                current.copy(Loaded = true)
            }

            is EventBestDetail.SetUserInfo -> {
                current.copy(userInfo = event.userInfo)
            }

            is EventBestDetail.SetItemBestDetailInfo -> {
                current.copy(itemBestDetailInfo = event.itemBestDetailInfo)
            }

            else -> {
                current.copy(Loaded = false)
            }
        }
    }

    fun setBestDetailInfo(platform : String, bookCode: String, context: Context) {

        when (platform) {
            "JOARA", "JOARA_NOBLESS", "JOARAPREMIUM" -> {
                setLayoutJoara(bookCode = bookCode, context = context)
            }
//            "Naver_Today", "Naver_Challenge", "Naver" -> {
//                setLayoutNaverToday()
//            }
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

    private fun setLayoutJoara(bookCode : String, context : Context) {
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
                            tabInfo = arrayListOf("작품 댓글", "작가의 다른 작품", "작품 분석"),
                            keyword = data.book.keyword
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
}