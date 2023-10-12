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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
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

    val (getMenu, setMenu) = remember { mutableStateOf("") }
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
            listState = listState
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
        topBar = { MainTopBar(setDrawer = {
            coroutineScope.launch {
                drawerState.open()
            }
        }) },
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
                listState = listState
            )
        }
    }
//    ModalBottomSheetLayout(
//        sheetState = modalSheetState,
//        sheetElevation = 50.dp,
//        sheetShape = RoundedCornerShape(
//            topStart = 25.dp,
//            topEnd = 25.dp
//        ),
//        sheetContent = {
//            ScreenTest()
//        },
//    ) {}
}

@Composable
fun MainTopBar(setDrawer: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = Color.Cyan)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(22.dp)
                    .clickable { setDrawer(true) }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
                .clickable { }
        )

        Spacer(
            modifier = Modifier
                .wrapContentWidth()
                .width(16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
                .clickable { }
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
        backgroundColor = colorDCDCDD,
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
                listState = listState
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
            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
            )
        },
        modifier = Modifier.background(color = color1E1E20)
    ) {
        Spacer(Modifier.weight(1f))

        items.forEach { item ->
            NavigationRailItem(
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
                        color = color1E4394
                    )
                },
                alwaysShowLabel = false
            )
        }

        Spacer(Modifier.weight(1f))
    }
}