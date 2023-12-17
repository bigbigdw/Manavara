package com.bigbigdw.manavara.best.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
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
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.best.areListsEqual
import com.bigbigdw.manavara.best.getBestListTodayJson
import com.bigbigdw.manavara.best.getBestListTodayStorage
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListKor
import com.bigbigdw.manavara.util.getPlatformColor
import com.bigbigdw.manavara.util.getPlatformDescription
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.getRandomNovel
import com.bigbigdw.manavara.util.novelListKor
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenTest
import deleteJson
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    currentRoute: String?,
) {

    val context = LocalContext.current
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelBest: ViewModelBest = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val coroutineScope = rememberCoroutineScope()
    val state = viewModelBest.state.collectAsState().value
    val item = state.itemBookInfo
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(currentRoute) {

        val platform = getRandomNovel()

        runBlocking {
            getBestListTodayJson(
                context = context,
                platform = platform,
                type = currentRoute ?: ""
            ){ json ->

                if(json.isNotEmpty()){
                    getBestListTodayStorage(
                        context = context,
                        platform = platform,
                        type = currentRoute ?: "",
                        checkUpdate = true
                    ) { storage ->
                        if (json.isNotEmpty() && storage.isNotEmpty()) {

                            if (areListsEqual(json, storage)) {
                                deleteJson(
                                    context = context,
                                    platform = currentRoute ?: "",
                                    type = "NOVEL",
                                )
                            }
                        }
                    }
                }

            }
        }

        viewModelBest.setScreen(
            type = currentRoute ?: "",
            platform = platform,
            detail = "투데이 베스트",
            menu = changePlatformNameKor(platform)
        )

        coroutineScope.launch {
            viewModelBest.sideEffects
                .onEach { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                .launchIn(coroutineScope)

        }

    }

    if(state.platform.isNotEmpty()){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorF6F6F6)
        ) {
            if (isExpandedScreen) {

                val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }
                val (getFab, setFab) = remember { mutableStateOf(false) }

                if (getDialogOpen) {
                    BestDialog(
                        onDismissRequest = { setDialogOpen(false) },
                        itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                        item = item,
                        isExpandedScreen = isExpandedScreen
                    )
                }

                Scaffold(
                    floatingActionButton = {
                        Column {
                            if (getFab) {

                                BestScreenFab(
                                    viewModelBest = viewModelBest,
                                    setFab = {setFab(false)}
                                )

                            } else {
                                FloatingActionButton(
                                    onClick = {
                                        setFab(true)
                                    },
                                    containerColor = when (state.detail) {
                                        "투데이 베스트" -> {
                                            color4AD7CF
                                        }
                                        "주간 베스트" -> {
                                            color5372DE
                                        }
                                        else -> {
                                            color998DF9
                                        }
                                    },
                                ) {
                                    Text(
                                        text = when (state.detail) {
                                            "투데이 베스트" -> {
                                                "투데이"
                                            }
                                            "주간 베스트" -> {
                                                "주간"
                                            }
                                            else -> {
                                                "월간"
                                            }
                                        },
                                        color = Color.White,
                                        fontWeight = FontWeight(weight = 700)
                                    )
                                }
                            }
                        }
                    }, floatingActionButtonPosition = FabPosition.End
                ) {

                    Row(
                        modifier = Modifier
                            .padding(it)
                    ) {
                        ScreenBestPropertyList(
                            listState = listState,
                            drawerState = drawerState,
                            viewModelBest = viewModelBest
                        )

                        Spacer(
                            modifier = Modifier
                                .width(16.dp)
                                .fillMaxHeight()
                                .background(color = colorF6F6F6)
                        )

                        ScreenMainBestDetail(
                            listState = listState,
                            setDialogOpen = setDialogOpen,
                            viewModelBest = viewModelBest,
                        )
                    }
                }

            } else {

                val modalSheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
                    skipHalfExpanded = false
                )

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenBestPropertyList(
                        listState = listState,
                        drawerState = drawerState,
                        viewModelBest = viewModelBest
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenBestTopbar(viewModelBest = viewModelBest, onClick = {
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
                            ScreenMainBestItemDetail(
                                modalSheetState = modalSheetState,
                                setDialogOpen = null,
                                listState = listState,
                                viewModelBest = viewModelBest
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

                                if (currentRoute == "NOVEL" || currentRoute == "COMIC") {

                                    Spacer(modifier = Modifier.size(4.dp))

                                    BestBottomDialog(
                                        itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                                        item = state.itemBookInfo,
                                        isExpandedScreen = isExpandedScreen,
                                        modalSheetState = modalSheetState,
                                        currentRoute = currentRoute,
                                    )
                                } else {
                                    ScreenTest()
                                }
                            },
                        ) {}
                    }
                }

                BackOnPressedMobile(modalSheetState = modalSheetState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestPropertyList(
    listState: LazyListState,
    drawerState: DrawerState?,
    viewModelBest: ViewModelBest
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelBest.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .width(330.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(
            if (state.type == "NOVEL") {
                novelListKor()
            } else {
                comicListKor()
            }
        ) { _, item ->

            ItemMainSettingSingleTablet(
                containerColor = getPlatformColor(item),
                image = getPlatformLogo(item),
                title = item,
                body = getPlatformDescription(item),
                current = state.menu,
                value = item,
                onClick = {
                    coroutineScope.launch {
                        viewModelBest.setScreen(
                            menu = item,
                            platform = changePlatformNameEng(item),
                            type = state.type,
                        )
                        listState.scrollToItem(index = 0)
                        drawerState?.close()
                    }
                },
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenMainBestDetail(
    listState: LazyListState,
    setDialogOpen: (Boolean) -> Unit,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
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
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = "${state.menu} ${state.detail}",
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )
        }

        ScreenMainBestItemDetail(
            modalSheetState = null,
            setDialogOpen = setDialogOpen,
            listState = listState,
            viewModelBest = viewModelBest,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMainBestItemDetail(
    modalSheetState: ModalBottomSheetState? = null,
    setDialogOpen: ((Boolean) -> Unit)?,
    listState: LazyListState,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value

    if (state.detail.contains("투데이 베스트")) {

        ScreenTodayBest(
            listState = listState,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelBest = viewModelBest,
        )

    } else if (state.detail.contains("주간 베스트")) {

        ScreenTodayWeek(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelBest = viewModelBest
        )

    } else if (state.detail.contains("월간 베스트")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayMonth(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelBest = viewModelBest
        )

    }
}

@Composable
fun ScreenBestTopbar(viewModelBest: ViewModelBest, onClick: () -> Unit) {
    val state = viewModelBest.state.collectAsState().value

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
                    text = "${changePlatformNameKor(state.platform)} 베스트",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )

            }

        }

        Text(
            modifier = Modifier.clickable {
                viewModelBest.setScreen(detail = "TODAY_BEST")
            },
            text = "투데이",
            fontSize = 18.sp,
            textAlign = TextAlign.Left,
            color = if (state.detail.contains("TODAY_BEST")) {
                color1E4394
            } else {
                color555b68
            },
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier
                .wrapContentWidth()
                .width(16.dp)
        )

        Text(
            modifier = Modifier.clickable {
                viewModelBest.setScreen(detail = "WEEK_BEST")
            },
            text = "주간",
            fontSize = 18.sp,
            textAlign = TextAlign.Left,
            color = if (state.detail.contains("WEEK_BEST")) {
                color1E4394
            } else {
                color555b68
            },
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier
                .wrapContentWidth()
                .width(16.dp)
        )

        Text(
            modifier = Modifier.clickable {
                viewModelBest.setScreen(detail = "MONTH_BEST")
            },
            text = "월간",
            fontSize = 18.sp,
            textAlign = TextAlign.Left,
            color = if (state.detail.contains("MONTH_BEST")) {
                color1E4394
            } else {
                color555b68
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BestDialog(
    onDismissRequest: () -> Unit,
    itemBestInfoTrophyList: ArrayList<ItemBestInfo>,
    item: ItemBookInfo,
    isExpandedScreen: Boolean
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelMain: ViewModelMain = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    val context = LocalContext.current
    val state = viewModelMain.state.collectAsState().value

    LaunchedEffect(item.bookCode) {
        viewModelMain.setIsPicked(
            type = "NOVEL",
            platform = item.platform,
            bookCode = item.bookCode
        )
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        AlertTwoBtn(
            onClickLeft = {
                if (state.isPicked) {
                    viewModelMain.setUnPickBook(
                        type = "NOVEL",
                        platform = item.platform,
                        item = item,
                        context = context
                    )
                } else {
                    viewModelMain.setPickBook(
                        type = "NOVEL",
                        platform = item.platform,
                        item = item,
                        context = context
                    )
                }
            },
            onClickRight = {
                val intent = Intent(context, ActivityBestDetail::class.java)
                intent.putExtra("BOOKCODE", item.bookCode)
                intent.putExtra("PLATFORM", item.platform)
                intent.putExtra("TYPE", item.platform)
                context.startActivity(intent)
                onDismissRequest()
            },
            btnLeft = if (state.isPicked) {
                "작품 PICK 해제"
            } else {
                "작품 PICK 하기"
            },
            btnRight = "작품 보러가기",
            modifier = Modifier.requiredWidth(400.dp),
            contents = {
                ScreenDialogBest(
                    item = item,
                    trophy = itemBestInfoTrophyList,
                    isExpandedScreen = isExpandedScreen,
                    isPicked = state.isPicked,
                    btnPickText = if (state.isPicked) {
                        "작품 PICK 해제"
                    } else {
                        "작품 PICK 하기"
                    },
                    onClickLeft = {}
                ) {}
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BestBottomDialog(
    itemBestInfoTrophyList: ArrayList<ItemBestInfo>,
    item: ItemBookInfo,
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState,
    currentRoute: String
) {
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelMain: ViewModelMain = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val state = viewModelMain.state.collectAsState().value

    LaunchedEffect(item.bookCode) {
        viewModelMain.setIsPicked(
            type = "NOVEL",
            platform = item.platform,
            bookCode = item.bookCode
        )
    }

    ScreenDialogBest(
        item = item,
        trophy = itemBestInfoTrophyList,
        isExpandedScreen = isExpandedScreen,
        isPicked = state.isPicked,
        needTrophyList = false,
        btnPickText = if (state.isPicked) {
            "작품 PICK 해제"
        } else {
            "작품 PICK 하기"
        },
        onClickLeft = {
            if (state.isPicked) {
                viewModelMain.setUnPickBook(
                    type = "NOVEL",
                    platform = item.platform,
                    item = item,
                    context = context
                )
            } else {
                viewModelMain.setPickBook(
                    type = "NOVEL",
                    platform = item.platform,
                    item = item,
                    context = context
                )
            }
        },
        onClickRight = {
            coroutineScope.launch {
                val intent = Intent(context, ActivityBestDetail::class.java)
                intent.putExtra("BOOKCODE", item.bookCode)
                intent.putExtra("PLATFORM", item.platform)
                intent.putExtra("TYPE", currentRoute)
                context.startActivity(intent)
                modalSheetState.hide()
            }
        })
}

@Composable
fun BestScreenFab(viewModelBest: ViewModelBest, setFab: () -> Unit) {
    FloatingActionButton(
        onClick = {
            setFab()
            viewModelBest.setScreen(
                detail = "투데이 베스트",
            )
        },
        containerColor = color4AD7CF,
    ) {
        Text(
            text = "투데이",
            color = Color.White,
            fontWeight = FontWeight(weight = 700)
        )
    }

    Spacer(modifier = Modifier.size(16.dp))

    FloatingActionButton(
        onClick = {
            setFab()
            viewModelBest.setScreen(
                detail = "주간 베스트",
            )
        },
        containerColor = color5372DE,
    ) {
        Text(
            text = "주간",
            color = Color.White,
            fontWeight = FontWeight(weight = 700)
        )
    }

    Spacer(modifier = Modifier.size(16.dp))

    FloatingActionButton(
        onClick = {
            setFab()
            viewModelBest.setScreen(
                detail = "월간 베스트",
            )
        },
        containerColor = color998DF9,
    ) {
        Text(
            text = "월간",
            color = Color.White,
            fontWeight = FontWeight(weight = 700)
        )
    }
}
