package com.bigbigdw.manavara.main.screen


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.screen.ScreenBest
import com.bigbigdw.manavara.best.screen.ScreenBestPropertyList
import com.bigbigdw.manavara.best.screen.ScreenBestTopbar
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.manavara.screen.ScreenManavara
import com.bigbigdw.manavara.manavara.screen.ScreenManavaraPropertyList
import com.bigbigdw.manavara.manavara.screen.ScreenManavaraTopbar
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1E1E20
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.BackOnPressed
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import kotlinx.coroutines.launch
import postFCMAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    needDataUpdate: Boolean,
    viewModelManavara: ViewModelManavara,
    isExpandedScreen: Boolean
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = rememberLazyListState()

    Log.d("RECOMPOSE???", "----------")
    Log.d("RECOMPOSE???", "ScreenMain")

    if (isExpandedScreen) {

        Log.d("RECOMPOSE???", "ScreenMainTablet")

        ScreenMainTablet(
            currentRoute = currentRoute,
            navController = navController,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen,
            viewModelBest = viewModelBest,
            listState = listState,
            needDataUpdate = needDataUpdate,
            drawerState = drawerState,
            viewModelManavara = viewModelManavara
        )
        BackOnPressed()

    } else {

        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

            if(currentRoute == "NOVEL" || currentRoute == "COMIC"){
                ScreenBestPropertyList(
                    viewModelBest = viewModelBest,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                    drawerState = drawerState
                )
            } else if(currentRoute == "MANAVARA"){
                ScreenManavaraPropertyList(
                    viewModelManavara = viewModelManavara,
                )
            }

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
                viewModelManavara = viewModelManavara
            )
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
    drawerState: DrawerState,
    viewModelManavara: ViewModelManavara,
) {

    Log.d("RECOMPOSE???", "ScreenMainTablet")

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
            drawerState = drawerState,
            viewModelManavara = viewModelManavara
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
    viewModelManavara: ViewModelManavara,
) {
    val state = viewModelBest.state.collectAsState().value

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val coroutineScope = rememberCoroutineScope()

    if(currentRoute == "NOVEL"){
        if (state.type != "NOVEL") {
            viewModelBest.setBest(
                type = "NOVEL",
                platform = "JOARA",
                bestType = "TODAY_BEST",
                menu = changePlatformNameKor("JOARA")
            )
        }
    } else if(currentRoute == "COMIC"){
        if (state.type != "COMIC") {
            viewModelBest.setBest(
                type = "COMIC",
                platform = "NAVER_SERIES",
                bestType = "TODAY_BEST",
                menu = changePlatformNameKor("NAVER_SERIES")
            )
        }
    }

    Scaffold(
        topBar = {
            TopbarMain(
                viewModelBest = viewModelBest,
                currentRoute = currentRoute
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
                drawerState = drawerState,
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                viewModelManavara = viewModelManavara,
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
                    currentRoute = currentRoute,
                    modalSheetState = modalSheetState
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
    currentRoute: String?,
    setDrawer: (Boolean) -> Unit,
) {

    if(currentRoute == "NOVEL" || currentRoute == "COMIC"){
        ScreenBestTopbar(viewModelBest = viewModelBest, onClick = { setDrawer(true) })
    } else if(currentRoute == "MANAVARA"){
        ScreenManavaraTopbar(viewModelBest = viewModelBest, onClick = { setDrawer(true) })
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
    drawerState: DrawerState,
    viewModelManavara: ViewModelManavara,
) {

    Log.d("RECOMPOSE???", "NavigationGraph")

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.NOVEL.screenRoute
    ) {
        composable(ScreemBottomItem.NOVEL.screenRoute) {

            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                listState = listState,
                modalSheetState = modalSheetState,
                needDataUpdate = needDataUpdate,
                viewModelBest = viewModelBest,
                drawerState = drawerState
            )


        }
        composable(ScreemBottomItem.COMIC.screenRoute) {

            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                listState = listState,
                modalSheetState = modalSheetState,
                needDataUpdate = needDataUpdate,
                viewModelBest = viewModelBest,
                drawerState = drawerState
            )

            ScreenTest()
        }
        composable(ScreemBottomItem.MANAVARA.screenRoute) {

            ScreenManavara(
                isExpandedScreen = isExpandedScreen,
                modalSheetState = modalSheetState,
                viewModelManavara = viewModelManavara
            )

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
                        painter = painterResource(id = R.drawable.logo_transparents),
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