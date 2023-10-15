package com.bigbigdw.manavara.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import getBookCount

@Composable
fun ScreenDB(){

    val (getMenu, setMenu) = remember { mutableStateOf("TODAY") }
    val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf(novelListEng()[0]) }
    val (getDetailType, setDetailType) = remember { mutableStateOf("NOVEL") }

    ItemMainSettingSingleTablet(
        containerColor = color4AD7CF,
        image = R.drawable.icon_setting_wht,
        title = "작품 검색",
        body = "플랫폼과 무관하게 작품 검색 진행",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color4AD7CF,
        image = R.drawable.icon_setting_wht,
        title = "북코드 검색",
        body = "플랫폼과 무관하게 작품 검색 진행",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color4AD7CF,
        image = R.drawable.icon_setting_wht,
        title = "웹툰 DB 검색",
        body = "웹툰 DB 검색",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color4AD7CF,
        image = R.drawable.icon_setting_wht,
        title = "웹소설 DB 검색",
        body = "웹소설 DB 검색",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color5372DE,
        image = R.drawable.icon_novel_wht,
        title = "마나바라 베스트 웹소설 DB",
        body = "마나바라에 기록된 베스트 웹소설 리스트",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color998DF9,
        image = R.drawable.icon_webtoon_wht,
        title = "마나바라 베스트 웹툰 DB",
        body = "마나바라에 기록된 웹툰 웹툰 리스트",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = colorF17FA0,
        image = R.drawable.icon_best_wht,
        title = "투데이 장르 베스트",
        body = "플랫폼별 투데이 베스트 장르 리스트 보기",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color21C2EC,
        image = R.drawable.icon_best_wht,
        title = "주간 장르 베스트",
        body = "플랫폼별 주간 베스트 장르 리스트 보기",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )

    ItemMainSettingSingleTablet(
        containerColor = color31C3AE,
        image = R.drawable.icon_best_wht,
        title = "월간 장르 베스트",
        body = "플랫폼별 월간 베스트 장르 리스트 보기",
        settter = setMenu,
        getter = getMenu,
        onClick = {  },
        value = "TODAY_BEST",
    )
}

@Composable
fun ScreenBestDBListNovel(isInit: Boolean = true, type: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
        contentAlignment = if(isInit){
            Alignment.Center
        } else {
            Alignment.TopStart
        }
    ) {
        val context = LocalContext.current
        val dataStore = DataStoreManager(context)

        LazyColumn(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(isInit){
                item {
                    Card(
                        modifier = Modifier
                            .wrapContentSize(),
                        colors = CardDefaults.cardColors(containerColor = colorDCDCDD),
                        shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(90.dp)
                                .width(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(72.dp)
                                    .width(72.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.size(8.dp)) }

                item {
                    Text(
                        text = "마나바라에 기록된 작품들",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = color000000
                    )
                }
            }

            item { Spacer(modifier = Modifier.size(8.dp)) }

            if(type == "NOVEL"){
                itemsIndexed(novelListEng()) { index, item ->
                    Box(modifier = Modifier.padding(16.dp, 8.dp)) {
                        TabletContentWrapBtn(
                            onClick = {},
                            content = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Image(
                                        painter = painterResource(id = getPlatformLogoEng(item)),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )

                                    Spacer(modifier = Modifier.size(8.dp))

                                    getBookCount(context = context, type = type, platform = item)

                                    Text(
                                        text = spannableString(
                                            textFront = "${changePlatformNameKor(item)} : ",
                                            color = color000000,
                                            textEnd = "${dataStore.getDataStoreString(
                                                getPlatformDataKeyNovel(item)
                                            ).collectAsState(initial = "").value ?: "0"} 작품"
                                        ),
                                        color = color20459E,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        )
                    }
                }
            } else {
                itemsIndexed(comicListEng()) { index, item ->
                    Box(modifier = Modifier.padding(16.dp, 8.dp)) {
                        TabletContentWrapBtn(
                            onClick = {},
                            content = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Image(
                                        painter = painterResource(id = getPlatformLogoEng(item)),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )

                                    Spacer(modifier = Modifier.size(8.dp))

                                    getBookCount(context = context, type = type, platform = item)

                                    Text(
                                        text = spannableString(
                                            textFront = "${changePlatformNameKor(item)} : ",
                                            color = color000000,
                                            textEnd = "${dataStore.getDataStoreString(
                                                getPlatformDataKeyComic(item)
                                            ).collectAsState(initial = "").value ?: "0"} 작품"
                                        ),
                                        color = color20459E,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}