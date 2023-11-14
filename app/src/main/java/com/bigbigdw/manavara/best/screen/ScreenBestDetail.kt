package com.bigbigdw.manavara.best.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bigbigdw.manavara.best.viewModels.ViewModelBestDetail
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
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

    val listState = rememberLazyListState()
    val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(bookCode) {
        viewModelBestDetail.setBestDetailInfo(
            platform = platform,
            bookCode = bookCode,
            context = context
        )
    }

    if (!isExpandedScreen) {

//        val (getMenu, setMenu) = remember { mutableStateOf("TODAY") }
//        val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf(novelListEng()[0]) }
//        val (getType, setType) = remember { mutableStateOf("") }
//        val (getBestType, setBestType) = remember { mutableStateOf("") }
//
//        ScreenMainMobile(
//            navController = navController,
//            currentRoute = currentRoute,
//            viewModelMain = viewModelMain,
//            viewModelBest = viewModelBest,
//            isExpandedScreen = isExpandedScreen,
//            drawerState = drawerState,
//            setMenu = setMenu,
//            getMenu = getMenu,
//            setPlatform = setDetailPlatform,
//            getPlatform = getDetailPlatform,
//            setType = setType,
//            getType = getType,
//            listState = listState,
//            setBestType = setBestType,
//            getBestType = getBestType
//        )

    } else {

        val (getMenu, setMenu) = remember { mutableStateOf("") }

        ScreenTabletBestDetail(
            viewModelBestDetail = viewModelBestDetail,
            isExpandedScreen = isExpandedScreen,
            platform = platform,
            bookCode = bookCode,
            type = type,
            listState = listState,
            setDialogOpen = setDialogOpen,
            getMenu = getMenu,
            setMenu = setMenu
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
    listState: LazyListState,
    setDialogOpen: (Boolean) -> Unit,
    type: String,
    getMenu: String,
    setMenu: (String) -> Unit
) {

    val item = viewModelBestDetail.state.collectAsState().value.itemBestDetailInfo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                ScreenItemBestDetailMenu(
                    viewModelBestDetail = viewModelBestDetail,
                    item = item,
                    platform = platform,
                    bookCode = bookCode,
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
    bookCode: String
) {

    Column(
        modifier = Modifier
            .width(400.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(color = colorF6F6F6)
            .padding(16.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "베스트 작품 상세",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(16.dp))

        ScreenItemBestDetailCard(
            item = item,
            viewModelBestDetail = viewModelBestDetail,
            platform = platform,
            bookCode = bookCode
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
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                onClick = {
                    setMenu(title)
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
                            text = title,
                            color = color000000,
                            fontSize = 18.sp,
                        )
                    }
                }
            )

            if (index != item.tabInfo.size - 1) {
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

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
           item{  Spacer(modifier = Modifier.size(16.dp)) }

            item{
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

        item{
            ScreenBestItemDetailTabItem(
                viewModelBestDetail = viewModelBestDetail,
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
    getMenu: String,
    platform: String,
    bookCode: String,
    type: String
) {

    val context = LocalContext.current

    if (getMenu.contains("작품 댓글")) {

        viewModelBestDetail.setComment(
            platform = platform,
            bookCode = bookCode,
            context = context
        )

        val item = viewModelBestDetail.state.collectAsState().value.listComment

        Spacer(modifier = Modifier.size(16.dp))

        if(item.size > 1){
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
                                    .weight(1f).padding(12.dp, 0.dp, 0.dp, 0.dp),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Left,
                                color = Color.Black,
                            )
                        })
                }
            }
        } else {
            Box(modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()) {
                ScreenEmpty(str = "댓글이 없습니다")
            }
        }

    } else if (getMenu.contains("작가의 다른 작품")) {

        viewModelBestDetail.setOtherBooks(
            platform = platform,
            bookCode = bookCode,
            context = context
        )

        val item = viewModelBestDetail.state.collectAsState().value.listBestOther

        Spacer(modifier = Modifier.size(16.dp))

        if(item.size > 1){
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

                                ScreenItemBestCard(item = itemBookInfo)

                                if(itemBookInfo.intro.isNotEmpty()){
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
            Box(modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()) {
                ScreenEmpty(str = "작품이 없습니다")
            }
        }
    }
}

@Composable
fun ScreenItemBestDetailCard(
    item: ItemBestDetailInfo,
    viewModelBestDetail: ViewModelBestDetail,
    platform: String,
    bookCode: String
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
                modifier = Modifier
                    .requiredHeight(200.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                AsyncImage(
                    model = item.bookImg,
                    contentDescription = null,
                    modifier = Modifier
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

        ItemTabletTitle(str = "작품 정보")

        TabletContentWrap {
            ItemBestDetailInfoLine(title = "작가 : ", value = item.writer)

            if (item.genre.isNotEmpty()) {
                ItemBestDetailInfoLine(title = "장르 : ", value = item.genre)
            }

            if (item.cntRecom.isNotEmpty()) {
                ItemBestDetailInfoLine(title = "플랫폼 평점 : ", value = item.cntRecom)
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