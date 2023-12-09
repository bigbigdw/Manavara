package com.bigbigdw.manavara.dataBase.screen

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.dataBase.getJsonGenreMonthList
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDataBaseDetail
import com.bigbigdw.manavara.best.getBookItemWeekTrophyDialog
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.best.screen.ItemBestDetailInfoAnalyze
import com.bigbigdw.manavara.best.screen.ListBest
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.dataBase.getGenreMap
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.dataBase.getJsonGenreWeekList
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
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

    LaunchedEffect(viewModelDataBaseDetail){
        Log.d("ScreenAnalyzeDetail", "MODE state.mode == ${state.mode}")

        getJsonFiles(
            platform = state.platform,
            type = state.type,
            root = "BEST_WEEK",
        ) {
            viewModelDataBaseDetail.setJsonNameList(it)

            if(state.mode == "GENRE_STATUS" || state.mode == "KEYWORD_STATUS"){
                viewModelDataBaseDetail.setScreen(
                    menu = if (state.mode.contains("GENRE")) {
                        "${convertDateStringWeek(it.get(0))} 장르"
                    } else {
                        "${convertDateStringWeek(it.get(0))} 키워드"
                    },
                    key = it[0]
                )
            }
        }

        when (state.mode) {
            "GENRE_BOOK" -> {
                getBookMap(
                    platform = state.platform,
                    type = state.type
                ) {
                    viewModelDataBaseDetail.setItemBookInfoMap(it)
                }
            }
            "GENRE_STATUS" -> {
                getGenreMap(
                    platform = state.platform,
                    type = state.type,
                    menuType = "GENRE"
                ) { monthList ->
                    viewModelDataBaseDetail.setGenreMap(ItemKeywordMap = monthList)
                }
            }
            "KEYWORD_BOOK" -> {
                getBookMap(
                    platform = state.platform,
                    type = state.type
                ) {
                    viewModelDataBaseDetail.setItemBookInfoMap(it)
                }

                getGenreMap(
                    platform = state.platform,
                    type = state.type,
                    menuType = "KEYWORD"
                ) { monthList ->
                    viewModelDataBaseDetail.setGenreMap(ItemKeywordMap = monthList)
                }
            }
            "KEYWORD_STATUS" -> {
                getGenreMap(
                    platform = state.platform,
                    type = state.type,
                    menuType = "KEYWORD"
                ) { monthList ->
                    viewModelDataBaseDetail.setGenreMap(ItemKeywordMap = monthList)
                }
            }
        }

        if (state.mode == "GENRE_BOOK" || state.mode == "GENRE_STATUS") {
            getJsonGenreMonthList(
                platform = state.platform,
                type = state.type,
                root = state.json
            ) { monthList, list ->
                viewModelDataBaseDetail.setGenreList(genreList = list, genreMonthList = monthList)

                if(state.mode == "GENRE_BOOK"){
                    viewModelDataBaseDetail.setScreen(
                        menu = "${list[0].key} 작품 리스트",
                        key = list[0].key
                    )
                }
            }
        } else if (state.mode == "KEYWORD_BOOK" || state.mode == "KEYWORD_STATUS") {
            getJsonGenreMonthList(
                platform = state.platform,
                type = state.type,
                root = state.json,
                dataType = "KEYWORD"
            ) { monthList, list ->
                viewModelDataBaseDetail.setGenreList(genreList = list, genreMonthList = monthList)

                if(state.mode == "KEYWORD_BOOK"){
                    viewModelDataBaseDetail.setScreen(
                        menu = "${list[0].key} 작품 리스트",
                        key = list[0].key
                    )
                }
            }
        }
    }


    if (isExpandedScreen) {

        val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

        if (getDialogOpen) {
            Dialog(
                onDismissRequest = { setDialogOpen(false) },
            ) {
                AlertTwoBtn(isShow = { setDialogOpen(false) },
                    onFetchClick = {
                        val intent = Intent(context, ActivityBestDetail::class.java)
                        intent.putExtra("BOOKCODE", state.itemBookInfo.bookCode)
                        intent.putExtra("PLATFORM", state.itemBookInfo.type)
                        intent.putExtra("TYPE", state.type)
                        context.startActivity(intent)
                    },
                    btnLeft = "취소",
                    btnRight = "작품 보러가기",
                    modifier = Modifier.requiredWidth(400.dp),
                    contents = {
                        ScreenDialogBest(
                            item = state.itemBookInfo,
                            trophy = state.itemBestInfoTrophyList,
                            isExpandedScreen = isExpandedScreen,
                            currentRoute = state.type,
                            modalSheetState = null
                        )
                    }
                )
            }
        }

        ScreenTabletAnalyzeDetail(
            viewModelDataBaseDetail = viewModelDataBaseDetail,
            setDialogOpen = setDialogOpen,
            modalSheetState = null,
            listState = listState
        )

//        BestDetailBackOnPressed(
//            getMenu = getMenu,
//            setMenu = setMenu
//        )

    } else {

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
            skipHalfExpanded = false
        )

        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

            ScreenAnalyzeDetailPropertyList(
                viewModelDataBaseDetail = viewModelDataBaseDetail,
                listState = listState,
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
                        viewModelDataBaseDetail = viewModelDataBaseDetail,
                        listState = listState
                    )
                }

            }
        }

    }
}

@Composable
fun ScreenAnalyzeDetailPropertyList(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    listState: LazyListState
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

        itemsIndexed(state.genreList) { index, item ->
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
                    }
                },
                value = state.menu,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTabletAnalyzeDetail(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    setDialogOpen: (Boolean) -> Unit,
    modalSheetState: ModalBottomSheetState?,
    listState: LazyListState
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            ScreenAnalyzeDetailPropertyList(
                viewModelDataBaseDetail = viewModelDataBaseDetail,
                listState = listState
            )

            Spacer(modifier = Modifier.size(16.dp))

            ScreenAnalyzeDetailItem(
                viewModelDataBaseDetail = viewModelDataBaseDetail,
                setDialogOpen = setDialogOpen,
                modalSheetState = modalSheetState,
                listState = listState
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeDetailItem(
    viewModelDataBaseDetail: ViewModelDataBaseDetail,
    setDialogOpen: ((Boolean) -> Unit)?,
    modalSheetState: ModalBottomSheetState?,
    listState: LazyListState
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

    if(state.itemKeywordMap.isNotEmpty() && state.itemBookInfoMap.isNotEmpty()){

        if(state.itemKeywordMap[state.key] != null){

            val keyword = state.itemKeywordMap[state.key]?.get(0) ?: ItemKeyword()

            val titleList = keyword.value.split(", ")

            for(item in titleList){
                val bookItem = state.itemBookInfoMap[item]

                if (bookItem != null) {
                    filteredList.add(bookItem)
                }
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

                    if (setDialogOpen != null) {
                        setDialogOpen(true)
                    }

                    modalSheetState?.show()
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

    val filteredMap = state.itemBookInfoMap.filter { it.value.genre == state.key }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp),
        state = listState
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

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

        val list = state.itemKeywordMap[state.key] ?: ArrayList()

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

    LaunchedEffect(state.key) {

        if(state.mode.contains("GENRE")){
            getJsonGenreWeekList(
                platform = state.platform,
                type = state.type,
                root = state.key
            ) { genreWeekList, genreList ->
                viewModelDataBaseDetail.setGenreList(
                    genreMonthList = genreWeekList,
                    genreList = genreList
                )
            }
        } else {
            getJsonGenreWeekList(
                platform = state.platform,
                type = state.type,
                root = state.key,
                dataType = "KEYWORD"
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

            itemsIndexed(state.genreList) { index, item ->
                ListGenreToday(
                    title = item.key,
                    value = item.value,
                    index = index
                )
            }

        } else {

            if (state.genreMonthList[getWeekDate(getDate)].size > 0) {

                itemsIndexed(state.genreMonthList[getWeekDate(getDate)]) { index, item ->
                    ListGenreToday(
                        title = item.key,
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