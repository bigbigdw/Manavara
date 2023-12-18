package com.bigbigdw.manavara.dataBase.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.best.screen.BestBottomDialog
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.best.screen.BestDialog
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformColorEng
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.getRandomPlatform
import com.bigbigdw.manavara.util.keywordListEng
import com.bigbigdw.manavara.util.menuListDatabase
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.AlertOneBtn
import com.bigbigdw.manavara.util.screen.ScreenMenuItem
import com.bigbigdw.manavara.util.screen.ScreenTopbar
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
    val (getPlatformDialogOpen, setPlatformDialogOpen) = remember { mutableStateOf(false) }

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val platformModalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    BestAnalyzeBackOnPressed(viewModelDatabase = viewModelDatabase)

    val menuOption = if (currentRoute?.contains("NOVEL") == true) {
        "웹소설"
    } else {
        "웹툰"
    }

    val type = if (currentRoute?.contains("NOVEL") == true) {
        "NOVEL"
    } else {
        "COMIC"
    }

    var list = if (currentRoute?.contains("NOVEL") == true) {
        novelListEng()
    } else {
        comicListEng()
    }

    LaunchedEffect(context) {
        viewModelDatabase.sideEffects
            .onEach { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            .launchIn(coroutineScope)

        viewModelDatabase.setScreen(
            menu = "마나바라 베스트 DB ${menuOption}",
            type = type,
            platform = getRandomPlatform(list)
        )
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

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            floatingActionButton = {

                if (!state.menu.contains("신규 작품") &&
                    !state.menu.contains("투데이 장르 현황") &&
                    !state.menu.contains("투데이 장르 현황")) {
                    if (getPlatformDialogOpen) {
                        Dialog(
                            onDismissRequest = { setPlatformDialogOpen(false) },
                        ) {
                            AlertOneBtn(
                                isShow = {
                                    setPlatformDialogOpen(false)
                                },
                                btnText = "돌아가기",
                                modifier = Modifier.Companion.requiredWidth(360.dp),
                                contents = {

                                    ScreenChoosePlatform(
                                        list = list,
                                        isClose = { setPlatformDialogOpen(false) },
                                        viewModelDatabase = viewModelDatabase,
                                        modifier = Modifier.width(360.dp),
                                        fontColor = Color.Black,
                                        getValue = state.platform,
                                    )
                                }
                            )
                        }
                    }

                    ScreenChoosePlatformFab(
                        isOpen = { setPlatformDialogOpen(true) },
                        platform = state.platform
                    )
                }

            }, floatingActionButtonPosition = FabPosition.End
        ) {

            Row(
                modifier = Modifier
                    .padding(it)
                    .fillMaxHeight()
                    .background(color = colorF6F6F6)
            ) {
                ScreenDataBasePropertyList(
                    drawerState = drawerState,
                    viewModelDatabase = viewModelDatabase
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )

                ScreenDataBaseItems(
                    viewModelDatabase = viewModelDatabase,
                    drawerState = drawerState,
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen,
                    isExpandedScreen = isExpandedScreen
                )
            }
        }

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
                    ScreenTopbar(detail = state.detail, menu = state.menu) {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                },
                floatingActionButton = {

                    if (!state.menu.contains("신규 작품") &&
                        !state.menu.contains("투데이 장르 현황") &&
                        !state.menu.contains("투데이 장르 현황")) {
                        if (!platformModalSheetState.isVisible) {
                            ScreenChoosePlatformFab(
                                isOpen = {
                                    coroutineScope.launch {
                                        platformModalSheetState.show()
                                    }
                                },
                                platform = state.platform
                            )
                        }
                    }

                }, floatingActionButtonPosition = FabPosition.End
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

            if (modalSheetState.isVisible) {
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
                            BestBottomDialog(
                                itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                                item = state.itemBookInfo,
                                isExpandedScreen = isExpandedScreen,
                                modalSheetState = modalSheetState,
                                currentRoute = currentRoute,
                            )
                        }
                    },
                ) {}
            }

            if (!state.menu.contains("신규 작품") &&
                !state.menu.contains("투데이 장르 현황") &&
                !state.menu.contains("투데이 장르 현황")) {
                if (platformModalSheetState.isVisible) {
                    ModalBottomSheetLayout(
                        sheetState = platformModalSheetState,
                        sheetElevation = 50.dp,
                        sheetShape = RoundedCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp
                        ),
                        sheetContent = {

                            list = if (state.menu.contains("장르")) {
                                genreListEng()
                            } else if (state.menu.contains("키워드")) {
                                keywordListEng()
                            } else {
                                list
                            }

                            ScreenChoosePlatform(
                                list = list,
                                isClose = {
                                    coroutineScope.launch {
                                        platformModalSheetState.hide()
                                    }
                                },
                                viewModelDatabase = viewModelDatabase,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color20459E),
                                fontColor = Color.White,
                                getValue = state.platform
                            )
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

        menuListDatabase.forEachIndexed { index, item ->
            ScreenMenuItem(
                item = item,
                index = index,
                current = state.menu,
                onClick = {
                    coroutineScope.launch {
                        viewModelDatabase.setScreen(
                            menu = item.menu,
                        )
                        drawerState?.close()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenDataBaseItem(
    viewModelDatabase: ViewModelDatabase,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelDatabase.state.collectAsState().value

    Log.d("ScreenDataBaseItem", "state.menu == ${state.menu}")

    if (state.menu.contains("마나바라 베스트 DB")) {

        ScreenBookMap(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelDatabase = viewModelDatabase
        )

    } else if (state.menu.contains("주차별 베스트") || state.menu.contains("월별 베스트")) {

        ScreenBestAnalyze(
            viewModelDatabase = viewModelDatabase,
            root = if (state.menu.contains("주차별 베스트")) {
                "BEST_WEEK"
            } else if (state.menu.contains("월별 베스트")) {
                "BEST_MONTH"
            } else {
                "BEST_WEEK"
            },
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen
        )

    } else if (state.menu.contains("투데이 장르 현황")
        || state.menu.contains("투데이 키워드 현황")
    ) {

        ScreenGenreKeywordToday(
            viewModelDatabase = viewModelDatabase,
            dataType = if (state.menu.contains("장르")) {
                "GENRE"
            } else {
                "KEYWORD"
            }
        )

    } else if (state.menu.contains("주간 장르 현황") ||
        state.menu.contains("월간 장르 현황") ||
        state.menu.contains("주간 키워드 현황") ||
        state.menu.contains("월간 키워드 현황")
    ) {

        GenreDetailJson(
            viewModelDatabase = viewModelDatabase,
            menuType = if (state.menu.contains("주간 장르 현황")) {
                "주간"
            } else {
                "월간"
            },
            type = if (state.menu.contains("장르")) {
                "GENRE"
            } else {
                "KEYWORD"
            }
        )

    } else if (state.menu.contains("장르 리스트 작품") ||
        state.menu.contains("장르 리스트 현황") ||
        state.menu.contains("키워드 리스트 작품") ||
        state.menu.contains("키워드 리스트 현황")
    ) {

        ScreenGenreDetail(
            viewModelDatabase = viewModelDatabase,
            mode = if (state.menu.contains("장르 리스트 작품")) {
                "GENRE_BOOK"
            } else if (state.menu.contains("장르 리스트 현황")) {
                "GENRE_STATUS"
            } else if (state.menu.contains("키워드 리스트 작품")) {
                "KEYWORD_BOOK"
            } else if (state.menu.contains("키워드 리스트 현황")) {
                "KEYWORD_STATUS"
            } else {
                "KEYWORD_BOOK"
            },
        )

    } else if (state.menu.contains("투데이 장르 현황")
        || state.menu.contains("주차별 장르 현황")
        || state.menu.contains("월별 장르 현황")
        || state.menu.contains("투데이 키워드 현황")
        || state.menu.contains("주차별 키워드 현황")
        || state.menu.contains("월별 키워드 현황")
    ) {
        ScreenGenreKeyword(
            menuType = if (state.menu.contains("투데이")) {
                "투데이"
            } else if (state.menu.contains("주차별")) {
                "주간"
            } else {
                "월간"
            },
            dataType = if (state.menu.contains("장르")) {
                "GENRE"
            } else {
                "KEYWORD"
            },
            viewModelDatabase = viewModelDatabase
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

    } else if (state.menu.contains("신규 작품")) {

        ScreenBookList(
            viewModelDatabase = viewModelDatabase,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            list = if (state.type == "NOVEL") {
                novelListEng()
            } else {
                comicListEng()
            }
        )

    }
}


@Composable
fun ScreenChoosePlatform(
    list: List<String>,
    isClose: () -> Unit,
    viewModelDatabase: ViewModelDatabase?,
    modifier: Modifier,
    fontColor: Color,
    getValue: String
) {

    Column(modifier = modifier) {
        Text(
            modifier = modifier
                .padding(0.dp, 16.dp)
                .fillMaxWidth(),
            text = "원하시는 플랫폼을 선택해 주세요.",
            fontSize = 18.sp,
            fontWeight = FontWeight(weight = 700),
            textAlign = TextAlign.Center,
            color = fontColor,
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp).background(Color.Red))

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            columns = GridCells.Fixed(4)
        ) {
            itemsIndexed(list) { index, item ->

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (getValue == item) {
                            colorF6F6F6
                        } else {
                            Color.White
                        }
                    ),
                    onClick = {
                        viewModelDatabase?.setScreen(platform = item)
                        isClose()
                    },
                    contentPadding = PaddingValues(4.dp, 8.dp),
                    shape = RoundedCornerShape(0.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Image(
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(
                                id = getPlatformLogo(changePlatformNameKor(item))
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                        )

                        Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            text = changePlatformNameKor(item),
                            color = Color.Black,
                            fontWeight = FontWeight(weight = 700),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(32.dp).background(Color.Red))
    }
}

@Composable
fun ScreenChoosePlatformFab(
    platform: String,
    isOpen: () -> Unit,
) {
    FloatingActionButton(
        onClick = {
            isOpen()
        },
        containerColor = getPlatformColorEng(platform),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.size(4.dp))

            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(
                    id = getPlatformLogo(
                        changePlatformNameKor(platform)
                    )
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 4.dp),
                text = changePlatformNameKor(platform),
                color = Color.White,
                fontWeight = FontWeight(weight = 700),
                textAlign = TextAlign.Center
            )
        }
    }
}