package com.bigbigdw.manavara.analyze.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.analyze.ActivityGenreDetail
import com.bigbigdw.manavara.analyze.getGenreDay
import com.bigbigdw.manavara.analyze.getJsonFiles
import com.bigbigdw.manavara.analyze.getJsonGenreMonthList
import com.bigbigdw.manavara.analyze.getJsonGenreWeekList
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.best.getBestMonthTrophy
import com.bigbigdw.manavara.best.getBestWeekTrophy
import com.bigbigdw.manavara.best.getBookItemWeekTrophyDialog
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.best.models.ItemGenre
import com.bigbigdw.manavara.best.screen.ListBest
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color2EA259
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4996E8
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color536FD2
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color64C157
import com.bigbigdw.manavara.ui.theme.color79B4F8
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.color808CF8
import com.bigbigdw.manavara.ui.theme.color80BF78
import com.bigbigdw.manavara.ui.theme.color8AA6BD
import com.bigbigdw.manavara.ui.theme.color91CEC7
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFDC24E
import com.bigbigdw.manavara.ui.theme.colorFFAC59
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import convertDateStringMonth
import convertDateStringWeek
import getBookCount
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenAnalyze(
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState? = null,
    drawerState: DrawerState,
    currentRoute: String?,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelAnalyze: ViewModelAnalyze = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state = viewModelAnalyze.state.collectAsState().value

    BestAnalyzeBackOnPressed(viewModelAnalyze = viewModelAnalyze)

    DisposableEffect(context) {

        viewModelAnalyze.sideEffects
            .onEach { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            .launchIn(coroutineScope)

        onDispose {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
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

                ScreenAnalyzePropertyList(
                    drawerState = drawerState,
                    viewModelAnalyze = viewModelAnalyze
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenAnalyzeItems(
                    viewModelAnalyze = viewModelAnalyze,
                    drawerState = drawerState,
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen
                )

            } else {

                if (modalSheetState != null) {
                    BestAnalyzeBackOnPressed(viewModelAnalyze = viewModelAnalyze)
                }

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenAnalyzePropertyList(
                        drawerState = drawerState,
                        viewModelAnalyze = viewModelAnalyze
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenAnalyzeTopbar {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        },

                        ) {
                        Box(
                            Modifier
                                .padding(it)
                                .background(color = colorF6F6F6)
                                .fillMaxSize()
                        ) {
                            ScreenAnalyzeItems(
                                viewModelAnalyze = viewModelAnalyze,
                                drawerState = drawerState,
                                modalSheetState = modalSheetState,
                                setDialogOpen = null
                            )
                        }
                    }

                    if (modalSheetState != null) {
                        ModalBottomSheetLayout(
                            sheetState = modalSheetState,
                            sheetElevation = 50.dp,
                            sheetShape = RoundedCornerShape(
                                topStart = 25.dp,
                                topEnd = 25.dp
                            ),
                            sheetContent = {
                                Spacer(modifier = Modifier.size(4.dp))

                                if (currentRoute != null) {
                                    ScreenDialogBest(
                                        item = state.itemBookInfo,
                                        trophy = state.itemBestInfoTrophyList,
                                        isExpandedScreen = isExpandedScreen,
                                        currentRoute = currentRoute,
                                        modalSheetState = modalSheetState
                                    )
                                }
                            },
                        ) {}
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAnalyzePropertyList(
    drawerState: DrawerState?,
    viewModelAnalyze: ViewModelAnalyze
) {

    val coroutineScope = rememberCoroutineScope()
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

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "마나바라 분석",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(16.dp))

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_novel_wht,
            title = "마나바라 베스트 웹소설 DB",
            body = "마나바라에 기록된 베스트 웹소설 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "베스트 웹소설 DB",
                        type = state.type
                    )
                    drawerState?.close()
                }
            },
            value = "베스트 웹소설 DB"
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_best_wht,
            title = "주차별 웹소설 베스트",
            body = "주차별 웹소설 베스트 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "주차별 웹소설 베스트",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "주차별 웹소설 베스트"
        )

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_best_wht,
            title = "월별 웹소설 베스트",
            body = "월별 웹소설 베스트 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "월별 웹소설 베스트",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                }
            },
            value = "월별 웹소설 베스트"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17FA0,
            image = R.drawable.icon_genre_wht,
            title = "투데이 장르 현황",
            body = "플랫폼별 투데이 장르 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "투데이 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                }
            },
            value = "투데이 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color21C2EC,
            image = R.drawable.icon_genre_wht,
            title = "주간 장르 현황",
            body = "플랫폼별 주간 장르 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "주간 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                }
            },
            value = "주간 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color31C3AE,
            image = R.drawable.icon_genre_wht,
            title = "월간 장르 현황",
            body = "플랫폼별 월간 장르 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "월간 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                }
            },
            value = "월간 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color7C81FF,
            image = R.drawable.icon_genre_wht,
            title = "장르 리스트 작품",
            body = "플랫폼별 장르 리스트 작품 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(
                        detail = "",
                        menu = "장르 리스트 작품",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                }
            },
            value = "장르 리스트 작품"
        )

        ItemMainSettingSingleTablet(
            containerColor = color64C157,
            image = R.drawable.icon_keyword_wht,
            title = "웹소설 투데이 키워드 베스트",
            body = "웹소설 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17666,
            image = R.drawable.icon_keyword_wht,
            title = "웹소설 주간 키워드 베스트",
            body = "웹소설 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_keyword_wht,
            title = "웹소설 월간 키워드 베스트",
            body = "웹소설 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color4996E8,
            image = R.drawable.icon_keyword_wht,
            title = "키워드 리스트 작품",
            body = "웹소설 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorFDC24E,
            image = R.drawable.icon_search_wht,
            title = "웹소설 DB 검색",
            body = "웹소설 DB 검색",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 DB 검색",
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color80BF78,
            image = R.drawable.icon_webtoon_wht,
            title = "마나바라 베스트 웹툰 DB",
            body = "마나바라에 기록된 웹툰 웹툰 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "베스트 웹툰 DB"
        )

        ItemMainSettingSingleTablet(
            containerColor = color91CEC7,
            image = R.drawable.icon_best_wht,
            title = "주차별 웹소설 베스트",
            body = "주차별 웹소설 베스트 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color79B4F8,
            image = R.drawable.icon_best_wht,
            title = "월별 웹소설 베스트",
            body = "월별 웹소설 베스트 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color8AA6BD,
            image = R.drawable.icon_trophy_wht,
            title = "주차별 웹소설 트로피",
            body = "주차별 웹소설 트로피 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color2EA259,
            image = R.drawable.icon_trophy_wht,
            title = "연간 웹소설 트로피",
            body = "연간 웹소설 트로피 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color808CF8,
            image = R.drawable.icon_genre_wht,
            title = "투데이 웹툰 장르 베스트",
            body = "플랫폼별 웹툰 베스트 장르 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹툰 투데이 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorFFAC59,
            image = R.drawable.icon_genre_wht,
            title = "주간 웹툰 장르 베스트",
            body = "플랫폼별 웹툰 베스트 장르 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹툰 주간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_genre_wht,
            title = "월간 웹툰 장르 베스트",
            body = "플랫폼별 월간 웹툰 베스트 장르 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹툰 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_keyword_wht,
            title = "웹툰 투데이 키워드 베스트",
            body = "웹툰 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_keyword_wht,
            title = "웹툰 주간 키워드 베스트",
            body = "웹툰 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_keyword_wht,
            title = "웹툰 월간 키워드 베스트",
            body = "웹툰 월간 키워드 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹소설 월간 장르"
        )

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_search_wht,
            title = "웹툰 DB 검색",
            body = "웹툰 DB 검색",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelAnalyze.setScreen(detail = "", menu = "주차별 웹소설 베스트")
                }
            },
            value = "웹툰 DB 검색",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeItem(
    viewModelAnalyze: ViewModelAnalyze,
    drawerState: DrawerState?,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelAnalyze.state.collectAsState().value

    if (state.menu.contains("베스트 웹소설 DB")) {

        ScreenBestDBListNovel(
            drawerState = drawerState,
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("베스트 웹툰 DB")) {

        ScreenBestDBListNovel(
            drawerState = drawerState,
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("주차별 웹소설 베스트")) {

        ScreenBestAnalyzeList(
            viewModelAnalyze = viewModelAnalyze,
            drawerState = drawerState,
        )

    } else if (state.menu.contains("월별 웹소설 베스트")) {

        ScreenBestAnalyzeList(
            viewModelAnalyze = viewModelAnalyze,
            drawerState = drawerState,
        )

    } else if (state.menu.contains("투데이 장르 현황")) {

        ScreenBestAnalyzeList(
            viewModelAnalyze = viewModelAnalyze,
            drawerState = drawerState,
            itemList = genreListEng()
        )

    } else if (state.menu.contains("주간 장르 현황")) {

        ScreenBestAnalyzeList(
            viewModelAnalyze = viewModelAnalyze,
            drawerState = drawerState,
            itemList = genreListEng()
        )

    } else if (state.menu.contains("월간 장르 현황")) {

        ScreenBestAnalyzeList(
            viewModelAnalyze = viewModelAnalyze,
            drawerState = drawerState,
            itemList = genreListEng()
        )

    }  else if (state.menu.contains("장르 리스트 작품")) {

        ScreenBestAnalyzeList(
            viewModelAnalyze = viewModelAnalyze,
            drawerState = drawerState,
            itemList = genreListEng()
        )

    } else if (state.menu.contains("웹툰 투데이 장르")) {
        ScreenGenre(
            menuType = "투데이",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹툰 주간 장르")) {
        ScreenGenre(
            menuType = "주간",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹툰 월간 장르")) {
        ScreenGenre(
            menuType = "월간",
            viewModelAnalyze = viewModelAnalyze
        )
    } else {
        ScreenBestDBListNovel(
            drawerState = drawerState,
            viewModelAnalyze = viewModelAnalyze
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeItemDetail(
    viewModelAnalyze: ViewModelAnalyze,
    drawerState: DrawerState?,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelAnalyze.state.collectAsState().value

    if (state.menu.contains("베스트 웹소설 DB")) {
        ScreenBookMap(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelAnalyze = viewModelAnalyze,
            platform = state.platform
        )
    } else if (state.menu.contains("베스트 웹툰 DB")) {
        ScreenBookMap(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelAnalyze = viewModelAnalyze,
            platform = state.platform
        )
    }  else if (state.menu.contains("주차별 웹소설 베스트")) {
        ScreenBestAnalyze(
            viewModelAnalyze = viewModelAnalyze,
            root = "WEEK",
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen
        )

    }  else if (state.menu.contains("월별 웹소설 베스트")) {

        ScreenBestAnalyze(
            viewModelAnalyze = viewModelAnalyze,
            root = "MONTH",
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen
        )

    } else if (state.menu.contains("장르 리스트 작품")) {

        ScreenGenreDetail(
            viewModelAnalyze = viewModelAnalyze,
        )

    } else if (state.menu.contains("투데이 장르 현황")) {
        ScreenGenre(
            menuType = "투데이",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("주간 장르 현황")) {
        ScreenGenre(
            menuType = "주간",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("월간 장르 현황")) {
        ScreenGenre(
            menuType = "월간",
            viewModelAnalyze = viewModelAnalyze
        )
    } else if (state.menu.contains("웹툰 투데이 장르")) {
        ScreenGenre(
            menuType = "투데이",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹툰 주간 장르")) {
        ScreenGenre(
            menuType = "주간",
            viewModelAnalyze = viewModelAnalyze
        )

    } else if (state.menu.contains("웹툰 월간 장르")) {
        ScreenGenre(
            menuType = "월간",
            viewModelAnalyze = viewModelAnalyze
        )
    } else {
        ScreenBestDBListNovel(
            drawerState = drawerState,
            viewModelAnalyze = viewModelAnalyze
        )
    }
}

@Composable
fun ScreenGenre(
    menuType: String,
    viewModelAnalyze: ViewModelAnalyze
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month){
        when (menuType) {
            "투데이" -> {
                getGenreDay(platform = state.platform, type = state.type){
                    viewModelAnalyze.setGenreList(it)
                }
            }
            "주간" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "WEEK",
                ) {
                    viewModelAnalyze.setJsonNameList(it)

                    if(state.week.isEmpty()){
                        viewModelAnalyze.setDate(week = it.get(0))
                    }
                }

                if(state.jsonNameList.isNotEmpty()){
                    getJsonGenreWeekList(
                        platform = state.platform,
                        type = state.type,
                        root = state.week
                    ) { weekList, list ->
                        viewModelAnalyze.setGenreList(list)
                    }
                }

            }
            else -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "MONTH",
                ) {
                    viewModelAnalyze.setJsonNameList(it)

                    if(state.month.isEmpty()){
                        viewModelAnalyze.setDate(month = it.get(0))
                    }
                }

                if(state.jsonNameList.isNotEmpty()){
                    getJsonGenreMonthList(
                        platform = state.platform,
                        type = state.type,
                        root = state.month
                    ) { monthList, list ->
                        viewModelAnalyze.setGenreList(list)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    when (menuType) {
        "투데이" -> {
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {
                itemsIndexed(state.genreList) { index, item ->
                    ListGenreToday(
                        itemBestKeyword = item,
                        index = index
                    )
                }
            }
        }
        "주간" -> {

            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {

                item {
                    LazyRow(
                        modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
                    ) {
                        itemsIndexed(state.jsonNameList) { index, item ->
                            Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                                ScreenItemKeyword(
                                    getter = convertDateStringWeek(item),
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModelAnalyze.setDate(week = item)

                                            listState.scrollToItem(index = 0)
                                        }
                                    },
                                    title = convertDateStringWeek(item),
                                    getValue = convertDateStringWeek(state.week)
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.size(4.dp)) }

                itemsIndexed(state.genreList) { index, item ->
                    ListGenreToday(
                        itemBestKeyword = item,
                        index = index
                    )
                }
            }
        }
        else -> {

            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {

                item {
                    LazyRow(
                        modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
                    ) {
                        itemsIndexed(state.jsonNameList) { index, item ->
                            Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                                ScreenItemKeyword(
                                    getter = convertDateStringMonth(item),
                                    onClick = {
                                        coroutineScope.launch {
                                            viewModelAnalyze.setDate(month = item)

                                            listState.scrollToItem(index = 0)
                                        }
                                    },
                                    title =  convertDateStringMonth(item),
                                    getValue =  convertDateStringMonth(state.month)
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.size(4.dp)) }


                itemsIndexed(state.genreList) { index, item ->
                    ListGenreToday(
                        itemBestKeyword = item,
                        index = index
                    )
                }

                item { Spacer(modifier = Modifier.size(60.dp)) }
            }
        }
    }

}

@Composable
fun ScreenGenreDetail(
    viewModelAnalyze: ViewModelAnalyze
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month){
        getJsonFiles(
            platform = state.platform,
            type = state.type,
            root = "MONTH",
        ) {
            viewModelAnalyze.setJsonNameList(it)

            if(state.month.isEmpty()){
                viewModelAnalyze.setDate(month = it.get(0))
            }
        }

    }

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        itemsIndexed(state.jsonNameList) { index, item ->

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
                    onClick = {
                        val intent = Intent(context, ActivityGenreDetail::class.java)
                        intent.putExtra("TITLE", "${changePlatformNameKor(state.platform)} ${convertDateStringMonth(item)} 장르")
                        intent.putExtra("JSON", item)
                        intent.putExtra("PLATFORM", state.platform)
                        intent.putExtra("TYPE", state.type)
                        context.startActivity(intent)
                    },
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
                                text = convertDateStringMonth(item),
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(1f),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Left,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    })
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }

}


@Composable
fun ListGenreToday(
    itemBestKeyword: ItemGenre,
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenAnalyzeItems(
    viewModelAnalyze: ViewModelAnalyze,
    drawerState: DrawerState?,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {
    
    val state = viewModelAnalyze.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        if (state.detail.isNotEmpty()) {

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
                    text = state.detail,
                    fontSize = 24.sp,
                    color = color000000,
                    fontWeight = FontWeight(weight = 700)
                )
            }


        } else if (state.menu.isNotEmpty()) {

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

        Spacer(modifier = Modifier.size(16.dp))


        if (state.detail.isNotEmpty()) {

            ScreenAnalyzeItemDetail(
                viewModelAnalyze = viewModelAnalyze,
                drawerState = drawerState,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen
            )
        } else {

            ScreenAnalyzeItem(
                viewModelAnalyze = viewModelAnalyze,
                drawerState = drawerState,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen
            )
        }
    }
}

@Composable
fun ScreenAnalyzeTopbar(onClick: () -> Unit) {

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDBListNovel(
    drawerState: DrawerState?, 
    viewModelAnalyze: ViewModelAnalyze
) {
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val coroutineScope = rememberCoroutineScope()
    val state = viewModelAnalyze.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
    ) {

        val itemList = if (state.type == "NOVEL") novelListEng() else comicListEng()

        itemsIndexed(itemList) { index, item ->
            Box(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)) {
                TabletContentWrapBtn(
                    onClick = {
                        coroutineScope.launch {

                            viewModelAnalyze.setScreen(
                                menu = state.menu,
                                detail = "${changePlatformNameKor(item)} ${state.menu}",
                                platform = item,
                                type = state.type
                            )
                            drawerState?.close()
                        }
                    },
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

                            getBookCount(context = context, type = state.type, platform = item)

                            Text(
                                text = spannableString(
                                    textFront = "${changePlatformNameKor(item)} : ",
                                    color = color000000,
                                    textEnd = "${
                                        dataStore.getDataStoreString(
                                            if (state.type == "NOVEL") getPlatformDataKeyNovel(item) else getPlatformDataKeyComic(
                                                item
                                            )
                                        ).collectAsState(initial = "").value ?: "0"
                                    } 작품"
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBookMap(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelAnalyze: ViewModelAnalyze,
    platform: String
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    getBookMap(
        platform = state.platform,
        type = state.type
    ) {
        viewModelAnalyze.setItemBookInfoMap(it)
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item {  Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(ArrayList(state.itemBookInfoMap.values)) { index, item ->
            ListBest(
                item = item,
                type = "MONTH",
                index = index,
            ) {
                coroutineScope.launch {
                    viewModelAnalyze.setItemBookInfo(itemBookInfo = item)

                    getBookItemWeekTrophyDialog(
                        itemBookInfo = item,
                        type = state.type,
                        platform = platform
                    ) { itemBookInfo, itemBestInfoTrophyList ->
                        viewModelAnalyze.setItemBestInfoTrophyList(
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
fun BestAnalyzeBackOnPressed(
    viewModelAnalyze: ViewModelAnalyze,
) {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    val coroutineScope = rememberCoroutineScope()
    
    val state = viewModelAnalyze.state.collectAsState().value

    BackHandler(enabled = true) {

        if (state.detail.isNotEmpty()) {
            coroutineScope.launch {
               viewModelAnalyze.setScreen(detail = "", menu = state.menu)
            }
        } else {
            if (System.currentTimeMillis() - backPressedTime <= 400L) {
                // 앱 종료
                (context as Activity).finish()
            } else {
                backPressedState = true
                Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBestAnalyze(
    viewModelAnalyze: ViewModelAnalyze,
    root: String,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    getJsonFiles(
        platform = state.platform,
        type = state.type,
        root = root,
    ) {
        viewModelAnalyze.setJsonNameList(it)

        if (root == "WEEK") {
            if(state.week.isEmpty()){
                viewModelAnalyze.setDate(week = it.get(0))
            }
        } else {
            if(state.month.isEmpty()){
                viewModelAnalyze.setDate(month = it.get(0))
            }
        }
    }

    if(state.jsonNameList.isNotEmpty()){

        if(root == "WEEK"){
            getBestWeekTrophy(
                platform = state.platform,
                type = state.type,
                root = state.week
            ) {
                viewModelAnalyze.setWeekTrophyList(it)
            }
        } else {
            getBestMonthTrophy(
                platform = state.platform,
                type = state.type,
                root = state.month
            ) {
                viewModelAnalyze.setWeekTrophyList(it)
            }
        }

        getBookMap(
            platform = state.platform,
            type = state.type
        ) {
            viewModelAnalyze.setItemBookInfoMap(it)
        }

        viewModelAnalyze.setFilteredList()
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
    ){

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
            ) {
                itemsIndexed(state.jsonNameList) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = if (root == "WEEK") {
                                convertDateStringWeek(item)
                            } else {
                                convertDateStringMonth(item)
                            },
                            onClick = {
                                coroutineScope.launch {
                                    if (root == "WEEK") {
                                        viewModelAnalyze.setDate(week = item)
                                    } else {
                                        viewModelAnalyze.setDate(month = item)
                                    }

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = if (root == "WEEK") {
                                convertDateStringWeek(item)
                            } else {
                                convertDateStringMonth(item)
                            },
                            getValue = if (root == "WEEK") {
                                convertDateStringWeek(state.week)
                            } else {
                                convertDateStringMonth(state.month)
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.size(4.dp))
        }

        item {
            Spacer(modifier = Modifier.size(4.dp))
        }

        if (state.jsonNameList.isNotEmpty()) {
            itemsIndexed(state.filteredList) { index, item ->

                Box(
                    modifier = Modifier
                        .background(colorF6F6F6)
                        .padding(16.dp, 0.dp, 16.dp, 0.dp)
                ) {
                    ListBest(
                        item = item,
                        type = root,
                        index = index
                    ){
                        coroutineScope.launch {
                            viewModelAnalyze.setItemBookInfo(itemBookInfo = item)

                            getBookItemWeekTrophyDialog(
                                itemBookInfo = item,
                                type = state.type,
                                platform = state.platform
                            ) { itemBookInfo, itemBestInfoTrophyList ->
                                viewModelAnalyze.setItemBestInfoTrophyList(
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
        } else {
            item { ScreenEmpty(str = "데이터가 없습니다") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestAnalyzeList(
    drawerState: DrawerState?,
    viewModelAnalyze: ViewModelAnalyze,
    itemList : List<String> = novelListEng()
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelAnalyze.state.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {

            itemList.forEach { item ->
                Box(modifier = Modifier.padding(8.dp)) {
                    TabletContentWrapBtn(
                        onClick = {
                            coroutineScope.launch {

                                viewModelAnalyze.setScreen(
                                    menu = state.menu,
                                    detail = "${changePlatformNameKor(item)} ${state.menu}",
                                    platform = item,
                                    type = state.type
                                )
                                drawerState?.close()
                            }
                        },
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

                                Text(
                                    text = changePlatformNameKor(item),
                                    color = color000000,
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