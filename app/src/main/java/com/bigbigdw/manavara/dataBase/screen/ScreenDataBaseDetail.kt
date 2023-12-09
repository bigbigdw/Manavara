package com.bigbigdw.manavara.dataBase.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.dataBase.getJsonGenreMonthList
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDataBaseDetail
import com.bigbigdw.manavara.best.getBookItemWeekTrophyDialog
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.screen.ItemBestDetailInfoAnalyze
import com.bigbigdw.manavara.best.screen.ListBest
import com.bigbigdw.manavara.dataBase.getGenreMap
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.dataBase.getJsonGenreWeekList
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import com.bigbigdw.manavara.util.weekListAll
import convertDateStringWeek
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAnalyzeDetail(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    widthSizeClass: WindowWidthSizeClass,
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val state = viewModelDataBaseDetail.state.collectAsState().value
    val context = LocalContext.current

    DisposableEffect(context) {

        getJsonFiles(
            platform = state.platform,
            type = state.type,
            root = "BEST_WEEK",
        ) {
            viewModelDataBaseDetail.setJsonNameList(it)

            viewModelDataBaseDetail.setScreen(
                menu = "${convertDateStringWeek(it.get(0))} 장르",
                key = it[0]
            )
        }

        if (state.mode == "GENRE_BOOK") {
            getBookMap(
                platform = state.platform,
                type = state.type
            ) {
                viewModelDataBaseDetail.setItemBookInfoMap(it)
            }
        } else {
            getGenreMap(
                platform = state.platform,
                type = state.type,
            ) { monthList ->
                viewModelDataBaseDetail.setGenreMap(itemGenreMap = monthList)
            }
        }

        getJsonGenreMonthList(
            platform = state.platform,
            type = state.type,
            root = state.json
        ) { monthList, list ->
            viewModelDataBaseDetail.setGenreList(genreList = list, genreMonthList = monthList)
        }

        onDispose {}
    }


    if (isExpandedScreen) {

        ScreenTabletAnalyzeDetail(
            viewModelDataBaseDetail = viewModelDataBaseDetail,
        )

//        BestDetailBackOnPressed(
//            getMenu = getMenu,
//            setMenu = setMenu
//        )

    } else {

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

            ScreenAnalyzeDetailPropertyList(
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )

        }) {
            Scaffold(
                topBar = {
//                    TopbarBestDetail(
//                        setter = setMenu,
//                        getter = getMenu,
//                        setDrawer = {
//                            coroutineScope.launch {
//                                drawerState.open()
//                            }
//                        })
                }
            ) {

                Box(
                    Modifier
                        .padding(it)
                        .background(color = colorF6F6F6)
                        .fillMaxSize()
                ) {

                    ScreenAnalyzeDetailPropertyList(
                        viewModelDataBaseDetail = viewModelDataBaseDetail
                    )
                }

            }
        }

    }
}

@Composable
fun ScreenAnalyzeDetailPropertyList(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDataBaseDetail.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .width(400.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {
        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            Text(
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = state.title,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight(weight = 700)
            )
        }

        item {
            Spacer(modifier = Modifier.size(16.dp))
        }

        itemsIndexed(state.jsonNameList) { index, item ->
            ItemMainSettingSingleTablet(
                containerColor = colorList.get(index),
                image = R.drawable.icon_genre_wht,
                title = "${convertDateStringWeek(item)} 장르",
                body = "${convertDateStringWeek(item)} 장르 일별로 보기",
                current = "${convertDateStringWeek(item)} 장르",
                onClick = {
                    coroutineScope.launch {
                        viewModelDataBaseDetail.setScreen(
                            menu = "${convertDateStringWeek(item)} 장르",
                            key = item
                        )
                    }
                },
                value = state.menu,
            )
        }

        item {
            TabletBorderLine()
        }

        itemsIndexed(state.genreList) { index, item ->
            ItemMainSettingSingleTablet(
                containerColor = colorList.get(index),
                image = R.drawable.icon_genre_wht,
                title = if (state.mode == "GENRE_BOOK") {
                    "${item.title} 작품 리스트"
                } else {
                    "${item.title} 변동 현황"
                },
                body = if (state.mode == "GENRE_BOOK") {
                    "${state.title}이 포함된 작품 확인"
                } else {
                    "${item.title}가 사용된 날짜 확인"
                },
                current = if (state.mode == "GENRE_BOOK") {
                    "${item.title} 작품 리스트"
                } else {
                    "${item.title} 변동 현황"
                },
                onClick = {
                    coroutineScope.launch {
                        viewModelDataBaseDetail.setScreen(
                            menu = if (state.mode == "GENRE_BOOK") {
                                "${item.title} 작품 리스트"
                            } else {
                                "${item.title} 변동 현황"
                            }, key = item.title
                        )
                    }
                },
                value = state.menu,
            )
        }
    }
}

@Composable
fun ScreenTabletAnalyzeDetail(
    viewModelDataBaseDetail: ViewModelDataBaseDetail
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            ScreenAnalyzeDetailPropertyList(
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )

            Spacer(modifier = Modifier.size(16.dp))

            ScreenAnalyzeDetailItem(
                viewModelDataBaseDetail = viewModelDataBaseDetail,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeDetailItem(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
    ) {

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
                text = state.menu,
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )
        }

        if (state.menu.contains("작품 리스트")) {

            ScreenGenreBooks(
                modalSheetState = null,
                setDialogOpen = null,
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )

        } else if (state.menu.contains("주차 장르")) {

            ScreenGenreStatusWeekly(
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )

        }else if (state.menu.contains("변동 현황")) {

            ScreenGenreStatus(
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenGenreBooks(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        val filteredList = ArrayList<ItemBookInfo>()

        for (trophyItem in state.itemBookInfoMap.values) {
            val bookCode = trophyItem.bookCode
            val bookInfo = state.itemBookInfoMap[bookCode]

            if (bookInfo != null) {
                filteredList.add(bookInfo)
            }
        }

        val filteredMap = state.itemBookInfoMap.filter { it.value.genre == state.key }

        itemsIndexed(ArrayList(filteredMap.values)) { index, item ->
            ListBest(
                item = item,
                type = "MONTH",
                index = index,
            ) {
                coroutineScope.launch {
                    viewModelDataBaseDetail.setItemBookInfo(itemBookInfo = item)

                    getBookItemWeekTrophyDialog(
                        itemBookInfo = item,
                        type = state.type,
                        platform = state.platform
                    ) { itemBookInfo, itemBestInfoTrophyList ->
                        viewModelDataBaseDetail.setItemBestInfoTrophyList(
                            itemBookInfo = itemBookInfo,
                            itemBestInfoTrophyList = itemBestInfoTrophyList
                        )
                    }

                    modalSheetState?.show()

                    if (setDialogOpen != null) {
                        setDialogOpen(true)
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenGenreStatus(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        val list = state.itemGenreMap[state.key] ?: ArrayList()

        item{
            TabletContentWrap {
                list.forEachIndexed { index, item ->

                    val year = item.date.substring(0, 4)
                    val month = item.date.substring(4, 6)
                    val day = item.date.substring(6, 8)

                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = item.value,
                        beforeValue = if (index == 0) {
                            "-"
                        } else {
                            list[index - 1].value
                        },
                        type = "선호작 분석",
                        isLast = index == list.size - 1
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenGenreStatusWeekly(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value
    val (getDate, setDate) = remember { mutableStateOf("전체") }

    LaunchedEffect(state.key){
        getJsonGenreWeekList(
            platform = state.platform,
            type = state.type,
            root = state.key
        ) { genreWeekList, genreList ->
            viewModelDataBaseDetail.setGenreList(genreMonthList = genreWeekList, genreList = genreList)
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(8.dp)) }

        item{
            LazyRow(
                modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
            ) {

                itemsIndexed(weekListAll()) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = getDate,
                            onClick = {
                                setDate(item)
                            },
                            title = item,
                            getValue = item
                        )
                    }
                }
            }
        }

        if (getDate == "전체") {

            itemsIndexed(state.genreList) { index, item ->
                ListGenreToday(
                    title = item.title,
                    value = item.value,
                    index = index
                )
            }

        } else {

            if(state.genreMonthList[getWeekDate(getDate)].size > 0){

                itemsIndexed(state.genreMonthList[getWeekDate(getDate)]) { index, item ->
                    ListGenreToday(
                        title = item.title,
                        value = item.value,
                        index = index
                    )
                }

            } else {
                item { ScreenEmpty(str = "데이터가 없습니다") }
            }
        }


    }
}