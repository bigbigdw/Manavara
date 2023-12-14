package com.bigbigdw.manavara.main.screen


import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bigbigdw.manavara.dataBase.screen.ScreenDataBase
import com.bigbigdw.manavara.best.screen.ScreenBest
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.best.areListsEqual
import com.bigbigdw.manavara.best.getBestListTodayJson
import com.bigbigdw.manavara.best.getBestListTodayStorage
import com.bigbigdw.manavara.manavara.screen.ScreenManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.BackOnPressed
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import deleteJson
import kotlinx.coroutines.runBlocking
import postFCMAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(
    isExpandedScreen: Boolean
) {

    val context = LocalContext.current

    DisposableEffect(context){

        init(context)

        onDispose {
            // 컴포넌트가 detached 될 때 실행되는 코드
            // 이 부분에 필요한 clean-up 코드를 작성할 수 있습니다.
        }
    }


    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    if (isExpandedScreen) {

        ScreenMainTablet(
            currentRoute = currentRoute,
            navController = navController,
            isExpandedScreen = isExpandedScreen,
            drawerState = drawerState,
        )
        BackOnPressed()

    } else {

        ScreenMainMobile(
            navController = navController,
            currentRoute = currentRoute,
            isExpandedScreen = isExpandedScreen,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainTablet(
    currentRoute: String?,
    navController: NavHostController,
    isExpandedScreen: Boolean,
    drawerState: DrawerState,
) {

    Row {
        TableAppNavRail(currentRoute = currentRoute ?: "", navController = navController)

        NavigationGraph(
            navController = navController,
            isExpandedScreen = isExpandedScreen,
            currentRoute = currentRoute
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainMobile(
    navController: NavHostController,
    currentRoute: String?,
    isExpandedScreen: Boolean,
) {

    Scaffold(
        bottomBar = { BottomNavScreen(navController = navController, currentRoute = currentRoute) }
    ) {
        Box(
            Modifier
                .padding(it)
                .background(color = colorF6F6F6)
                .fillMaxSize()
        ) {
            NavigationGraph(
                navController = navController,
                isExpandedScreen = isExpandedScreen,
                currentRoute = currentRoute,
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
        ScreemBottomItem.NOVELDB,
        ScreemBottomItem.WEBTOONDB,
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

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isExpandedScreen: Boolean,
    currentRoute: String?,
) {

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.NOVEL.screenRoute
    ) {
        composable(ScreemBottomItem.NOVEL.screenRoute) {

            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                currentRoute = currentRoute
            )

        }
        composable(ScreemBottomItem.COMIC.screenRoute) {

            ScreenBest(
                isExpandedScreen = isExpandedScreen,
                currentRoute = currentRoute
            )

        }
        composable(ScreemBottomItem.NOVELDB.screenRoute) {

            ScreenDataBase(
                isExpandedScreen = isExpandedScreen,
                currentRoute = currentRoute
            )

        }
        composable(ScreemBottomItem.WEBTOONDB.screenRoute) {

            ScreenDataBase(
                isExpandedScreen = isExpandedScreen,
                currentRoute = currentRoute
            )

        }
        composable(ScreemBottomItem.MANAVARA.screenRoute) {

            ScreenManavara(
                isExpandedScreen = isExpandedScreen
            )

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
        ScreemBottomItem.NOVELDB,
        ScreemBottomItem.WEBTOONDB,
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

fun init(context: Context) {
    for (platform in novelListEng()) {
        runBlocking {
            getBestListTodayJson(
                context = context,
                platform = platform,
                type = "NOVEL",
                checkUpdate = true
            ){ json ->

                if(json.isNotEmpty()){
                    getBestListTodayStorage(
                        context = context,
                        platform = platform,
                        type = "NOVEL",
                        checkUpdate = true
                    ) { storage ->
                        if (json.isNotEmpty() && storage.isNotEmpty()) {

                            if (areListsEqual(json, storage)) {
                                deleteJson(
                                    context = context,
                                    platform = platform,
                                    type = "NOVEL",
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}