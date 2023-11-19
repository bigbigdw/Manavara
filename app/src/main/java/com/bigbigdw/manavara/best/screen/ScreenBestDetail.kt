package com.bigbigdw.manavara.best.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.viewModels.ViewModelBestDetail
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color02BC77
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color1E1E20
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.getBestDetailDescription
import com.bigbigdw.manavara.util.getBestDetailLogo
import com.bigbigdw.manavara.util.getBestDetailLogoMobile
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import getRandomColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDetail(
    viewModelBestDetail: ViewModelBestDetail,
    widthSizeClass: WindowWidthSizeClass,
    platform: String,
    bookCode: String,
    type: String
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    val context = LocalContext.current

    LaunchedEffect(bookCode) {
        viewModelBestDetail.setBestDetailInfo(
            platform = platform,
            bookCode = bookCode,
            context = context
        )
    }

    val (getMenu, setMenu) = remember { mutableStateOf("") }
    val item = viewModelBestDetail.state.collectAsState().value.itemBestDetailInfo
    val itemBestInfo = viewModelBestDetail.state.collectAsState().value.itemBestInfo

    if (!isExpandedScreen) {

        if (getMenu.isEmpty()) {
            setMenu("작품 상세")
        }

        viewModelBestDetail.setManavaraBestInfo(
            platform = platform,
            bookCode = bookCode,
            type = type
        )

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

            DrawerBestDetail(
                setter = setMenu,
                getter = getMenu,
                drawerState = drawerState,
                itemBestInfo = itemBestInfo,
                item = item
            )

        }) {
            Scaffold(
                topBar = {
                    TopbarBestDetail(
                        setter = setMenu,
                        getter = getMenu,
                        setDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                },
                bottomBar = {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = color8F8F8F),
                            shape = RoundedCornerShape(0.dp),
                            onClick = {  },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),

                        ) {
                            Text(
                                text = "취소",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                            shape = RoundedCornerShape(0.dp),
                            onClick = {
                                viewModelBestDetail.gotoUrl(
                                    platform = platform,
                                    bookCode = bookCode,
                                    context = context
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                        ) {
                            Text(
                                text = "작품 보러가기",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            ) {

                Box(
                    Modifier
                        .padding(it)
                        .background(color = colorF6F6F6)
                        .fillMaxSize()
                ) {

                    ScreenItemBestDetailMenu(
                        item = item,
                        viewModelBestDetail = viewModelBestDetail,
                        platform = platform,
                        bookCode = bookCode,
                        isExpandedScreen = isExpandedScreen,
                        getMenu = getMenu,
                        type = type
                    )
                }

            }
        }

    } else {

        ScreenTabletBestDetail(
            viewModelBestDetail = viewModelBestDetail,
            isExpandedScreen = isExpandedScreen,
            platform = platform,
            bookCode = bookCode,
            type = type,
            getMenu = getMenu,
            setMenu = setMenu,
            item = item
        )

        BestDetailBackOnPressed(
            getMenu = getMenu,
            setMenu = setMenu
        )
    }
}

@Composable
fun ScreenTabletBestDetail(
    viewModelBestDetail: ViewModelBestDetail,
    isExpandedScreen: Boolean,
    platform: String,
    bookCode: String,
    type: String,
    getMenu: String,
    setMenu: (String) -> Unit,
    item: ItemBestDetailInfo
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                ScreenItemBestDetailMenu(
                    item = item,
                    viewModelBestDetail = viewModelBestDetail,
                    platform = platform,
                    bookCode = bookCode,
                    isExpandedScreen = isExpandedScreen,
                    getMenu = getMenu,
                    type = type
                )

                Spacer(modifier = Modifier.size(16.dp))

                if (getMenu.isNotEmpty()) {
                    ScreenBestDetailTabs(
                        getMenu = getMenu,
                        viewModelBestDetail = viewModelBestDetail,
                        platform = platform,
                        bookCode = bookCode,
                        type = type
                    )
                } else {
                    ScreenBestDetailTabsEmpty(
                        viewModelBestDetail = viewModelBestDetail,
                        platform = platform,
                        bookCode = bookCode,
                        type = type,
                        setMenu = setMenu,
                        item = item
                    )
                }

            }
        }
    }
}

@Composable
fun ScreenItemBestDetailMenu(
    item: ItemBestDetailInfo,
    viewModelBestDetail: ViewModelBestDetail,
    platform: String,
    bookCode: String,
    isExpandedScreen: Boolean,
    getMenu: String,
    type: String
) {

    val modifier = if(isExpandedScreen){
        Modifier.width(400.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(color = colorF6F6F6)
            .padding(16.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        if (isExpandedScreen) {
            Text(
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = "베스트 작품 상세",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight(weight = 700)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        ScreenItemBestDetailCard(
            item = item,
            viewModelBestDetail = viewModelBestDetail,
            platform = platform,
            bookCode = bookCode,
            type = type,
            isExpandedScreen = isExpandedScreen,
            getMenu = getMenu
        )

    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenBestDetailTabsEmpty(
    viewModelBestDetail: ViewModelBestDetail,
    platform: String,
    bookCode: String,
    type: String,
    setMenu: (String) -> Unit,
    item: ItemBestDetailInfo
) {

    viewModelBestDetail.setManavaraBestInfo(
        platform = platform,
        bookCode = bookCode,
        type = type
    )

    val bestItem = viewModelBestDetail.state.collectAsState().value.itemBestInfo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "작품 상세 정보",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(16.dp))

        item.tabInfo.forEachIndexed { index, title ->

            if(title.contains("분석")){
                if(bestItem.total != 0){
                    ScreenBestDetailAnalyzeBtn(
                        title = title,
                        onClick = { setMenu(title) },
                        isLast = index != item.tabInfo.size - 1
                    )
                }
            } else {
                ScreenBestDetailAnalyzeBtn(
                    title = title,
                    onClick = { setMenu(title) },
                    isLast = index != item.tabInfo.size - 1
                )
            }
        }

        if(bestItem.total != 0){
            ScreenItemBestDetailManavara(bestItem = bestItem)
        }

    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenBestDetailTabs(
    getMenu: String,
    viewModelBestDetail: ViewModelBestDetail,
    platform: String,
    bookCode: String,
    type: String
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
    ) {

        if (getMenu.isNotEmpty()) {
            item { Spacer(modifier = Modifier.size(16.dp)) }

            item {
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
                        text = getMenu,
                        fontSize = 24.sp,
                        color = color000000,
                        fontWeight = FontWeight(weight = 700)
                    )
                }
            }
        }

        item {
            ScreenBestItemDetailTabItem(
                viewModelBestDetail = viewModelBestDetail,
                item = ItemBestDetailInfo(),
                isExpandedScreen = true,
                getMenu = getMenu,
                platform = platform,
                bookCode = bookCode,
                type = type
            )
        }
    }
}

@Composable
fun ScreenBestItemDetailTabItem(
    viewModelBestDetail: ViewModelBestDetail,
    item: ItemBestDetailInfo,
    isExpandedScreen: Boolean,
    getMenu: String,
    platform: String,
    bookCode: String,
    type: String
) {

    val state = viewModelBestDetail.state.collectAsState().value

    if (getMenu == "작품 상세" && !isExpandedScreen) {

        ScreenBestDetailInfo(item = item)

        if (!isExpandedScreen) {

            val bestItem = viewModelBestDetail.state.collectAsState().value.itemBestInfo

            if(bestItem.total != 0){
                ScreenItemBestDetailManavara(bestItem = bestItem)
            }
        }
    }

    if (getMenu.contains("작품 댓글")) {

        ScreenBestDetailComment(
            viewModelBestDetail = viewModelBestDetail,
            platform = platform,
            bookCode = bookCode
        )

    } else if (getMenu.contains("작가의 다른 작품") || getMenu.contains("비슷한 작품")) {

        val context = LocalContext.current

        if(getMenu.contains("작가의 다른 작품")){

            LaunchedEffect(bookCode){
                viewModelBestDetail.setOtherBooks(
                    platform = platform,
                    bookCode = bookCode,
                    writerLink = state.itemBestDetailInfo.writerLink,
                    context = context
                )
            }

        } else {

            LaunchedEffect(bookCode){
                viewModelBestDetail.setBestDetailRecom(
                    platform = platform,
                    bookCode = bookCode,
                    context = context
                )
            }
        }

        ScreenBestDetailOther(
            viewModelBestDetail = viewModelBestDetail,
            type = type
        )

    } else if (getMenu.contains("분석")) {
        viewModelBestDetail.setBestDetailAnalyze(
            platform = platform,
            bookCode = bookCode,
            type = type
        )

        val item = viewModelBestDetail.state.collectAsState().value.listBestInfo

        if(item.isNotEmpty()){
            ScreenBestDetailAnalyze(
                item = item,
                getMenu = getMenu
            )
        }
    }
}

@Composable
fun ScreenItemBestDetailCard(
    item: ItemBestDetailInfo,
    viewModelBestDetail: ViewModelBestDetail,
    platform: String,
    bookCode: String,
    type: String,
    isExpandedScreen: Boolean,
    getMenu: String
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                AsyncImage(
                    model = item.bookImg,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredWidth(140.dp)
                        .requiredHeight(200.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                modifier = Modifier.padding(16.dp, 0.dp),
                text = item.title,
                color = color20459E,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        if (isExpandedScreen) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(50.dp),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PICK 하기",
                            color = color000000,
                            fontSize = 18.sp,
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = {
                    viewModelBestDetail.gotoUrl(
                        platform = platform,
                        bookCode = bookCode,
                        context = context
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(50.dp),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "작품 보러가기",
                            color = color000000,
                            fontSize = 18.sp,
                        )
                    }
                }
            )

            ScreenBestDetailInfo(item = item)

        } else {
            ScreenBestItemDetailTabItem(
                viewModelBestDetail = viewModelBestDetail,
                item = item,
                isExpandedScreen = isExpandedScreen,
                getMenu = getMenu,
                platform = platform,
                bookCode = bookCode,
                type = type
            )
        }

        Spacer(modifier = Modifier.size(32.dp))
    }
}

@Composable
fun ItemBestDetailInfoLine(title: String, value: String, isLast: Boolean = false) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp)
        ) {
            Text(
                text = title,
                color = color000000,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = value,
                color = color8E8E8E,
                fontSize = 18.sp,
                textAlign = TextAlign.End
            )
        }

        if (!isLast) {
            Spacer(modifier = Modifier.size(4.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorE9E9E9)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun ItemBestDetailInfoAnalyze(
    title: String,
    beforeValue: String,
    value: String,
    isLast: Boolean = false,
    type: String,
) {

    val currentDiff: Float = try {
        if (type == "랭킹 분석") {
            (beforeValue.toFloat() - value.toFloat())
        } else {
            (value.toFloat() - beforeValue.toFloat())
        }
    } catch (e: Exception) {
        0f
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 4.dp)
        ) {
            Text(
                text = title,
                color = color000000,
                fontSize = 18.sp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.replace(".0", ""),
                    color = color8E8E8E,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )

                if (beforeValue != "0") {
                    if (currentDiff > 0) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_up_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (currentDiff < 0) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_down_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (currentDiff != 0f) {
                        Text(
                            text = currentDiff.toString().replace(".0", ""),
                            modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentSize(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Left,
                            color = if (currentDiff > 0f) {
                                color02BC77
                            } else if (currentDiff < 0f) {
                                colorFF2366
                            } else {
                                color1CE3EE
                            },
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        if (!isLast) {
            Spacer(modifier = Modifier.size(4.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorE9E9E9)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun BestDetailBackOnPressed(
    getMenu: String,
    setMenu: (String) -> Unit
) {
    val context = LocalContext.current

    BackHandler(enabled = true) {

        if (getMenu.isNotEmpty()) {
            setMenu("")
        } else {
            (context as Activity).finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBestDetail(
    setter: (String) -> Unit,
    getter: String,
    item: ItemBestDetailInfo,
    drawerState: DrawerState,
    itemBestInfo: ItemBookInfo,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .width(330.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Column {
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = "베스트 작품 상세",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight(weight = 700)
            )

            Spacer(modifier = Modifier.size(16.dp))

            ItemMainSettingSingleTablet(
                containerColor = color4AD7CF,
                image = R.drawable.icon_novel_wht,
                title = "작품 정보",
                body = "작품의 상세 정보 보기",
                current = getter,
                onClick = {
                    coroutineScope.launch {
                        setter("작품 정보")
                        drawerState.close()
                    }
                },
                value = "작품 상세",
            )

            item.tabInfo.forEachIndexed { index, title ->

                if(title.contains("분석")){
                    if(itemBestInfo.total != 0){
                        ItemMainSettingSingleTablet(
                            containerColor = colorList[index + 1],
                            image = getBestDetailLogoMobile(menu = title),
                            title = title,
                            body = getBestDetailDescription(menu = title),
                            current = getter,
                            onClick = {
                                coroutineScope.launch {
                                    setter(title)
                                    drawerState.close()
                                }
                            },
                            value = title,
                        )
                    }
                } else {
                    ItemMainSettingSingleTablet(
                        containerColor = colorList[index + 1],
                        image = getBestDetailLogoMobile(menu = title),
                        title = title,
                        body = getBestDetailDescription(menu = title),
                        current = getter,
                        onClick = {
                            coroutineScope.launch {
                                setter(title)
                                drawerState.close()
                            }
                        },
                        value = title,
                    )
                }
            }
        }

    }
}

@Composable
fun TopbarBestDetail(setDrawer: (Boolean) -> Unit, setter: (String) -> Unit, getter: String) {
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
                    text = getter,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ScreenItemBestDetailManavara(bestItem: ItemBookInfo) {
    ItemTabletTitle(str = "마나바라 수집 정보")

    TabletContentWrap {
        ItemBestDetailInfoLine(title = "베스트 총합 : ", value = bestItem.total.toString())

        ItemBestDetailInfoLine(title = "주간 총점 : ", value = bestItem.totalWeek.toString())

        ItemBestDetailInfoLine(title = "월간 총합 : ", value = bestItem.totalMonth.toString())

        ItemBestDetailInfoLine(title = "총 베스트 횟수 : ", value = bestItem.totalCount.toString())

        ItemBestDetailInfoLine(
            title = "주간 베스트 횟수 : ",
            value = bestItem.totalWeekCount.toString()
        )

        ItemBestDetailInfoLine(
            title = "월간 베스트 횟수 : ",
            value = bestItem.totalMonthCount.toString(),
            isLast = true
        )
    }
}

@Composable
fun ScreenBestDetailComment(
    viewModelBestDetail: ViewModelBestDetail,
    platform: String,
    bookCode: String
) {

    val context = LocalContext.current

    viewModelBestDetail.setComment(
        platform = platform,
        bookCode = bookCode,
        context = context
    )

    val item = viewModelBestDetail.state.collectAsState().value.listComment

    Spacer(modifier = Modifier.size(16.dp))

    if (item.size > 0) {
        item.forEachIndexed { index, itemBestComment ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = getRandomColor()),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = R.drawable.logo_transparents),
                            contentDescription = null,
                            modifier = Modifier
                                .height(28.dp)
                                .width(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

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
                        top = 2.dp,
                        end = 0.dp,
                        bottom = 2.dp,
                    ),
                    content = {

                        Text(
                            text = itemBestComment.comment,
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f)
                                .padding(12.dp, 0.dp, 0.dp, 0.dp),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Left,
                            color = Color.Black,
                        )
                    })
            }
        }
    } else {
        Box(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
        ) {
            ScreenEmpty(str = "댓글이 없습니다")
        }
    }
}

@Composable
fun ScreenBestDetailOther(
    viewModelBestDetail: ViewModelBestDetail,
    type: String
) {
    val context = LocalContext.current
    val item = viewModelBestDetail.state.collectAsState().value.listBestOther

    Spacer(modifier = Modifier.size(16.dp))

    if (item.size > 0) {
        item.forEachIndexed { index, itemBookInfo ->
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(
                    start = 0.dp,
                    top = 0.dp,
                    end = 0.dp,
                    bottom = 0.dp,
                ),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    val intent = Intent(context, ActivityBestDetail::class.java)
                    intent.putExtra("BOOKCODE", itemBookInfo.bookCode)
                    intent.putExtra("PLATFORM", itemBookInfo.type)
                    intent.putExtra("TYPE", type)
                    context.startActivity(intent)
                },
                content = {
                    Column(
                        modifier = Modifier
                            .padding(24.dp, 4.dp)
                    ) {

                        Spacer(modifier = Modifier.size(4.dp))

                        Spacer(modifier = Modifier.size(4.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {

                            ScreenItemBestCard(item = itemBookInfo, index = -1)

                            if (itemBookInfo.intro.isNotEmpty()) {
                                Spacer(modifier = Modifier.size(16.dp))

                                Text(
                                    text = itemBookInfo.intro,
                                    color = color8E8E8E,
                                    fontSize = 16.sp,
                                )
                            } else {
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.size(4.dp))
                    }
                })

            Spacer(modifier = Modifier.size(16.dp))
        }
    } else {
        Box(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
        ) {
            ScreenEmpty(str = "작품이 없습니다")
        }
    }
}

@Composable
fun ScreenBestDetailAnalyze(
    item: ArrayList<ItemBestInfo>,
    getMenu: String
) {

    Spacer(modifier = Modifier.size(16.dp))

    if(getMenu.contains("최근 분석")){

        if(item[0].cntRecom.isNotEmpty()){
            ItemTabletTitle(str = "평점 분석")

            TabletContentWrap {
                item.forEachIndexed { index, itemBestInfo ->

                    val year = itemBestInfo.date.substring(0, 4)
                    val month = itemBestInfo.date.substring(4, 6)
                    val day = itemBestInfo.date.substring(6, 8)

                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = itemBestInfo.cntRecom,
                        beforeValue = if (index == 0) {
                            "0"
                        } else {
                            item[index - 1].cntRecom
                        },
                        type = getMenu,
                        isLast = index == item.size - 1
                    )
                }

            }
        }

        if(item[0].cntFavorite.isNotEmpty()){
            ItemTabletTitle(str = "선호작 분석")

            TabletContentWrap {
                item.forEachIndexed { index, itemBestInfo ->

                    val year = itemBestInfo.date.substring(0, 4)
                    val month = itemBestInfo.date.substring(4, 6)
                    val day = itemBestInfo.date.substring(6, 8)

                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = itemBestInfo.cntFavorite,
                        beforeValue = if (index == 0) {
                            "0"
                        } else {
                            item[index - 1].cntFavorite
                        },
                        type = getMenu,
                        isLast = index == item.size - 1
                    )
                }
            }
        }

        if(item[0].cntPageRead.isNotEmpty()){
            ItemTabletTitle(str = "조회 분석")

            TabletContentWrap {
                item.forEachIndexed { index, itemBestInfo ->

                    val year = itemBestInfo.date.substring(0, 4)
                    val month = itemBestInfo.date.substring(4, 6)
                    val day = itemBestInfo.date.substring(6, 8)

                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = itemBestInfo.cntPageRead,
                        beforeValue = if (index == 0) {
                            "0"
                        } else {
                            item[index - 1].cntPageRead
                        },
                        type = getMenu,
                        isLast = index == item.size - 1
                    )
                }
            }
        }

        if(item[0].cntTotalComment.isNotEmpty()){
            ItemTabletTitle(str = "댓글 분석")

            TabletContentWrap {
                item.forEachIndexed { index, itemBestInfo ->

                    val year = itemBestInfo.date.substring(0, 4)
                    val month = itemBestInfo.date.substring(4, 6)
                    val day = itemBestInfo.date.substring(6, 8)


                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = itemBestInfo.cntTotalComment,
                        beforeValue = if (index == 0) {
                            "0"
                        } else {
                            item[index - 1].cntTotalComment
                        },
                        type = getMenu,
                        isLast = index == item.size - 1
                    )

                }
            }

        }

        if(item[0].number.toString().isNotEmpty()){
            ItemTabletTitle(str = "랭킹 분석")

            TabletContentWrap {
                item.forEachIndexed { index, itemBestInfo ->

                    val year = itemBestInfo.date.substring(0, 4)
                    val month = itemBestInfo.date.substring(4, 6)
                    val day = itemBestInfo.date.substring(6, 8)

                    ItemBestDetailInfoAnalyze(
                        title = "${year}년 ${month}월 ${day}일",
                        value = (itemBestInfo.number + 1).toString(),
                        beforeValue = if (index == 0) {
                            "0"
                        } else {
                            (item[index - 1].number + 1).toString()
                        },
                        type = getMenu,
                        isLast = index == item.size - 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(32.dp))

    } else {

        TabletContentWrap {
            item.forEachIndexed { index, itemBestInfo ->

                val year = itemBestInfo.date.substring(0, 4)
                val month = itemBestInfo.date.substring(4, 6)
                val day = itemBestInfo.date.substring(6, 8)

                ItemBestDetailInfoAnalyze(
                    title = "${year}년 ${month}월 ${day}일",
                    value = if (getMenu.contains("평점 분석")) {
                        itemBestInfo.cntRecom
                    } else if (getMenu.contains("선호작 분석")) {
                        itemBestInfo.cntFavorite
                    } else if (getMenu.contains("조회 분석")) {
                        itemBestInfo.cntPageRead
                    } else if (getMenu.contains("댓글 분석")) {
                        itemBestInfo.cntTotalComment
                    } else if (getMenu.contains("랭킹 분석")) {
                        (itemBestInfo.number + 1).toString()
                    } else {
                        (itemBestInfo.number + 1).toString()
                    },
                    beforeValue = if (index == 0) {
                        "0"
                    } else {
                        if (getMenu.contains("평점 분석")) {
                            item[index - 1].cntRecom
                        } else if (getMenu.contains("선호작 분석")) {
                            item[index - 1].cntFavorite
                        } else if (getMenu.contains("조회 분석")) {
                            item[index - 1].cntPageRead
                        } else if (getMenu.contains("댓글 분석")) {
                            item[index - 1].cntTotalComment
                        } else {
                            (item[index - 1].number + 1).toString()
                        }
                    },
                    type = getMenu,
                    isLast = index == item.size - 1
                )
            }
        }

        Spacer(modifier = Modifier.width(32.dp))
    }


}

@Composable
fun ScreenBestDetailInfo(item : ItemBestDetailInfo){
    ItemTabletTitle(str = "작품 정보")

    TabletContentWrap {
        ItemBestDetailInfoLine(title = "작가 : ", value = item.writer)

        if (item.genre.isNotEmpty()) {
            ItemBestDetailInfoLine(title = "장르 : ", value = item.genre)
        }

        if (item.cntRecom.isNotEmpty()) {
            ItemBestDetailInfoLine(title = "작품 추천 수 : ", value = item.cntRecom)
        }

        if (item.cntChapter.isNotEmpty()) {
            ItemBestDetailInfoLine(title = "총 편수 : ", value = item.cntChapter)
        }

        if (item.cntFavorite.isNotEmpty()) {
            ItemBestDetailInfoLine(title = "선호작 수 : ", value = item.cntFavorite)
        }

        if (item.cntPageRead.isNotEmpty()) {
            ItemBestDetailInfoLine(title = "조회 수 : ", value = item.cntPageRead)
        }

        if (item.cntTotalComment.isNotEmpty()) {
            ItemBestDetailInfoLine(title = "댓글 수 : ", value = item.cntTotalComment)
        }

        if (item.keyword.isNotEmpty()) {
            ItemBestDetailInfoLine(
                title = "키워드 : ",
                value = item.keyword.joinToString(", "),
            )
        }

        if (item.platform.isNotEmpty()) {
            ItemBestDetailInfoLine(
                title = "플랫폼 : ",
                value = changePlatformNameKor(item.platform),
                isLast = true
            )
        }
    }

    if (item.intro.isNotEmpty()) {
        ItemTabletTitle(str = "줄거리")

        TabletContentWrap {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = item.intro,
                overflow = TextOverflow.Ellipsis,
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun ScreenBestDetailAnalyzeBtn(title : String, onClick : () -> Unit, isLast: Boolean){
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = getBestDetailLogo(menu = title)),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    if (isLast) {
        Spacer(modifier = Modifier.size(16.dp))
    }
}