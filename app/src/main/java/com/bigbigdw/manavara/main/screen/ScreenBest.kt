package com.bigbigdw.manavara.main.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.ui.theme.colorea927C
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.getPlatformColor
import com.bigbigdw.manavara.util.getPlatformDescription
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.novelListKor
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain
) {

    val (getMenu, setMenu) = remember { mutableStateOf("") }
    val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf(novelListEng()[0]) }
    val (getDetailType, setDetailType) = remember { mutableStateOf("NOVEL") }
    val listState = rememberLazyListState()

    LaunchedEffect(getDetailPlatform){
//        viewModelBest.getBestListToday(
//            platform = getDetailPlatform,
//            type = getDetailType,
//        )

        viewModelBest.getBestWeekTrophy(
            platform = getDetailPlatform,
            type = getDetailType,
        )

        viewModelBest.getBestMapToday(
            platform = getDetailPlatform,
            type = getDetailType,
        )

        viewModelBest.getBestWeekList(
            platform = getDetailPlatform,
            type = getDetailType,
        )

        viewModelBest.getBestMonthTrophy(
            platform = getDetailPlatform,
            type = getDetailType,
        )

        viewModelBest.getBestMonthList(
            platform = getDetailPlatform,
            type = getDetailType,
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {
                ScreenBestTabletList(
                    setMenu = setMenu,
                    getMenu = getMenu,
                    viewModelMain = viewModelMain,
                    setDetailPlatform = setDetailPlatform,
                    setDetailType = setDetailType,
                    listState = listState
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenBestDetail(
                    getMenu = getMenu,
                    viewModelMain = viewModelMain,
                    getDetailPlatform = getDetailPlatform,
                    getDetailType = getDetailType,
                    viewModelBest = viewModelBest,
                    isExpandedScreen = isExpandedScreen,
                    listState = listState
                )

            } else {
                ScreenTodayBest(
                    viewModelMain = viewModelMain,
                    viewModelBest = viewModelBest,
                    getDetailPlatform = getDetailPlatform,
                    getDetailType = getDetailType,
                    isExpandedScreen = isExpandedScreen,
                    listState = listState
                )
            }
        }
    }
}

@Composable
fun ScreenBestTabletList(
    setMenu: (String) -> Unit,
    getMenu: String,
    viewModelMain: ViewModelMain,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
    listState: LazyListState
) {

    Column(
        modifier = Modifier
            .width(330.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "웹소설 베스트",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_setting_wht,
            title = "유저 옵션",
            body = "뷰모드 전환 및 플랫폼 선택",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_setting_wht,
            title = "작품 검색",
            body = "플랫폼과 무관하게 작품 검색 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_best_wht,
            title = "마나바라 투데이 베스트",
            body = "플랫폼별 1~5위에 랭크된 작품 리스트",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        novelListKor().forEachIndexed{ index, item ->
            ItemBestListSingle(
                containerColor = getPlatformColor(item),
                image = getPlatformLogo(item),
                title = item,
                body = getPlatformDescription(item),
                setMenu = setMenu,
                getMenu = getMenu,
                bestType = "TODAY_BEST",
                setDetailPlatform = { setDetailPlatform(changePlatformNameEng(item)) },
                setDetailType = {
                    setDetailType("NOVEL")
//                    CoroutineScope(Dispatchers.IO).launch {
//                        listState.animateScrollToItem(index = 0)
//                    }

                },

            )
        }

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_best_wht,
            title = "주간 베스트",
            body = "주간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        novelListKor().forEachIndexed{ index, item ->
            ItemBestListSingle(
                containerColor = getPlatformColor(item),
                image = getPlatformLogo(item),
                title = item,
                body = getPlatformDescription(item),
                setMenu = setMenu,
                getMenu = getMenu,
                bestType = "WEEK_BEST",
                setDetailPlatform = { setDetailPlatform(changePlatformNameEng(item)) },
                setDetailType = { setDetailType("NOVEL") },
                )
        }

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorea927C,
            image = R.drawable.icon_best_wht,
            title = "월간 베스트",
            body = "월간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        novelListKor().forEachIndexed{ index, item ->
            ItemBestListSingle(
                containerColor = getPlatformColor(item),
                image = getPlatformLogo(item),
                title = item,
                body = getPlatformDescription(item),
                setMenu = setMenu,
                getMenu = getMenu,
                bestType = "MONTH_BEST",
                setDetailPlatform = { setDetailPlatform(changePlatformNameEng(item)) },
                setDetailType = { setDetailType("NOVEL") },
            )
        }
    }
}

@Composable
fun ItemBestListSingle(
    containerColor: Color,
    image: Int,
    title: String,
    body: String,
    setMenu: (String) -> Unit,
    getMenu: String,
    bestType: String,
    setDetailPlatform: () -> Unit,
    setDetailType: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (getMenu == "$bestType $title") {
                colorE9E9E9
            } else {
                colorF7F7F7
            }
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            setMenu("$bestType $title")
            setDetailPlatform()
            setDetailType()
        },
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 6.dp,
            end = 12.dp,
            bottom = 6.dp,
        ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    backgroundColor = containerColor,
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .height(28.dp)
                                .width(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Row {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = color000000,

                            fontWeight = FontWeight(weight = 500)
                        )
                    }

                    Row {
                        Text(
                            text = body,
                            fontSize = 14.sp,
                            color = color8E8E8E,
                        )
                    }
                }
            }
        })
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenBestDetail(
    getMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailType: String,
    viewModelBest: ViewModelBest,
    isExpandedScreen: Boolean,
    listState: LazyListState
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = null,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )

            Text(
                modifier = Modifier
                    .padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = changeDetailNameKor(getMenu),
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        if (getMenu.contains("TODAY_BEST")) {
            ScreenTodayBest(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                isExpandedScreen = isExpandedScreen,
                listState =  listState
            )

        } else if (getMenu.contains("WEEK_BEST")) {
            ScreenTodayWeek(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest
            )

        }  else if (getMenu.contains("MONTH_BEST")) {
            ScreenTodayMonth(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest
            )

        } else {
            ScreenTest()
        }
    }
}
