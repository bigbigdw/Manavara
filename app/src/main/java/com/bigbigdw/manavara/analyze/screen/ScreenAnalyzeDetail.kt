package com.bigbigdw.manavara.analyze.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.analyze.getJsonGenreMonthList
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyzeDetail
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

                viewModelAnalyzeDetail.setScreen(menu = "${list.get(0).title} 작품 리스트")
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
            .width(330.dp)
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
                body = "누적 작품 수 : ${item.value}",
                current = "${item.title} 작품 리스트",
                onClick = {
                    coroutineScope.launch {
                        viewModelAnalyzeDetail.setScreen(menu = "${item.title} 작품 리스트")
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
                        viewModelAnalyzeDetail.setScreen(menu = "${item.title} 변동 현황")
                    }
                },
                value = state.menu,
            )
        }
    }
}

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

//            if (getMenu.isNotEmpty()) {
//                ScreenBestDetailTabs(
//                    getMenu = getMenu,
//                    viewModelBestDetail = viewModelBestDetail,
//                    platform = platform,
//                    bookCode = bookCode,
//                    type = type
//                )
//            } else {
//                ScreenBestDetailTabsEmpty(
//                    viewModelBestDetail = viewModelBestDetail,
//                    platform = platform,
//                    bookCode = bookCode,
//                    type = type,
//                    setMenu = setMenu,
//                    item = item
//                )
//            }
        }
    }
}