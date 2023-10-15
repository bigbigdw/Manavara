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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorABD436
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.getPlatformColor
import com.bigbigdw.manavara.util.getPlatformDescription
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.novelListKor
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain,
    setMenu: (String) -> Unit,
    getMenu: String,
    setDetailPlatform: (String) -> Unit,
    getDetailPlatform: String,
    setDetailType: (String) -> Unit,
    getDetailType: String,
    listState: LazyListState,
    modalSheetState: ModalBottomSheetState? = null,
) {

    viewModelBest.getBestListToday(
        platform = getDetailPlatform,
        type = getDetailType,
    )

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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

                if(getDialogOpen){
                    Dialog(
                        onDismissRequest = { setDialogOpen(false) },
                    ) {
                        AlertTwoBtn(
                            isShow = {  },
                            onFetchClick = { },
                            btnLeft = "취소",
                            btnRight = "확인",
                            modifier = Modifier.requiredWidth(400.dp),
                            contents = {
                                ScreenDialogBest(
                                    item = viewModelBest.state.collectAsState().value.itemBookInfo,
                                )
                            })
                    }
                }

                ScreenBestTabletList(
                    viewModelMain = viewModelMain,
                    setMenu = setMenu,
                    getMenu = getMenu,
                    setDetailPlatform = setDetailPlatform,
                    setDetailType = setDetailType,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen
                ) {}

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
                    listState = listState,
                    setDialogOpen = setDialogOpen
                )

            } else {

                if (getMenu.contains("TODAY")) {
                    ScreenTodayBest(
                        viewModelMain = viewModelMain,
                        viewModelBest = viewModelBest,
                        getDetailType = getDetailType,
                        isExpandedScreen = isExpandedScreen,
                        listState = listState,
                        modalSheetState = modalSheetState,
                        setDialogOpen = null
                    )

                } else if (getMenu.contains("WEEK")) {

                    ScreenTodayWeek(
                        viewModelMain = viewModelMain,
                        viewModelBest = viewModelBest
                    )

                } else if (getMenu.contains("MONTH")) {

                    ScreenTodayMonth(
                        viewModelMain = viewModelMain,
                        viewModelBest = viewModelBest
                    )

                } else if (getMenu.contains("투데이 장르")) {
                    GenreDetailJson(
                        viewModelBest = viewModelBest,
                        getDetailType = getDetailType,
                        menuType = "투데이"
                    )

                } else if (getMenu.contains("주간 장르")) {
                    GenreDetailJson(
                        viewModelBest = viewModelBest,
                        getDetailType = getDetailType,
                        menuType = "주간"
                    )

                } else if (getMenu.contains("월간 장르")) {
                    GenreDetailJson(
                        viewModelBest = viewModelBest,
                        getDetailType = getDetailType,
                        menuType = "월간"
                    )
                } else if (getMenu.contains("베스트 웹소설 DB")) {
                    ScreenBestDBListNovel(isInit = false, type = "NOVEL")
                }  else if (getMenu.contains("베스트 웹툰 DB")) {
                    ScreenBestDBListNovel(isInit = false, type = "COMIC")
                } else {
                    ScreenBestDBListNovel(type = "NOVEL")
                }
            }
        }
    }
}

@Composable
fun ScreenBestTabletList(
    viewModelMain: ViewModelMain,
    setMenu: (String) -> Unit,
    getMenu: String,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit,
    listState: LazyListState,
    isExpandedScreen: Boolean,
    onClick: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

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

        if (isExpandedScreen) {

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
                title = "작품 검색",
                body = "플랫폼과 무관하게 작품 검색 진행",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = color5372DE,
                image = R.drawable.icon_novel_wht,
                title = "마나바라 베스트 웹소설 DB",
                body = "마나바라에 기록된 베스트 웹소설 리스트",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = color998DF9,
                image = R.drawable.icon_webtoon_wht,
                title = "마나바라 베스트 웹툰 DB",
                body = "마나바라에 기록된 웹툰 웹툰 리스트",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

        } else {

            ItemMainSettingSingleTablet(
                containerColor = color4AD7CF,
                image = R.drawable.icon_setting_wht,
                title = "작품 검색",
                body = "플랫폼과 무관하게 작품 검색 진행",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = color5372DE,
                image = R.drawable.icon_novel_wht,
                title = "마나바라 베스트 웹소설 DB",
                body = "마나바라에 기록된 베스트 웹소설 리스트",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = color998DF9,
                image = R.drawable.icon_webtoon_wht,
                title = "마나바라 베스트 웹툰 DB",
                body = "마나바라에 기록된 웹툰 웹툰 리스트",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = colorF17FA0,
                image = R.drawable.icon_best_wht,
                title = "투데이 장르 베스트",
                body = "플랫폼별 투데이 베스트 장르 리스트 보기",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = color21C2EC,
                image = R.drawable.icon_best_wht,
                title = "주간 장르 베스트",
                body = "플랫폼별 주간 베스트 장르 리스트 보기",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            ItemMainSettingSingleTablet(
                containerColor = color31C3AE,
                image = R.drawable.icon_best_wht,
                title = "월간 장르 베스트",
                body = "플랫폼별 월간 베스트 장르 리스트 보기",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )
        }

        TabletBorderLine()

        if (isExpandedScreen) {
            ItemMainSettingSingleTablet(
                containerColor = colorF17FA0,
                image = R.drawable.icon_best_wht,
                title = "투데이 장르 베스트",
                body = "플랫폼별 투데이 베스트 장르 리스트 보기",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )
        }

        novelListKor().forEachIndexed { index, item ->
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
                    coroutineScope.launch {
                        listState.scrollToItem(index = 0)
                    }
                    onClick()
                },

                )
        }

        if (isExpandedScreen) {
            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color21C2EC,
                image = R.drawable.icon_best_wht,
                title = "주간 장르 베스트",
                body = "플랫폼별 주간 베스트 장르 리스트 보기",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            novelListKor().forEachIndexed { index, item ->
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
                containerColor = color31C3AE,
                image = R.drawable.icon_best_wht,
                title = "월간 장르 베스트",
                body = "플랫폼별 월간 베스트 장르 리스트 보기",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = { onClick() },
            )

            novelListKor().forEachIndexed { index, item ->
                ItemBestListSingle(
                    containerColor = getPlatformColor(item),
                    image = getPlatformLogo(item),
                    title = item,
                    body = getPlatformDescription(item),
                    setMenu = setMenu,
                    getMenu = getMenu,
                    bestType = "MONTH_BEST",
                    setDetailPlatform = { setDetailPlatform(changePlatformNameEng(item)) },
                    setDetailType = {
                        setDetailType("NOVEL")
                        onClick()
                    },
                )
            }
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

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenBestDetail(
    getMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailType: String,
    viewModelBest: ViewModelBest,
    isExpandedScreen: Boolean,
    listState: LazyListState,
    setDialogOpen: (Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if(getMenu != "TODAY"){
            Spacer(modifier = Modifier.size(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
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
        }

        if (getMenu.contains("TODAY_BEST")) {

            Spacer(modifier = Modifier.size(16.dp))

            ScreenTodayBest(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                getDetailType = getDetailType,
                isExpandedScreen = isExpandedScreen,
                listState = listState,
                modalSheetState = null,
                setDialogOpen = setDialogOpen
            )

        } else if (getMenu.contains("WEEK_BEST")) {

            Spacer(modifier = Modifier.size(16.dp))

            ScreenTodayWeek(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest
            )

        } else if (getMenu.contains("MONTH_BEST")) {

            Spacer(modifier = Modifier.size(16.dp))

            ScreenTodayMonth(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest
            )

        } else if (getMenu.contains("투데이 장르")) {
            GenreDetailJson(
                viewModelBest = viewModelBest,
                getDetailType = getDetailType,
                menuType = "투데이"
            )

        } else if (getMenu.contains("주간 장르")) {
            GenreDetailJson(
                viewModelBest = viewModelBest,
                getDetailType = getDetailType,
                menuType = "주간"
            )

        } else if (getMenu.contains("월간 장르")) {
            GenreDetailJson(
                viewModelBest = viewModelBest,
                getDetailType = getDetailType,
                menuType = "월간"
            )

        } else if (getMenu.contains("베스트 웹소설 DB")) {
            ScreenBestDBListNovel(isInit = false, type = "NOVEL")
        }  else if (getMenu.contains("베스트 웹툰 DB")) {
            ScreenBestDBListNovel(isInit = false, type = "COMIC")
        } else {
            ScreenBestDBListNovel(type = "NOVEL")
        }
    }
}
