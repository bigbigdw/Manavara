package com.bigbigdw.manavara.main.screen


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.bigbigdw.manavara.util.screen.BtnMobile
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.event.StateBest
import com.bigbigdw.manavara.best.screen.ScreenBest
import com.bigbigdw.manavara.best.screen.ScreenBestPropertyList
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1E1E20
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.BackOnPressed
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import postFCMAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(
    viewModelMain: ViewModelMain,
    widthSizeClass: WindowWidthSizeClass,
    viewModelBest: ViewModelBest,
    needDataUpdate: Boolean
) {
    Log.d("RECOMPOSE", "-----------------")
    Log.d("RECOMPOSE", "1")

    val mainState by remember { derivedStateOf { viewModelMain.state } }
    val bestState by remember { derivedStateOf { viewModelBest.state } }

    LaunchedEffect(viewModelMain) {
        viewModelMain.setUserInfo()
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val listState = rememberLazyListState()

    if (mainState.collectAsState().value.userInfo.userEmail.isNotEmpty()) {
        if (!isExpandedScreen) {

            ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                ScreenBestPropertyList(
                    viewModelBest = viewModelBest,
                    bestState = bestState,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                    drawerState = drawerState
                )

            }) {
                ScreenMainMobile(
                    navController = navController,
                    currentRoute = currentRoute,
                    isExpandedScreen = isExpandedScreen,
                    drawerState = drawerState,
                    listState = listState,
                    needDataUpdate = needDataUpdate,
                    viewModelMain = viewModelMain,
                    viewModelBest = viewModelBest,
                    bestState = bestState,
                )
            }

        } else {
            ScreenMainTablet(
                currentRoute = currentRoute,
                navController = navController,
                isExpandedScreen = isExpandedScreen,
                listState = listState,
                needDataUpdate = needDataUpdate,
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                bestState = bestState,
                drawerState = drawerState
            )
            BackOnPressed()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainTablet(
    currentRoute: String?,
    navController: NavHostController,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    listState: LazyListState,
    needDataUpdate: Boolean,
    bestState: StateFlow<StateBest>,
    drawerState: DrawerState,
) {

    Row {
        TableAppNavRail(currentRoute = currentRoute ?: "", navController = navController)
        NavigationGraph(
            navController = navController,
            isExpandedScreen = isExpandedScreen,
            modalSheetState = null,
            needDataUpdate = needDataUpdate,
            listState = listState,
            viewModelMain = viewModelMain,
            viewModelBest = viewModelBest,
            bestState = bestState,
            drawerState = drawerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenMainMobile(
    navController: NavHostController,
    currentRoute: String?,
    isExpandedScreen: Boolean,
    drawerState: DrawerState,
    listState: LazyListState,
    needDataUpdate: Boolean,
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    bestState: StateFlow<StateBest>,
) {
    val state = bestState.collectAsState().value

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopbarMain(
                viewModelBest = viewModelBest
            ) {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        },
        bottomBar = { BottomNavScreen(navController = navController, currentRoute = currentRoute) }
    ) {
        Box(
            Modifier
                .padding(it)
                .background(color = color1E1E20)
                .fillMaxSize()
        ) {
            NavigationGraph(
                navController = navController,
                isExpandedScreen = isExpandedScreen,
                modalSheetState = modalSheetState,
                needDataUpdate = needDataUpdate,
                listState = listState,
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                bestState = bestState,
                drawerState = drawerState
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

            if(currentRoute == "NOVEL" || currentRoute == "COMIC"){

                Spacer(modifier = Modifier.size(4.dp))

                ScreenDialogBest(
                    item = state.itemBookInfo,
                    trophy = state.itemBestInfoTrophyList,
                    isExpandedScreen = isExpandedScreen,
                    currentRoute = currentRoute
                )
            } else {
                ScreenTest()
            }
        },
    ) {}

    BackOnPressedMobile(modalSheetState = modalSheetState)
}

@Composable
fun TopbarMain(
    viewModelBest: ViewModelBest,
    setDrawer: (Boolean) -> Unit,
) {

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
                modifier = Modifier.clickable { setDrawer(true) }) {
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

        if(state.bestType != "USER_OPTION"){
            Text(
                modifier = Modifier.clickable { viewModelBest.setBest(bestType = "TODAY_BEST") },
                text = "투데이",
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                color = if (state.bestType.contains("TODAY_BEST")) {
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
                modifier = Modifier.clickable { viewModelBest.setBest(bestType = "WEEK_BEST") },
                text = "주간",
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                color = if (state.bestType.contains("WEEK_BEST")) {
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
                modifier = Modifier.clickable { viewModelBest.setBest(bestType = "MONTH_BEST") },
                text = "월간",
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                color = if (state.bestType.contains("MONTH_BEST")) {
                    color1E4394
                } else {
                    color555b68
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomNavScreen(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        ScreemBottomItem.NOVEL,
        ScreemBottomItem.COMIC,
        ScreemBottomItem.MANAVARA,
        ScreemBottomItem.PICK,
        ScreemBottomItem.EVENT,
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = color1E4394
    ) {

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = if (currentRoute == item.screenRoute) {
                            painterResource(id = item.iconOn)
                        } else {
                            painterResource(id = item.iconOff)
                        },
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 13.sp,
                        color = color1E4394
                    )
                },
                selected = currentRoute == item.screenRoute,
                selectedContentColor = color1E4394,
                unselectedContentColor = color555b68,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState? = null,
    needDataUpdate: Boolean,
    listState: LazyListState,
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    bestState: StateFlow<StateBest>,
    drawerState: DrawerState,
) {

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.NOVEL.screenRoute
    ) {
        composable(ScreemBottomItem.NOVEL.screenRoute) {

            if (!novelListEng().contains(bestState.collectAsState().value.platform)) {
                viewModelBest.setBest(
                    type = "NOVEL",
                    platform = "JOARA",
                    bestType = "TODAY_BEST",
                    menu = changePlatformNameKor("JOARA")
                )
            } else {
                viewModelBest.setBest(type = "NOVEL")
            }

            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                listState = listState,
                modalSheetState = modalSheetState,
                needDataUpdate = needDataUpdate,
                viewModelBest = viewModelBest,
                bestState = bestState,
                drawerState = drawerState
            )
        }
        composable(ScreemBottomItem.COMIC.screenRoute) {

            if (!novelListEng().contains(bestState.collectAsState().value.platform)) {
                viewModelBest.setBest(
                    type = "COMIC",
                    platform = "NAVER_SERIES",
                    bestType = "TODAY_BEST",
                    menu = changePlatformNameKor("NAVER_SERIES")
                )
            } else {
                viewModelBest.setBest(type = "COMIC")
            }

            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                listState = listState,
                modalSheetState = modalSheetState,
                needDataUpdate = needDataUpdate,
                viewModelBest = viewModelBest,
                bestState = bestState,
                drawerState = drawerState
            )
        }
        composable(ScreemBottomItem.MANAVARA.screenRoute) {

//            setType("MANAVARA")

//            ScreenManavara(
//                isExpandedScreen = isExpandedScreen,
//                viewModelBest = viewModelBest,
//                viewModelMain = viewModelMain,
//                setMenu = setMenu,
//                getMenu = getMenu,
//                setPlatform = setPlatform,
//                getPlatform = getPlatform,
//                getType = getType,
//                listState = listState,
//                setBestType = setBestType,
//                getBestType = getBestType,
//                modalSheetState = modalSheetState
//            )

            ScreenTest()
        }
        composable(ScreemBottomItem.PICK.screenRoute) {
            ScreenTest()
        }
        composable(ScreemBottomItem.EVENT.screenRoute) {
            ScreenTest()
        }
    }
}

@Composable
fun TableAppNavRail(
    currentRoute: String,
    navController: NavHostController
) {

    val items = listOf(
        ScreemBottomItem.NOVEL,
        ScreemBottomItem.COMIC,
        ScreemBottomItem.MANAVARA,
        ScreemBottomItem.PICK,
        ScreemBottomItem.EVENT,
    )

    val context = LocalContext.current

    NavigationRail(
        header = {

            Spacer(Modifier.size(8.dp))

            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        Toast
                            .makeText(context, "클릭클릭", Toast.LENGTH_SHORT)
                            .show()
                    },
                colors = CardDefaults.cardColors(containerColor = colorDCDCDD),
                shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(44.dp)
                        .width(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.ic_launcher),
                        contentDescription = null,
                        modifier = Modifier
                            .height(32.dp)
                            .width(32.dp)
                    )
                }
            }
        },
        modifier = Modifier.background(color = Color.White)
    ) {
        Spacer(Modifier.weight(1f))

        items.forEach { item ->
            NavigationRailItem(
                colors = NavigationRailItemDefaults
                    .colors(
                        selectedIconColor = color1E4394,
                        selectedTextColor = color1E4394,
                        unselectedIconColor = color555b68,
                        unselectedTextColor = color555b68,
                        indicatorColor = colorDCDCDD
                    ),
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    androidx.compose.material3.Icon(
                        painter = if (currentRoute == item.screenRoute) {
                            painterResource(id = item.iconOn)
                        } else {
                            painterResource(id = item.iconOff)
                        },
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 13.sp,
                        color = color1E4394,
                        fontWeight = FontWeight(weight = 700)
                    )
                },
                alwaysShowLabel = false
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {

    val context = LocalContext.current
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    Column {

        ItemTabletTitle(str = "문의 사항 전송")

        TabletContentWrap {
            TextField(
                value = getFCM.title,
                onValueChange = {
                    setFCM(getFCM.copy(title = it))
                },
                label = { androidx.compose.material3.Text("푸시 알림 제목", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(16.dp))

            TextField(
                value = getFCM.body,
                onValueChange = {
                    setFCM(getFCM.copy(body = it))
                },
                label = { androidx.compose.material3.Text("푸시 알림 내용", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                BtnMobile(
                    func = { postFCMAlert(context = context, getFCM = getFCM) },
                    btnText = "문의사항 등록"
                )
            }
        }

        Spacer(modifier = Modifier.size(60.dp))
    }
}