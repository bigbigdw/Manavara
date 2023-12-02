package com.bigbigdw.manavara.analyze.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4996E8
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color536FD2
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color64C157
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.color80BF78
import com.bigbigdw.manavara.ui.theme.color91CEC7
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorABD436
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.ui.theme.colorFDC24E
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.manavaraListKor
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import getBookCount
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenAnalyze(
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState? = null,
    drawerState: DrawerState,
    viewModelAnalyze: ViewModelAnalyze,
    currentRoute: String?,
) {

    val context = LocalContext.current

    val state = viewModelAnalyze.state.collectAsState().value

    LaunchedEffect(state.platform, state.type) {
        viewModelAnalyze.getBestListTodayStorage(
            context = context,
        )

        viewModelAnalyze.getBestWeekTrophy()
        viewModelAnalyze.getBestWeekListStorage(context)
        viewModelAnalyze.getBestMonthTrophy()
        viewModelAnalyze.getBestMonthListStorage(context)
    }

    val coroutineScope = rememberCoroutineScope()

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
                                if (modalSheetState != null) {
                                    ScreenDialogBest(
                                        item = viewModelAnalyze.state.collectAsState().value.itemBookInfo,
                                        trophy = viewModelAnalyze.state.collectAsState().value.itemBestInfoTrophyList,
                                        isExpandedScreen = isExpandedScreen,
                                        currentRoute = "NOVEL",
                                        modalSheetState = modalSheetState
                                    )
                                }
                            })
                    }
                }

                ScreenManavaraPropertyList(
                    viewModelAnalyze = viewModelAnalyze,
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenManavaDetail(
                    viewModelAnalyze = viewModelAnalyze
                )

            } else {

                val modalSheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
                    skipHalfExpanded = false
                )

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenManavaraPropertyList(
                        viewModelAnalyze = viewModelAnalyze,
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenManavaraTopbar(
                                viewModelAnalyze = viewModelAnalyze,
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                })
                        },

                        ) {
                        Box(
                            Modifier
                                .padding(it)
                                .background(color = colorF6F6F6)
                                .fillMaxSize()
                        ) {
                            ScreenManavaraItemDetail(
                                viewModelAnalyze = viewModelAnalyze
                            )
                        }
                    }

                    ModalBottomSheetLayout(
                        sheetState = modalSheetState,
                        sheetElevation = 50.dp,
                        sheetShape = RoundedCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp
                        ),
                        sheetContent = {

//                            if(currentRoute == "NOVEL" || currentRoute == "COMIC"){
//
//                                Spacer(modifier = Modifier.size(4.dp))
//
//                                ScreenDialogBest(
//                                    item = state.itemBookInfo,
//                                    trophy = state.itemBestInfoTrophyList,
//                                    isExpandedScreen = isExpandedScreen,
//                                    currentRoute = currentRoute,
//                                    modalSheetState = modalSheetState
//                                )
//                            } else {
//                                ScreenTest()
//                            }
                        },
                    ) {}
                }

                BackOnPressedMobile(modalSheetState = modalSheetState)

            }
        }
    }
}

@Composable
fun ScreenManavaraPropertyList(
    viewModelAnalyze: ViewModelAnalyze,
) {

    val state = viewModelAnalyze.state.collectAsState().value

    Column(
        modifier = Modifier
            .width(330.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Column {
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = "마나바라 분석",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight(weight = 700)
            )

            ItemMainSettingSingleTablet(
                containerColor = color4AD7CF,
                image = R.drawable.icon_novel_wht,
                title = "마나바라 베스트 웹소설 DB",
                body = "마나바라에 기록된 베스트 웹소설 리스트",
                current = state.menu,
                onClick = {  },
                value = "베스트 웹소설 DB"
            )

            ItemMainSettingSingleTablet(
                containerColor = color5372DE,
                image = R.drawable.icon_genre_wht,
                title = "투데이 장르 베스트",
                body = "플랫폼별 투데이 베스트 장르 리스트 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 투데이 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color998DF9,
                image = R.drawable.icon_genre_wht,
                title = "주간 장르 베스트",
                body = "플랫폼별 주간 베스트 장르 리스트 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 주간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorEA927C,
                image = R.drawable.icon_genre_wht,
                title = "월간 장르 베스트",
                body = "플랫폼별 월간 베스트 장르 리스트 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorABD436,
                image = R.drawable.icon_keyword_wht,
                title = "웹소설 투데이 키워드 베스트",
                body = "웹소설 월간 키워드 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorF17FA0,
                image = R.drawable.icon_keyword_wht,
                title = "웹소설 주간 키워드 베스트",
                body = "웹소설 월간 키워드 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color21C2EC,
                image = R.drawable.icon_keyword_wht,
                title = "웹소설 월간 키워드 베스트",
                body = "웹소설 월간 키워드 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color31C3AE,
                image = R.drawable.icon_webtoon_wht,
                title = "마나바라 베스트 웹툰 DB",
                body = "마나바라에 기록된 웹툰 웹툰 리스트",
                current = state.menu,
                onClick = {  },
                value = "베스트 웹툰 DB"
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.icon_genre_wht,
                title = "투데이 웹툰 장르 베스트",
                body = "플랫폼별 웹툰 베스트 장르 리스트 보기",
                current = state.menu,
                onClick = {  },
                value = "웹툰 투데이 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color64C157,
                image = R.drawable.icon_genre_wht,
                title = "주간 웹툰 장르 베스트",
                body = "플랫폼별 웹툰 베스트 장르 리스트 보기",
                current = state.menu,
                onClick = {  },
                value = "웹툰 주간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorF17666,
                image = R.drawable.icon_genre_wht,
                title = "월간 웹툰 장르 베스트",
                body = "플랫폼별 월간 웹툰 베스트 장르 리스트 보기",
                current = state.menu,
                onClick = {  },
                value = "웹툰 월간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color536FD2,
                image = R.drawable.icon_keyword_wht,
                title = "웹툰 투데이 키워드 베스트",
                body = "웹툰 월간 키워드 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color4996E8,
                image = R.drawable.icon_keyword_wht,
                title = "웹툰 주간 키워드 베스트",
                body = "웹툰 월간 키워드 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorFDC24E,
                image = R.drawable.icon_keyword_wht,
                title = "웹툰 월간 키워드 베스트",
                body = "웹툰 월간 키워드 보기",
                current = state.menu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color80BF78,
                image = R.drawable.icon_novel_wht,
                title = "웹소설 DB 검색",
                body = "웹소설 DB 검색",
                current = state.menu,
                onClick = {  },
                value = "웹소설 DB 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color91CEC7,
                image = R.drawable.icon_webtoon_wht,
                title = "웹툰 DB 검색",
                body = "웹툰 DB 검색",
                current = state.menu,
                onClick = {  },
                value = "웹툰 DB 검색",
            )
        }
    }
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

@Composable
fun GenreDetailJson(
    getDetailType: String,
    menuType: String,
    viewModelAnalyze: ViewModelAnalyze
) {

    val (getPlatform, setPlatform) = remember { mutableStateOf("JOARA") }

    LaunchedEffect(menuType, getPlatform) {
        when (menuType) {
            "투데이" -> {
                viewModelAnalyze.getJsonGenreList(platform = getPlatform, type = getDetailType)
            }
            "주간" -> {
                viewModelAnalyze.getJsonGenreWeekList(
                    platform = getPlatform,
                    type = getDetailType
                )
            }
            else -> {
                viewModelAnalyze.getJsonGenreMonthList(
                    platform = getPlatform,
                    type = getDetailType
                )
            }
        }
    }

    val state = viewModelAnalyze.state.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        LazyRow {
            itemsIndexed(genreListEng()) { index, item ->
                Box {
                    ScreenItemKeyword(
                        getter = getPlatform,
                        setter = setPlatform,
                        title =  changePlatformNameKor(item),
                        getValue = item
                    )
                }
            }
        }

        LazyColumn(modifier = Modifier.padding(0.dp, 16.dp)) {
            itemsIndexed(state.genreDay) { index, item ->
                ListGenreToday(
                    itemBestKeyword = item,
                    index = index
                )
            }
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ScreenItemKeyword(
    getter: String,
    setter: (String) -> Unit,
    title: String,
    getValue: String
) {

    Card(
        modifier = if (getter == getValue) {
            Modifier.border(2.dp, color20459E, CircleShape)
        } else {
            Modifier.border(2.dp, colorF7F7F7, CircleShape)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (getter == getValue) {
                color20459E
            } else {
                Color.White
            }
        ),
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp, 8.dp)
                .clickable {
                    setter(getValue)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = getPlatformLogo(title)),
                contentDescription = null,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                textAlign = TextAlign.Left,
                color = if (getter == getValue) {
                    Color.White
                } else {
                    Color.Black
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ListGenreToday(
    itemBestKeyword: ItemKeyword,
    index: Int,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = {},
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 0.dp,
            ),
            content = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${index + 1} ",
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(16.dp, 0.dp, 0.dp, 0.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Left,
                        color = color20459E,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        maxLines = 1,
                        text = itemBestKeyword.title,
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = itemBestKeyword.value,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        color = color1CE3EE,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            })
    }
}

@Composable
fun ScreenManavaraItemDetail(
    viewModelAnalyze: ViewModelAnalyze,
) {

    val state = viewModelAnalyze.state.collectAsState().value

    if (state.menu.contains("베스트 웹소설 DB")) {
        ScreenBestDBListNovel(isInit = false, type = "NOVEL")
    } else if (state.menu.contains("베스트 웹툰 DB")) {
        ScreenBestDBListNovel(isInit = false, type = "COMIC")
    } else if (state.menu.contains("웹소설 투데이 장르")) {
        GenreDetailJson(
            viewModelAnalyze = viewModelAnalyze,
            getDetailType = "NOVEL",
            menuType = "투데이"
        )

    } else if (state.menu.contains("웹소설 주간 장르")) {
        GenreDetailJson(
            getDetailType = "NOVEL",
            menuType = "주간",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹소설 월간 장르")) {
        GenreDetailJson(
            getDetailType = "NOVEL",
            menuType = "월간",
            viewModelAnalyze = viewModelAnalyze
        )
    } else if (state.menu.contains("웹툰 투데이 장르")) {
        GenreDetailJson(
            getDetailType = "COMIC",
            menuType = "투데이",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹툰 주간 장르")) {
        GenreDetailJson(
            getDetailType = "COMIC",
            menuType = "주간",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹툰 월간 장르")) {
        GenreDetailJson(
            getDetailType = "COMIC",
            menuType = "월간",
            viewModelAnalyze = viewModelAnalyze
        )
    } else {
        ScreenBestDBListNovel(type = "NOVEL")
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenManavaDetail(
    viewModelAnalyze: ViewModelAnalyze
) {

    val state = viewModelAnalyze.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if(manavaraListKor().contains(state.menu)){
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
                    text = changeDetailNameKor(state.menu),
                    fontSize = 24.sp,
                    color = color000000,
                    fontWeight = FontWeight(weight = 700)
                )
            }
        }

        ScreenManavaraItemDetail(
            viewModelAnalyze = viewModelAnalyze
        )
    }
}

@Composable
fun ScreenManavaraTopbar(viewModelAnalyze: ViewModelAnalyze, onClick: () -> Unit){
    val state = viewModelAnalyze.state.collectAsState().value

    Row(
        Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onClick() }) {
                Image(
                    painter = painterResource(id = R.drawable.icon_drawer),
                    contentDescription = null,
                    modifier = Modifier
                        .width(22.dp)
                        .height(22.dp)
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text(
                    text = "마나바라",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )

            }

        }


    }
}