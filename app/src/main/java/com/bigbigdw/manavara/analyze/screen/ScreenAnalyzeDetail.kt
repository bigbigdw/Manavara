package com.bigbigdw.manavara.analyze.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
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
import com.bigbigdw.manavara.analyze.getJsonGenreMonthList
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyzeDetail
import com.bigbigdw.manavara.best.getBookItemWeekTrophyDialog
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.screen.ListBest
import com.bigbigdw.manavara.best.screen.ScreenBestItemDetailTabItem
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAnalyzeDetail(
    viewModelAnalyzeDetail: ViewModelAnalyzeDetail,
    widthSizeClass: WindowWidthSizeClass,
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val state = viewModelAnalyzeDetail.state.collectAsState().value
    val context = LocalContext.current

    DisposableEffect(context) {

        getBookMap(
            platform = state.platform,
            type = state.type
        ) {
            viewModelAnalyzeDetail.setItemBookInfoMap(it)

        }

        getJsonGenreMonthList(
            platform = state.platform,
            type = state.type,
            root = state.json
        ) { monthList, list ->
            viewModelAnalyzeDetail.setGenreList(list)

            if(state.menu.isEmpty()){

                Log.d("HIHI", "${list.get(0).title} 작품 리스트")

                viewModelAnalyzeDetail.setScreen(menu = "${list.get(0).title} 작품 리스트", key = list.get(0).title)
            }
        }

        onDispose {}
    }


    if (isExpandedScreen) {

        ScreenTabletAnalyzeDetail(
            viewModelAnalyzeDetail = viewModelAnalyzeDetail,
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
                viewModelAnalyzeDetail = viewModelAnalyzeDetail
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
                        viewModelAnalyzeDetail = viewModelAnalyzeDetail
                    )
                }

            }
        }

    }
}

@Composable
fun ScreenAnalyzeDetailPropertyList(
    viewModelAnalyzeDetail: ViewModelAnalyzeDetail,
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelAnalyzeDetail.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .width(400.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {
        item {Spacer(modifier = Modifier.size(16.dp))}

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

        itemsIndexed(state.genreList) { index, item ->
            ItemMainSettingSingleTablet(
                containerColor = colorList.get(index),
                image = R.drawable.icon_genre_wht,
                title = "${item.title} 작품 리스트",
                body = "${state.title}이 포함된 작품 확인",
                current = "${item.title} 작품 리스트",
                onClick = {
                    coroutineScope.launch {
                        viewModelAnalyzeDetail.setScreen(menu = "${item.title} 작품 리스트", key = item.title)
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
                title = "${item.title} 변동 현황",
                body = "${state.title} 변동 현황",
                current = "${item.title} 변동 현황",
                onClick = {
                    coroutineScope.launch {
                        viewModelAnalyzeDetail.setScreen(menu = "${item.title} 변동 현황", key = item.title)
                    }
                },
                value = state.menu,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTabletAnalyzeDetail(
    viewModelAnalyzeDetail: ViewModelAnalyzeDetail
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            ScreenAnalyzeDetailPropertyList(
                viewModelAnalyzeDetail = viewModelAnalyzeDetail
            )

            Spacer(modifier = Modifier.size(16.dp))

            ScreenAnalyzeDetailItem(
                viewModelAnalyzeDetail = viewModelAnalyzeDetail,
                drawerState = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeDetailItem(
    viewModelAnalyzeDetail: ViewModelAnalyzeDetail,
    drawerState: DrawerState?,
) {

    val state = viewModelAnalyzeDetail.state.collectAsState().value

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
                viewModelAnalyzeDetail = viewModelAnalyzeDetail
            )

        } else if (state.menu.contains("변동 현황")) {

//        ScreenBestDBListNovel(
//            drawerState = drawerState,
//            viewModelAnalyze = viewModelAnalyze
//        )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenGenreBooks(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelAnalyzeDetail: ViewModelAnalyzeDetail,
) {

    val state = viewModelAnalyzeDetail.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item {  Spacer(modifier = Modifier.size(16.dp)) }

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
                    viewModelAnalyzeDetail.setItemBookInfo(itemBookInfo = item)

                    getBookItemWeekTrophyDialog(
                        itemBookInfo = item,
                        type = state.type,
                        platform = state.platform
                    ) { itemBookInfo, itemBestInfoTrophyList ->
                        viewModelAnalyzeDetail.setItemBestInfoTrophyList(
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