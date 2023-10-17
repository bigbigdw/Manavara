package com.bigbigdw.manavara.main.screen

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.bigbigdw.manavara.main.models.ItemKeyword
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color536FD2
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color64C157
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorABD436
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.comicListKor
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformColor
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformDescription
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.manavaraListKor
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.novelListKor
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import getBookCount
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenManavara(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain,
    setMenu: (String) -> Unit,
    getMenu: String,
    setPlatform: (String) -> Unit,
    getPlatform: String,
    getType: String,
    listState: LazyListState,
    setBestType: (String) -> Unit,
    getBestType: String,
    modalSheetState: ModalBottomSheetState? = null,
) {

    LaunchedEffect(getPlatform,getType){
        viewModelBest.getBestListToday(
            platform = getPlatform,
            type = getType,
        )

        viewModelBest.getBestWeekTrophy(
            platform = getPlatform,
            type = getType,
        )

        viewModelBest.getBestMapToday(
            platform = getPlatform,
            type = getType,
        )

        viewModelBest.getBestWeekList(
            platform = getPlatform,
            type = getType,
        )

        viewModelBest.getBestMonthTrophy(
            platform = getPlatform,
            type = getType,
        )

        viewModelBest.getBestMonthList(
            platform = getPlatform,
            type = getType,
        )
    }

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
                                    trophy = viewModelBest.state.collectAsState().value.itemBestInfoTrophyList,
                                    isExpandedScreen = isExpandedScreen
                                )
                            })
                    }
                }

                ScreenManavaraPropertyList(
                    setMenu = setMenu,
                    getMenu = getMenu,
                    setPlatform = setPlatform,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                    setBestType = setBestType,
                    getBestType = getBestType,
                    getType = getType
                ) {}

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenManavaDetail(
                    getMenu = getMenu,
                    viewModelMain = viewModelMain,
                    getType = getType,
                    viewModelBest = viewModelBest,
                    isExpandedScreen = isExpandedScreen,
                    listState = listState,
                    setDialogOpen = setDialogOpen,
                    getBestType = getBestType,
                    getPlatform = getPlatform
                )

            } else {

                ScreenManavaraItemDetail(
                    isExpandedScreen = isExpandedScreen,
                    viewModelBest = viewModelBest,
                    viewModelMain = viewModelMain,
                    getPlatform = getPlatform,
                    getType = getType,
                    getMenu = getMenu,
                    listState = listState,
                    getBestType = getBestType,
                    setDialogOpen = null,
                    modalSheetState = modalSheetState
                )
            }
        }
    }
}

@Composable
fun ScreenManavaraPropertyList(
    setMenu: (String) -> Unit,
    getMenu: String,
    setPlatform: (String) -> Unit,
    listState: LazyListState,
    isExpandedScreen: Boolean,
    setBestType: (String) -> Unit,
    getBestType: String,
    getType: String,
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

        Column {
            Spacer(modifier = Modifier.size(16.dp))

            LaunchedEffect(getBestType){
                setPlatform("JOARA")
            }

            Text(
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = "마나바라 스페셜",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight(weight = 700)
            )

            ItemMainSettingSingleTablet(
                containerColor = color4AD7CF,
                image = R.drawable.icon_novel_wht,
                title = "마나바라 베스트 웹소설 DB",
                body = "마나바라에 기록된 베스트 웹소설 리스트",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "베스트 웹소설 DB"
            )

            ItemMainSettingSingleTablet(
                containerColor = color5372DE,
                image = R.drawable.icon_novel_wht,
                title = "투데이 장르 베스트",
                body = "플랫폼별 투데이 베스트 장르 리스트 보기",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹소설 투데이 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color998DF9,
                image = R.drawable.icon_novel_wht,
                title = "주간 장르 베스트",
                body = "플랫폼별 주간 베스트 장르 리스트 보기",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹소설 주간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorEA927C,
                image = R.drawable.icon_novel_wht,
                title = "월간 장르 베스트",
                body = "플랫폼별 월간 베스트 장르 리스트 보기",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹소설 월간 장르"
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = colorABD436,
                image = R.drawable.icon_webtoon_wht,
                title = "마나바라 베스트 웹툰 DB",
                body = "마나바라에 기록된 웹툰 웹툰 리스트",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "베스트 웹툰 DB"
            )

            ItemMainSettingSingleTablet(
                containerColor = colorF17FA0,
                image = R.drawable.icon_webtoon_wht,
                title = "투데이 웹툰 장르 베스트",
                body = "플랫폼별 웹툰 베스트 장르 리스트 보기",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹툰 투데이 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color21C2EC,
                image = R.drawable.icon_webtoon_wht,
                title = "주간 웹툰 장르 베스트",
                body = "플랫폼별 웹툰 베스트 장르 리스트 보기",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹툰 주간 장르"
            )

            ItemMainSettingSingleTablet(
                containerColor = color31C3AE,
                image = R.drawable.icon_webtoon_wht,
                title = "월간 웹툰 장르 베스트",
                body = "플랫폼별 월간 웹툰 베스트 장르 리스트 보기",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹툰 월간 장르"
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.icon_search_wht,
                title = "작품 검색",
                body = "플랫폼과 무관하게 작품 검색 진행",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color64C157,
                image = R.drawable.icon_search_wht,
                title = "북코드 검색",
                body = "플랫폼과 무관하게 작품 검색 진행",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "북코드 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = colorF17666,
                image = R.drawable.icon_search_wht,
                title = "웹소설 DB 검색",
                body = "웹소설 DB 검색",
                settter = setMenu,
                getter = getMenu,
                onClick = {  },
                value = "웹소설 DB 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color536FD2,
                image = R.drawable.icon_search_wht,
                title = "웹툰 DB 검색",
                body = "웹툰 DB 검색",
                settter = setMenu,
                getter = getMenu,
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
    viewModelBest: ViewModelBest,
    getDetailType: String,
    menuType: String
) {

    val (getPlatform, setPlatform) = remember { mutableStateOf("JOARA") }

    LaunchedEffect(menuType, getPlatform) {
        when (menuType) {
            "투데이" -> {
                viewModelBest.getJsonGenreList(platform = getPlatform, type = getDetailType)
            }
            "주간" -> {
                viewModelBest.getJsonGenreWeekList(
                    platform = getPlatform,
                    type = getDetailType
                )
            }
            else -> {
                viewModelBest.getJsonGenreMonthList(
                    platform = getPlatform,
                    type = getDetailType
                )
            }
        }
    }

    val state = viewModelBest.state.collectAsState().value

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenManavaraItemDetail(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain,
    getPlatform: String,
    getType: String,
    listState: LazyListState,
    getBestType: String,
    getMenu: String,
    modalSheetState: ModalBottomSheetState? = null,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    if (getMenu.contains("베스트 웹소설 DB")) {
        ScreenBestDBListNovel(isInit = false, type = "NOVEL")
    } else if (getMenu.contains("베스트 웹툰 DB")) {
        ScreenBestDBListNovel(isInit = false, type = "COMIC")
    } else if (getMenu.contains("웹소설 투데이 장르")) {
        GenreDetailJson(
            viewModelBest = viewModelBest,
            getDetailType = "NOVEL",
            menuType = "투데이"
        )

    } else if (getMenu.contains("웹소설 주간 장르")) {
        GenreDetailJson(
            viewModelBest = viewModelBest,
            getDetailType = "NOVEL",
            menuType = "주간"
        )

    } else if (getMenu.contains("웹소설 월간 장르")) {
        GenreDetailJson(
            viewModelBest = viewModelBest,
            getDetailType = "NOVEL",
            menuType = "월간"
        )
    } else if (getMenu.contains("웹툰 투데이 장르")) {
        GenreDetailJson(
            viewModelBest = viewModelBest,
            getDetailType = "COMIC",
            menuType = "투데이"
        )

    } else if (getMenu.contains("웹툰 주간 장르")) {
        GenreDetailJson(
            viewModelBest = viewModelBest,
            getDetailType = "COMIC",
            menuType = "주간"
        )

    } else if (getMenu.contains("웹툰 월간 장르")) {
        GenreDetailJson(
            viewModelBest = viewModelBest,
            getDetailType = "COMIC",
            menuType = "월간"
        )
    } else {
        ScreenBestDBListNovel(type = "NOVEL")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenManavaDetail(
    getMenu: String,
    viewModelMain: ViewModelMain,
    getType: String,
    viewModelBest: ViewModelBest,
    isExpandedScreen: Boolean,
    listState: LazyListState,
    setDialogOpen: (Boolean) -> Unit,
    getBestType: String,
    getPlatform: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if(manavaraListKor().contains(getMenu)){
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

        ScreenManavaraItemDetail(
            isExpandedScreen = isExpandedScreen,
            viewModelBest = viewModelBest,
            viewModelMain = viewModelMain,
            getPlatform = getPlatform,
            getType = getType,
            getMenu = getMenu,
            listState = listState,
            getBestType = getBestType,
            setDialogOpen = setDialogOpen,
            modalSheetState = null
        )
    }
}