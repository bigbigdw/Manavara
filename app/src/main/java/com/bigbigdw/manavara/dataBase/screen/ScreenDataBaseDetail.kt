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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
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
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDataBaseDetail
import com.bigbigdw.manavara.best.getBookMapJson
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.best.screen.BestBottomDialog
import com.bigbigdw.manavara.best.screen.BestDialog
import com.bigbigdw.manavara.best.screen.ItemBestDetailInfoAnalyze
import com.bigbigdw.manavara.best.screen.TopbarBestDetail
import com.bigbigdw.manavara.dataBase.getGenreListWeekJson
import com.bigbigdw.manavara.dataBase.getGenreMap
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import com.bigbigdw.manavara.util.weekListAll
import convertDateStringWeek
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeDetail(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    widthSizeClass: WindowWidthSizeClass,
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val state = viewModelDataBaseDetail.state.collectAsState().value
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModelDataBaseDetail){

        when (state.mode) {
            "GENRE_BOOK", "KEYWORD_BOOK" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "BEST_WEEK",
                ) { jsonNameList ->

                    getGenreListWeekJson(
                        context = context,
                        platform = state.platform,
                        type = state.type,
                        dayType = "MONTH",
                        dataType = if (state.mode.contains("GENRE")) {
                            "GENRE"
                        } else {
                            "KEYWORD"
                        },
                        root = state.json
                    ) { genreKeywordMonthList, genreKeywordList ->

                        getBookMapJson(
                            platform = state.platform,
                            type = state.type,
                            context = context
                        ) { itemBookInfoMap ->

                            if(state.mode == "GENRE_BOOK"){
                                viewModelDataBaseDetail.setGenreBook(
                                    jsonNameList = jsonNameList,
                                    genreKeywordList = genreKeywordList,
                                    genreKeywordMonthList = genreKeywordMonthList,
                                    itemBookInfoMap = itemBookInfoMap,
                                    menu = "${genreKeywordList[0].key} 작품 리스트",
                                    key = genreKeywordList[0].key,
                                    itemGenreKeywordMap =  mutableMapOf()
                                )
                            } else {
                                getGenreMap(
                                    platform = state.platform,
                                    type = state.type,
                                    menuType = if (state.mode.contains("GENRE")) {
                                        "GENRE"
                                    } else {
                                        "KEYWORD"
                                    },
                                ) { itemGenreKeywordMap ->

                                    viewModelDataBaseDetail.setGenreBook(
                                        jsonNameList = jsonNameList,
                                        genreKeywordList = genreKeywordList,
                                        itemGenreKeywordMap = itemGenreKeywordMap,
                                        genreKeywordMonthList = genreKeywordMonthList,
                                        itemBookInfoMap = itemBookInfoMap,
                                        menu = "${genreKeywordList[0].key} 작품 리스트",
                                        key = genreKeywordList[0].key
                                    )
                                }
                            }
                        }
                    }
                }
            }
            "GENRE_STATUS", "KEYWORD_STATUS" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "BEST_WEEK",
                ) { jsonNameList ->

                    getGenreListWeekJson(
                        context = context,
                        platform = state.platform,
                        type = state.type,
                        dayType = "MONTH",
                        dataType = if (state.mode.contains("GENRE")) {
                            "GENRE"
                        } else {
                            "KEYWORD"
                        },
                        root = state.json
                    ) { genreKeywordMonthList, genreKeywordList ->

                        getGenreMap(
                            platform = state.platform,
                            type = state.type,
                            menuType = if (state.mode.contains("GENRE")) {
                                "GENRE"
                            } else {
                                "KEYWORD"
                            },
                        ) { itemGenreKeywordMap ->

                            viewModelDataBaseDetail.setGenreStatus(
                                jsonNameList = jsonNameList,
                                genreKeywordList = genreKeywordList,
                                genreKeywordMonthList = genreKeywordMonthList,
                                itemGenreKeywordMap = itemGenreKeywordMap,
                                menu = if (state.mode.contains("GENRE")) {
                                    "${convertDateStringWeek(jsonNameList.get(0))} 장르"
                                } else {
                                    "${convertDateStringWeek(jsonNameList.get(0))} 키워드"
                                },
                                key = genreKeywordList[0].key
                            )
                        }
                    }
                }

            }
        }
    }


    if (isExpandedScreen) {

        val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

        if (getDialogOpen) {
            BestDialog(
                onDismissRequest = { setDialogOpen(false) },
                itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                item = state.itemBookInfo,
                isExpandedScreen = isExpandedScreen,
                type = state.type
            )
        }

        ScreenTabletAnalyzeDetail(
            viewModelDataBaseDetail = viewModelDataBaseDetail,
            setDialogOpen = setDialogOpen,
            modalSheetState = null,
            listState = listState,
            isExpandedScreen = isExpandedScreen,
            drawerState = null
        )

    } else {

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
            skipHalfExpanded = false
        )

        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

            Column(
                modifier = Modifier
                    .width(330.dp)
                    .fillMaxHeight()
                    .background(color = colorF6F6F6)
                    .padding(8.dp, 0.dp)
                    .semantics { contentDescription = "Overview Screen" },
            ) {
                ScreenAnalyzeDetailPropertyList(
                    viewModelDataBaseDetail = viewModelDataBaseDetail,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                    drawerState = drawerState
                )
            }

        }) {
            Scaffold(
                topBar = {
                    TopbarBestDetail(
                        setDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        },
                        getter = state.menu
                    )
                }
            ) {

                Box(
                    Modifier
                        .padding(it)
                        .background(color = colorF6F6F6)
                        .fillMaxSize()
                ) {

                    ScreenTabletAnalyzeDetail(
                        viewModelDataBaseDetail = viewModelDataBaseDetail,
                        setDialogOpen = null,
                        modalSheetState = modalSheetState,
                        listState = listState,
                        isExpandedScreen = isExpandedScreen,
                        drawerState = drawerState
                    )
                }

            }

            if(modalSheetState.isVisible){
                ModalBottomSheetLayout(
                    sheetState = modalSheetState,
                    sheetElevation = 50.dp,
                    sheetShape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp
                    ),
                    sheetContent = {
                        Spacer(modifier = Modifier.size(4.dp))

                        BestBottomDialog(
                            itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                            item = state.itemBookInfo,
                            isExpandedScreen = isExpandedScreen,
                            modalSheetState = modalSheetState,
                            currentRoute = "NOVEL",
                        )
                    },
                ) {}
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAnalyzeDetailPropertyList(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    listState: LazyListState,
    isExpandedScreen: Boolean,
    drawerState: DrawerState?
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDataBaseDetail.state.collectAsState().value

    val modifier = if(isExpandedScreen){
        Modifier.width(400.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    LazyColumn(
        modifier = modifier
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

        if(state.mode == "GENRE_STATUS" || state.mode == "KEYWORD_STATUS"){
            itemsIndexed(state.jsonNameList) { index, item ->
                ItemMainSettingSingleTablet(
                    containerColor = colorList.get(index),
                    image = R.drawable.icon_collection_wht,
                    title = if (state.mode.contains("GENRE")) {
                        "${convertDateStringWeek(item)} 장르"
                    } else {
                        "${convertDateStringWeek(item)} 키워드"
                    },
                    body = "${convertDateStringWeek(item)} 장르 일별로 보기",
                    current = if (state.mode.contains("GENRE")) {
                        "${convertDateStringWeek(item)} 장르"
                    } else {
                        "${convertDateStringWeek(item)} 키워드"
                    },
                    onClick = {
                        coroutineScope.launch {
                            viewModelDataBaseDetail.setScreen(
                                menu = if (state.mode.contains("GENRE")) {
                                    "${convertDateStringWeek(item)} 장르"
                                } else {
                                    "${convertDateStringWeek(item)} 키워드"
                                },
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
        }

        itemsIndexed(state.genreKeywordList) { index, item ->
            ItemMainSettingSingleTablet(
                containerColor = if (index == 0) {
                    colorList[0]
                } else {
                    colorList[index % colorList.size]
                },
                image = R.drawable.icon_genre_wht,
                title = if (state.mode == "GENRE_BOOK" || state.mode == "KEYWORD_BOOK") {
                    "${item.key} 작품 리스트"
                } else {
                    "${item.key} 변동 현황"
                },
                body = if (state.mode == "GENRE_BOOK" || state.mode == "KEYWORD_BOOK") {
                    "${item.key}(이)가 포함된 작품 확인"
                } else {
                    "${item.key}(이)가 사용된 날짜 확인"
                },
                current = if (state.mode == "GENRE_BOOK" || state.mode == "KEYWORD_BOOK") {
                    "${item.key} 작품 리스트"
                } else {
                    "${item.key} 변동 현황"
                },
                onClick = {
                    coroutineScope.launch {
                        viewModelDataBaseDetail.setScreen(
                            menu = if (state.mode == "GENRE_BOOK" || state.mode == "KEYWORD_BOOK") {
                                "${item.key} 작품 리스트"
                            } else {
                                "${item.key} 변동 현황"
                            }, key = item.key
                        )

                        listState.scrollToItem(index = 0)

                        drawerState?.close()
                    }
                },
                value = state.menu,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenTabletAnalyzeDetail(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    setDialogOpen: ((Boolean) -> Unit)?,
    modalSheetState: ModalBottomSheetState?,
    listState: LazyListState,
    isExpandedScreen: Boolean,
    drawerState: DrawerState?
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    )  {

        if(isExpandedScreen){
            ScreenAnalyzeDetailPropertyList(
                viewModelDataBaseDetail = viewModelDataBaseDetail,
                listState = listState,
                isExpandedScreen = isExpandedScreen,
                drawerState = drawerState
            )

            Spacer(modifier = Modifier.size(16.dp))
        }

        ScreenAnalyzeDetailItem(
            viewModelDataBaseDetail = viewModelDataBaseDetail,
            setDialogOpen = setDialogOpen,
            modalSheetState = modalSheetState,
            listState = listState,
            isExpandedScreen = isExpandedScreen
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeDetailItem(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    setDialogOpen: ((Boolean) -> Unit)?,
    modalSheetState: ModalBottomSheetState?,
    listState: LazyListState,
    isExpandedScreen : Boolean
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
    ) {

        if(isExpandedScreen){
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
        }

        if (state.menu.contains("작품 리스트")) {

            if(state.mode.contains("GENRE")){
                ScreenGenreBooks(
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen,
                    viewModelDataBaseDetail = viewModelDataBaseDetail,
                    listState = listState
                )
            } else{
                ScreenKeywordBooks(
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen,
                    viewModelDataBaseDetail = viewModelDataBaseDetail,
                    listState = listState
                )
            }

        } else if (state.menu.contains("주차")) {

            ScreenGenreStatusWeekly(
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )

        } else if (state.menu.contains("변동 현황")) {

            ScreenGenreStatus(
                viewModelDataBaseDetail = viewModelDataBaseDetail
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenKeywordBooks(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    listState: LazyListState
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value
    val filteredList = ArrayList<ItemBookInfo>()
    val coroutineScope = rememberCoroutineScope()

    if(state.itemGenreKeywordMap[state.key] != null){

        val keyword = state.itemGenreKeywordMap[state.key]?.get(0) ?: ItemKeyword()

        val titleList = keyword.value.split(", ")

        for(item in titleList){
            val bookItem = state.itemBookInfoMap[item]

            if (bookItem != null) {
                filteredList.add(bookItem)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp),
        state = listState
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(filteredList) { index, item ->
            ScreenBookCard(
                type = "MONTH",
                item = item,
                index = index,
            ) {
                coroutineScope.launch {

                    modalSheetState?.show()

                    getBookItemWeekTrophy(
                        bookCode = item.bookCode,
                        type = state.type,
                        platform = state.platform
                    ) { itemBestInfoTrophyList ->

                        viewModelDataBaseDetail.setItemBestInfoTrophyList(
                            itemBookInfo = item,
                            itemBestInfoTrophyList = itemBestInfoTrophyList
                        )

                    }

                    if (setDialogOpen != null) {
                        setDialogOpen(true)
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenGenreBooks(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    listState: LazyListState
) {

    val state = viewModelDataBaseDetail.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()


    val filteredList = ArrayList<ItemBookInfo>()

    for (trophyItem in state.itemBookInfoMap.values) {
        val bookCode = trophyItem.bookCode
        val bookInfo = state.itemBookInfoMap[bookCode]

        if (bookInfo != null) {
            filteredList.add(bookInfo)
        }
    }


    val filteredMap = state.itemBookInfoMap.filter {

        it.value.genre == state.key
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp),
        state = listState
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(ArrayList(filteredMap.values)) { index, item ->
            ScreenBookCard(
                type = "MONTH",
                item = item,
                index = index,
            ) {
                coroutineScope.launch {

                    getBookItemWeekTrophy(
                        bookCode = item.bookCode,
                        type = state.type,
                        platform = state.platform
                    ) { itemBestInfoTrophyList ->

                        viewModelDataBaseDetail.setItemBestInfoTrophyList(
                            itemBookInfo = item,
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

        val list = state.itemGenreKeywordMap[state.key] ?: ArrayList()

        item {
            TabletContentWrap {
                list.forEachIndexed { index, item ->

                    val year = item.date.substring(0, 4)
                    val month = item.date.substring(4, 6)
                    val day = item.date.substring(6, 8)

                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = if (state.mode.contains("KEYWORD")) {
                            val wordCount =
                                item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }
                            wordCount.toString()
                        } else {
                            item.value
                        },
                        beforeValue = if (index == 0) {
                            "-"
                        } else {
                            if (state.mode.contains("KEYWORD")) {
                                val wordCount = list[index - 1].value.split("\\s+".toRegex())
                                    .count { it.isNotEmpty() }
                                wordCount.toString()
                            } else {
                                list[index - 1].value
                            }
                        },
                        type = "랭킹 분석",
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
    val context = LocalContext.current

    LaunchedEffect(state.key) {

        if(state.mode.contains("GENRE")){
            getGenreListWeekJson(
                context = context,
                platform = state.platform,
                type = state.type,
                dayType = "WEEK",
                dataType = "GENRE",
                root = state.key
            ) { genreWeekList, genreList ->
                viewModelDataBaseDetail.setGenreList(
                    genreMonthList = genreWeekList,
                    genreList = genreList
                )
            }
        } else {
            getGenreListWeekJson(
                context = context,
                platform = state.platform,
                type = state.type,
                dayType = "WEEK",
                dataType = "KEYWORD",
                root = state.key
            ) { genreWeekList, genreList ->
                viewModelDataBaseDetail.setGenreList(
                    genreMonthList = genreWeekList,
                    genreList = genreList
                )
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(8.dp)) }

        item {
            LazyRow(
                modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
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

            itemsIndexed(state.genreKeywordList) { index, item ->
                ListGenreKeywordToday(
                    title = item.key,
                    value = item.value,
                    index = index
                )
            }

        } else {

            if (state.genreKeywordMonthList[getWeekDate(getDate)].size > 0) {

                itemsIndexed(state.genreKeywordMonthList[getWeekDate(getDate)]) { index, item ->
                    ListGenreKeywordToday(
                        title = item.key,
                        value = item.value,
                        index = index
                    )
                }

            } else {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ScreenEmpty(str = "데이터가 없습니다")
                    }
                }
            }
        }


    }
}