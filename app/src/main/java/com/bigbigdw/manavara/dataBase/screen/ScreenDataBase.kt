package com.bigbigdw.manavara.dataBase.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
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
import com.bigbigdw.manavara.ui.theme.colorABD436
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFDC24E
import com.bigbigdw.manavara.ui.theme.colorFFAC59
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.keywordListEng
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenDataBase(
    isExpandedScreen: Boolean,
    currentRoute: String?,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state = viewModelDatabase.state.collectAsState().value
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    BestAnalyzeBackOnPressed(viewModelDatabase = viewModelDatabase)

    DisposableEffect(context) {

        viewModelDatabase.sideEffects
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

                ScreenDataBasePropertyList(
                    drawerState = drawerState,
                    viewModelDatabase = viewModelDatabase
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenDataBaseItems(
                    viewModelDatabase = viewModelDatabase,
                    drawerState = drawerState,
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen,
                    isExpandedScreen = isExpandedScreen
                )

            } else {
                BestAnalyzeBackOnPressed(viewModelDatabase = viewModelDatabase)

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenDataBasePropertyList(
                        drawerState = drawerState,
                        viewModelDatabase = viewModelDatabase
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenDataBaseTopbar(viewModelDatabase = viewModelDatabase) {
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
                            ScreenDataBaseItems(
                                viewModelDatabase = viewModelDatabase,
                                drawerState = drawerState,
                                modalSheetState = modalSheetState,
                                setDialogOpen = null,
                                isExpandedScreen = isExpandedScreen,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenDataBasePropertyList(
    drawerState: DrawerState?,
    viewModelDatabase: ViewModelDatabase
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDatabase.state.collectAsState().value

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
                    viewModelDatabase.setScreen(
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
            image = R.drawable.icon_novel_wht,
            title = "신규 작품",
            body = "최근에 등록된 작품 확인",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "신규 작품",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "신규 작품"
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_best_wht,
            title = "주차별 웹소설 베스트",
            body = "주차별 웹소설 베스트 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
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
            containerColor = colorEA927C,
            image = R.drawable.icon_best_wht,
            title = "월별 웹소설 베스트",
            body = "월별 웹소설 베스트 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "월별 웹소설 베스트",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "월별 웹소설 베스트"
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorABD436,
            image = R.drawable.icon_genre_wht,
            title = "투데이 장르 현황",
            body = "웹소설 플랫폼별 투데이 장르 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "투데이 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "투데이 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17FA0,
            image = R.drawable.icon_genre_wht,
            title = "주간 장르 현황",
            body = "웹소설 주간 주차별 장르 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "주간 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "주간 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color21C2EC,
            image = R.drawable.icon_genre_wht,
            title = "월간 장르 현황",
            body = "웹소설 플랫폼별 월간 장르 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "월간 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "월간 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color31C3AE,
            image = R.drawable.icon_genre_wht,
            title = "주차별 장르 현황",
            body = "웹소설 플랫폼별 주차별 장르 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "주차별 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "주차별 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color7C81FF,
            image = R.drawable.icon_genre_wht,
            title = "월별 장르 현황",
            body = "웹소설 플랫폼별 월별 장르 리스트",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "월별 장르 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "월별 장르 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color64C157,
            image = R.drawable.icon_genre_wht,
            title = "장르 리스트 작품",
            body = "장르별 작품 리스트 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "장르 리스트 작품",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "장르 리스트 작품"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17666,
            image = R.drawable.icon_genre_wht,
            title = "장르 리스트 현황",
            body = "장르별 랭킹 변동 현황",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "장르 리스트 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "장르 리스트 현황"
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_keyword_wht,
            title = "투데이 키워드 현황",
            body = "웹소설 주차별 투데이 키워드 현황",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "투데이 키워드 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "투데이 키워드 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color4996E8,
            image = R.drawable.icon_keyword_wht,
            title = "주간 키워드 현황",
            body = "웹소설 주간 키워드 현황",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "주간 키워드 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "주간 키워드 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = colorFDC24E,
            image = R.drawable.icon_keyword_wht,
            title = "월간 키워드 현황",
            body = "웹소설 월간 키워드 현황",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "월간 키워드 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "월간 키워드 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color80BF78,
            image = R.drawable.icon_keyword_wht,
            title = "주차별 키워드 현황",
            body = "웹소설 주차별 월별 키워드 현황",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "주차별 키워드 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "주차별 키워드 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color91CEC7,
            image = R.drawable.icon_keyword_wht,
            title = "월별 키워드 현황",
            body = "웹소설 플랫폼별 월별 키워드 현황",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "월별 키워드 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "월별 키워드 현황"
        )

        ItemMainSettingSingleTablet(
            containerColor = color79B4F8,
            image = R.drawable.icon_keyword_wht,
            title = "키워드 리스트 작품",
            body = "웹소설 키워드 리스트 작품 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "키워드 리스트 작품",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "키워드 리스트 작품"
        )

        ItemMainSettingSingleTablet(
            containerColor = color8AA6BD,
            image = R.drawable.icon_keyword_wht,
            title = "키워드 리스트 현황",
            body = "웹소설 키워드 리스트 작품 보기",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "키워드 리스트 현황",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "키워드 리스트 현황"
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color2EA259,
            image = R.drawable.icon_search_wht,
            title = "마나바라 DB 검색",
            body = "마나바라 DB 검색",
            current = state.menu,
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "마나바라 DB에 저장된 작품 검색",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "마나바라 DB 검색",
        )

        ItemMainSettingSingleTablet(
            containerColor = color808CF8,
            image = R.drawable.icon_search_wht,
            title = "작품 검색",
            body = "플랫폼과 무관하게 작품 검색 진행",
            current = "",
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "작품 검색",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "작품 검색",
        )

        ItemMainSettingSingleTablet(
            containerColor = colorFFAC59,
            image = R.drawable.icon_search_wht,
            title = "북코드 검색",
            body = "북코드로 작품 찾기",
            current = "",
            onClick = {
                coroutineScope.launch {
                    viewModelDatabase.setScreen(
                        detail = "",
                        menu = "북코드 검색",
                        type = "NOVEL",
                        platform = "JOARA"
                    )
                    drawerState?.close()
                }
            },
            value = "북코드 검색",
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenDataBaseItem(
    viewModelDatabase: ViewModelDatabase,
    drawerState: DrawerState?,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelDatabase.state.collectAsState().value

    if (state.menu.contains("베스트 웹소설 DB")) {

        ScreenBestDataBaseList(
            drawerState = drawerState,
            viewModelDatabase = viewModelDatabase
        )

    } else if (state.menu.contains("베스트 웹툰 DB")) {

        ScreenBestDataBaseList(
            drawerState = drawerState,
            viewModelDatabase = viewModelDatabase
        )

    } else if (state.menu.contains("주차별 웹소설 베스트") || state.menu.contains("월별 웹소설 베스트")) {

        ScreenBestDataBaseList(
            viewModelDatabase = viewModelDatabase,
            drawerState = drawerState,
        )

    } else if (state.menu.contains("투데이 장르 현황")
        || state.menu.contains("주차별 장르 현황")
        || state.menu.contains("월별 장르 현황")
        || state.menu.contains("주간 장르 현황")
        || state.menu.contains("월간 장르 현황")
    ) {

        ScreenBestDataBaseList(
            viewModelDatabase = viewModelDatabase,
            drawerState = drawerState,
            itemList = genreListEng()
        )

    } else if (state.menu.contains("장르 리스트 작품") || state.menu.contains("장르 리스트 현황")) {

        ScreenBestDataBaseList(
            viewModelDatabase = viewModelDatabase,
            drawerState = drawerState,
            itemList = genreListEng()
        )

    } else if (state.menu.contains("투데이 키워드 현황")
        || state.menu.contains("주차별 키워드 현황")
        || state.menu.contains("월별 키워드 현황")
        || state.menu.contains("주간 키워드 현황")
        || state.menu.contains("월간 키워드 현황")
    ) {

        ScreenBestDataBaseList(
            viewModelDatabase = viewModelDatabase,
            drawerState = drawerState,
            itemList = keywordListEng()
        )

    } else if (state.menu.contains("키워드 리스트 작품") || state.menu.contains("키워드 리스트 현황")) {

        ScreenBestDataBaseList(
            viewModelDatabase = viewModelDatabase,
            drawerState = drawerState,
            itemList = keywordListEng()
        )

    } else if (state.menu.contains("마나바라 DB 검색")) {

        ScreenSearchDataBase(
            viewModelDatabase = viewModelDatabase,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen
        )

    } else if (state.menu.contains("작품 검색")) {

        ScreenSearchDataBase(
            viewModelDatabase = viewModelDatabase,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen
        )

    } else if (state.menu.contains("북코드 검색")) {

        ScreenSearchDataBaseDetail(
            viewModelDatabase = viewModelDatabase
        )

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenAnalyzeItemDetail(
    viewModelDatabase: ViewModelDatabase,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelDatabase.state.collectAsState().value

    if (state.menu.contains("베스트 웹소설 DB")) {
        ScreenBookMap(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelDatabase = viewModelDatabase
        )

    } else if (state.menu.contains("주차별 웹소설 베스트") || state.menu.contains("월별 웹소설 베스트")) {
        ScreenBestAnalyze(
            viewModelDatabase = viewModelDatabase,
            root = if (state.menu.contains("주차별 웹소설 베스트")) {
                "BEST_WEEK"
            } else if (state.menu.contains("월별 웹소설 베스트")) {
                "BEST_MONTH"
            } else {
                ""
            },
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen
        )

    } else if (state.menu.contains("장르 리스트 작품") || state.menu.contains("장르 리스트 현황")) {

        ScreenGenreDetail(
            viewModelDatabase = viewModelDatabase,
            mode = if (state.menu.contains("장르 리스트 작품")) {
                "GENRE_BOOK"
            } else if (state.menu.contains("장르 리스트 현황")) {
                "GENRE_STATUS"
            } else {
                "GENRE_BOOK"
            },
        )

    } else if (state.menu.contains("투데이 장르 현황")
        || state.menu.contains("주차별 장르 현황")
        || state.menu.contains("월별 장르 현황")
    ) {
        ScreenGenre(
            menuType = if (state.menu.contains("투데이 장르 현황")) {
                "투데이"
            } else if (state.menu.contains("주차별 장르 현황")) {
                "주간"
            } else {
                "월간"
            },
            viewModelDatabase = viewModelDatabase
        )

    } else if (state.menu.contains("주간 장르 현황") || state.menu.contains("월간 장르 현황")) {

        GenreDetailJson(
            viewModelDatabase = viewModelDatabase,
            menuType = if (state.menu.contains("주간 장르 현황")) {
                "주간"
            } else {
                "월간"
            }
        )

    } else if (state.menu.contains("투데이 키워드 현황")
        || state.menu.contains("주차별 키워드 현황")
        || state.menu.contains("월별 키워드 현황")) {

        ScreenKeyword(
            menuType = if (state.menu.contains("투데이 키워드 현황")) {
                "투데이"
            } else if (state.menu.contains("주차별 키워드 현황")) {
                "주간"
            } else {
                "월간"
            },
            viewModelDatabase = viewModelDatabase,
        )

    } else if (state.menu.contains("주간 키워드 현황")
        || state.menu.contains("월간 키워드 현황")) {

        GenreDetailJson(
            viewModelDatabase = viewModelDatabase,
            menuType = if (state.menu.contains("주간 키워드 현황")) {
                "주간"
            } else {
                "월간"
            },
            type = "KEYWORD"
        )

    } else if (state.menu.contains("키워드 리스트 작품") || state.menu.contains("키워드 리스트 현황")) {

        ScreenGenreDetail(
            viewModelDatabase = viewModelDatabase,
            mode = if (state.menu.contains("키워드 리스트 작품")) {
                "KEYWORD_BOOK"
            } else if (state.menu.contains("키워드 리스트 현황")) {
                "KEYWORD_STATUS"
            } else {
                "KEYWORD_BOOK"
            },
        )

    }
}