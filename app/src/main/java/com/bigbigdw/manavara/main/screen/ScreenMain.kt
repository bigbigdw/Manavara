package com.bigbigdw.manavara.main.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1E1E20
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ScreenTest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(
    viewModelMain: ViewModelMain,
    widthSizeClass: WindowWidthSizeClass,
    viewModelBest: ViewModelBest

) {

    viewModelMain.setUserInfo()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
    val mainState = viewModelMain.state.collectAsState().value
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val (getMenu, setMenu) = remember { mutableStateOf("TODAY") }
    val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf(novelListEng()[0]) }
    val (getDetailType, setDetailType) = remember { mutableStateOf("NOVEL") }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    if (mainState.userInfo.userEmail.isNotEmpty()) {
        if (!isExpandedScreen) {

            ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

//                ModalDrawerSheet{}

                ScreenBestTabletList(
                    viewModelMain = viewModelMain,
                    setMenu = setMenu,
                    getMenu = getMenu,
                    setDetailPlatform = setDetailPlatform,
                    setDetailType = setDetailType,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                ) {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }

            }) {
                ScreenMainMobile(
                    navController = navController,
                    currentRoute = currentRoute,
                    viewModelMain = viewModelMain,
                    viewModelBest = viewModelBest,
                    isExpandedScreen = isExpandedScreen,
                    drawerState = drawerState,
                    setMenu = setMenu,
                    getMenu = getMenu,
                    setDetailPlatform = setDetailPlatform,
                    getDetailPlatform = getDetailPlatform,
                    setDetailType = setDetailType,
                    getDetailType = getDetailType,
                    listState = listState
                )
            }

        } else {
            ScreenMainTablet(
                currentRoute = currentRoute,
                navController = navController,
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                isExpandedScreen = isExpandedScreen,
                setMenu = setMenu,
                getMenu = getMenu,
                setDetailPlatform = setDetailPlatform,
                getDetailPlatform = getDetailPlatform,
                setDetailType = setDetailType,
                getDetailType = getDetailType,
                listState = listState
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMainTablet(
    currentRoute: String?,
    navController: NavHostController,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    setMenu: (String) -> Unit,
    getMenu: String,
    setDetailPlatform: (String) -> Unit,
    getDetailPlatform: String,
    setDetailType: (String) -> Unit,
    getDetailType: String,
    listState: LazyListState,
) {
    Row {
        TableAppNavRail(currentRoute = currentRoute ?: "", navController = navController)
        NavigationGraph(
            navController = navController,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen,
            viewModelBest = viewModelBest,
            setMenu = setMenu,
            getMenu = getMenu,
            setDetailPlatform = setDetailPlatform,
            getDetailPlatform = getDetailPlatform,
            setDetailType = setDetailType,
            getDetailType = getDetailType,
            listState = listState,
            modalSheetState = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenMainMobile(
    navController: NavHostController,
    currentRoute: String?,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    drawerState: DrawerState,
    setMenu: (String) -> Unit,
    getMenu: String,
    setDetailPlatform: (String) -> Unit,
    getDetailPlatform: String,
    setDetailType: (String) -> Unit,
    getDetailType: String,
    listState: LazyListState,
) {

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MainTopBar(
                setMenu = setMenu,
                getMenu = getMenu,
                setDrawer = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                })
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
                viewModelMain = viewModelMain,
                isExpandedScreen = isExpandedScreen,
                viewModelBest = viewModelBest,
                setMenu = setMenu,
                getMenu = getMenu,
                setDetailPlatform = setDetailPlatform,
                getDetailPlatform = getDetailPlatform,
                setDetailType = setDetailType,
                getDetailType = getDetailType,
                listState = listState,
                modalSheetState = modalSheetState
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
            ScreenTest()
        },
    ) {}
}

@Composable
fun MainTopBar(setDrawer: (Boolean) -> Unit, setMenu: (String) -> Unit, getMenu: String) {
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
                    text = "웹소설 베스트",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )

            }

        }

        Text(
            modifier = Modifier.clickable { setMenu("TODAY") },
            text = "투데이",
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            color = if (getMenu.contains("TODAY")) {
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
            modifier = Modifier.clickable { setMenu("WEEK") },
            text = "주간",
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            color = if (getMenu.contains("WEEK")) {
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
            modifier = Modifier.clickable { setMenu("MONTH") },
            text = "월간",
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            color = if (getMenu.contains("MONTH")) {
                color1E4394
            } else {
                color555b68
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomNavScreen(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        ScreemBottomItem.NOVEL,
        ScreemBottomItem.COMIC,
        ScreemBottomItem.FCM,
        ScreemBottomItem.JSON,
        ScreemBottomItem.TROPHY,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    setMenu: (String) -> Unit,
    getMenu: String,
    setDetailPlatform: (String) -> Unit,
    getDetailPlatform: String,
    setDetailType: (String) -> Unit,
    getDetailType: String,
    listState: LazyListState,
    modalSheetState: ModalBottomSheetState? = null,
) {

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.NOVEL.screenRoute
    ) {
        composable(ScreemBottomItem.NOVEL.screenRoute) {
            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                viewModelBest = viewModelBest,
                viewModelMain = viewModelMain,
                setMenu = setMenu,
                getMenu = getMenu,
                setDetailPlatform = setDetailPlatform,
                getDetailPlatform = getDetailPlatform,
                setDetailType = setDetailType,
                getDetailType = getDetailType,
                listState = listState,
                modalSheetState = modalSheetState
            )
        }
        composable(ScreemBottomItem.COMIC.screenRoute) {
            ScreenTest()
        }
        composable(ScreemBottomItem.FCM.screenRoute) {
            ScreenTest()
        }
        composable(ScreemBottomItem.JSON.screenRoute) {
            ScreenTest()
        }
        composable(ScreemBottomItem.TROPHY.screenRoute) {
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
        ScreemBottomItem.FCM,
        ScreemBottomItem.JSON,
        ScreemBottomItem.TROPHY,
    )

    NavigationRail(
        header = {

            Spacer(Modifier.size(8.dp))

            Card(
                modifier = Modifier
                    .wrapContentSize(),
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