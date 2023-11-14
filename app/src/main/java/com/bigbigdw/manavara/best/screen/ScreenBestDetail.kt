package com.bigbigdw.manavara.best.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.viewModels.ViewModelBestDetail
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.screen.spannableString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDetail(
    viewModelBestDetail: ViewModelBestDetail,
    widthSizeClass: WindowWidthSizeClass,
    platform: String,
    bookCode: String
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

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
        ScreenTabletBestDetail(
            viewModelBestDetail = viewModelBestDetail,
            isExpandedScreen = isExpandedScreen,
            platform = platform,
            bookCode = bookCode
        )
    }
}

@Composable
fun ScreenTabletBestDetail(
    viewModelBestDetail: ViewModelBestDetail,
    isExpandedScreen: Boolean,
    platform: String,
    bookCode: String
) {

    val context = LocalContext.current

    viewModelBestDetail.setBestDetailInfo(
        platform = platform,
        bookCode = bookCode,
        context = context
    )

    val item = viewModelBestDetail.state.collectAsState().value.itemBestDetailInfo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }

                val (getDetailPage, setDetailPage) = remember { mutableStateOf(false) }
                val (getDetailMenu, setDetailMenu) = remember { mutableStateOf("") }
                val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf("") }
                val (getDetailType, setDetailType) = remember { mutableStateOf("") }

                ScreenItemBestDetail(
                    isExpandedScreen = isExpandedScreen,
                    item = item,
                    setMenu = setMenu,
                    getMenu = getMenu,
                    onClick = { setDetailPage(false) })

                Spacer(modifier = Modifier.size(16.dp))

//                if (getDetailPage) {
//                    ScreenTabletDetail(
//                        setDetailPage = setDetailPage,
//                        getDetailMenu = getDetailMenu,
//                        viewModelMain = viewModelMain,
//                        getDetailPlatform = getDetailPlatform,
//                        getDetailType = getDetailType,
//                    )
//                } else {
//                    ScreenTablet(
//                        title = getMenu,
//                        viewModelMain = viewModelMain,
//                        setDetailPage = setDetailPage,
//                        setDetailMenu = setDetailMenu,
//                        setDetailPlatform = setDetailPlatform,
//                        setDetailType = setDetailType
//                    )
//                }

            } else {
//                ScreenSettingMobile()
            }
        }
    }
}

@Composable
fun ScreenItemBestDetail(
    setMenu: (String) -> Unit,
    getMenu: String,
    onClick: () -> Unit,
    isExpandedScreen: Boolean,
    item: ItemBestDetailInfo
) {

    Column(
        modifier = Modifier
            .width(400.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
//            .background(color = colorF6F6F6)
            .background(color = Color.Red)
            .padding(8.dp, 0.dp)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        ScreenItemBestDetailCard(item = item)

        Spacer(modifier = Modifier.size(8.dp))

        if (isExpandedScreen) {

            Spacer(modifier = Modifier.size(8.dp))

            if (item.intro.isNotEmpty()) {
                Text(
                    text = item.intro,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }
        }

    }
}

@Composable
fun ScreenItemBestDetailCard(item: ItemBestDetailInfo){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
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

        Column(modifier = Modifier.wrapContentHeight()) {
            Text(
                text = item.title,
                color = color20459E,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = item.writer,
                color = color000000,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.size(4.dp))

            if (item.genre.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "장르 : ",
                        color = color000000,
                        textEnd = item.genre
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntChapter.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "챕터 수 : ",
                        color = color000000,
                        textEnd = item.cntChapter
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntRecom.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "플랫폼 평점 : ",
                        color = color000000,
                        textEnd = item.cntRecom
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntChapter.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "총 편수 : ",
                        color = color000000,
                        textEnd = item.cntChapter
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntFavorite.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "선호작 수 : ",
                        color = color000000,
                        textEnd = item.cntFavorite
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntPageRead.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "조회 수 : ",
                        color = color000000,
                        textEnd = item.cntPageRead
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntTotalComment.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "댓글 수 : ",
                        color = color000000,
                        textEnd = item.cntTotalComment
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }
        }
    }
}