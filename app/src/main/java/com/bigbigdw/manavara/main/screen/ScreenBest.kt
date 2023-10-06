package com.bigbigdw.manavara.main.screen

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.bigbigdw.manavara.main.models.ItemBookInfo
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color02BC77
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color52A9FF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.ui.theme.colorea927C
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.ItemKeyword
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import getPlatformGenre
import kotlinx.coroutines.launch

@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain
) {

    val mainState = viewModelMain.state.collectAsState().value

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if (isExpandedScreen) {

                val (getMenu, setMenu) = remember { mutableStateOf("") }
                val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf(mainState.platformRangeNovel[0]) }
                val (getDetailGenre, setDetailGenre) = remember { mutableStateOf("ALL") }
                val (getDetailType, setDetailType) = remember { mutableStateOf("COMIC") }

                Log.d("ScreenTodayBest", "DBDate.dateMMDD() == ${DBDate.dateMMDD()}")

                viewModelBest.getBestJsonList(
                    date = DBDate.dateMMDD(),
                    platform = getDetailPlatform,
                    genre = getDetailGenre,
                    type = getDetailType,
                )

                ScreenBestTabletList(
                    setMenu = setMenu,
                    getMenu = getMenu,
                    viewModelMain = viewModelMain,
                    setDetailPlatform = setDetailPlatform,
                    setDetailType = setDetailType
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenBestDetail(
                    getMenu = getMenu,
                    viewModelMain = viewModelMain,
                    viewModelBest = viewModelBest,
                    getDetailPlatform = getDetailPlatform,
                    getDetailGenre = getDetailGenre,
                    getDetailType = getDetailType,
                    setDetailGenre = setDetailGenre
                )

            } else {
                ScreenTest()
            }
        }
    }
}

@Composable
fun ScreenBestTabletList(
    setMenu: (String) -> Unit,
    getMenu: String,
    viewModelMain: ViewModelMain,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit
) {

    val mainState = viewModelMain.state.collectAsState().value

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
            text = "베스트",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_setting_wht,
            title = "유저 옵션",
            body = "뷰모드 전환 및 플랫폼 선택",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_setting_wht,
            title = "작품 검색",
            body = "플랫폼과 무관하게 작품 검색 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_best_wht,
            title = "투데이 베스트",
            body = "투데이 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        mainState.platformRangeNovel.forEachIndexed{ index, item ->
            ItemBestListSingle(
                containerColor = color52A9FF,
                image = R.drawable.icon_best_wht,
                title = changePlatformNameKor(item),
                body = "투데이 베스트 관련 옵션 진행",
                setMenu = setMenu,
                getMenu = getMenu,
                bestType = "TODAY_BEST",
                setDetailPlatform = { setDetailPlatform(item) },
                setDetailType = { setDetailType("COMIC") },

            )
        }

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_best_wht,
            title = "주간 베스트",
            body = "주간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorea927C,
            image = R.drawable.icon_best_wht,
            title = "월간 베스트",
            body = "월간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )
    }
}

@Composable
fun ItemBestListSingle(
    containerColor: Color,
    image: Int,
    title: String,
    body: String,
    setMenu: (String) -> Unit,
    getMenu: String,
    bestType: String,
    setDetailPlatform: () -> Unit,
    setDetailType: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (getMenu == "$bestType $title") {
                colorE9E9E9
            } else {
                colorF7F7F7
            }
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            setMenu("$bestType $title")
            setDetailPlatform()
            setDetailType()
        },
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 6.dp,
            end = 12.dp,
            bottom = 6.dp,
        ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    backgroundColor = containerColor,
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .height(28.dp)
                                .width(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Row {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = color000000,

                            fontWeight = FontWeight(weight = 500)
                        )
                    }

                    Row {
                        Text(
                            text = body,
                            fontSize = 14.sp,
                            color = color8E8E8E,
                        )
                    }
                }
            }
        })
}

@Composable
fun ScreenBestDetail(
    getMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailGenre: String,
    getDetailType: String,
    viewModelBest: ViewModelBest,
    setDetailGenre: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .background(color = colorF6F6F6)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp),
            text = "< $getMenu",
            fontSize = 24.sp,
            color = color000000,
            fontWeight = FontWeight(weight = 700)
        )

        if (getMenu.contains("TODAY_BEST")) {
            ScreenTodayBest(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                getDetailPlatform = getDetailPlatform,
                getDetailType = getDetailType,
                setDetailGenre = setDetailGenre,
                getDetailGenre = getDetailGenre
            )

        } else {
            ScreenTest()
        }
    }
}

@Composable
fun ScreenTodayBest(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    getDetailPlatform: String,
    getDetailType: String,
    setDetailGenre: (String) -> Unit,
    getDetailGenre: String,
) {

    val bestState = viewModelBest.state.collectAsState().value

    val listState = rememberLazyListState()

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(16.dp, 20.dp, 0.dp, 20.dp),
        ) {
            itemsIndexed(
                getPlatformGenre(
                    type = getDetailType,
                    platform = getDetailPlatform
                )
            ) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ItemKeyword(
                        getter = getDetailGenre,
                        setter = setDetailGenre,
                        title = item,
                        viewModelBest = viewModelBest,
                        listState = listState
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .background(colorF6F6F6)
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
        ) {

            itemsIndexed(bestState.itemBookInfoList) { index, item ->
                ListBestToday(
                    viewModelMain = viewModelMain,
                    viewModelBest = viewModelBest,
                    itemBookInfo = item,
                    index = index
                )
            }
        }
    }
}

@Composable
fun ListBestToday(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    itemBookInfo: ItemBookInfo,
    index: Int,
) {

    val coroutineScope = rememberCoroutineScope()
    val userInfo = viewModelMain.state.collectAsState().value

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        AsyncImage(
            model = itemBookInfo.bookImg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))

        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = {
                coroutineScope.launch {
//                        viewModelBestList.getBottomBestData(bestItemData, index)
//                        viewModelBestList.bottomDialogBestGetRank(userInfo, bestItemData)
//                        modalSheetState.show()
                }
            },
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 0.dp,
            ),
            content = {

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

                    Text(
                        text = "${index + 1} ",
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(16.dp, 0.dp, 0.dp, 0.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Left,
                        color = color20459E,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        maxLines = 1,
                        text = itemBookInfo.title,
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    if(itemBookInfo.currentDiff > 0){
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_up_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if(itemBookInfo.currentDiff < 0) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_down_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = if (itemBookInfo.totalCount > 1) {
                            if(itemBookInfo.currentDiff != 0){
                                itemBookInfo.currentDiff.toString()
                            } else {
                                "-"
                            }
                        } else {
                            "NEW"
                        },
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Left,
                        color = if (itemBookInfo.totalCount > 1) {
                            if (itemBookInfo.currentDiff != 0) {

                                if(itemBookInfo.currentDiff > 0){
                                    color02BC77
                                } else if(itemBookInfo.currentDiff < 0){
                                    colorFF2366
                                } else {
                                    color1CE3EE
                                }
                            } else {
                                color1CE3EE
                            }
                        } else {
                            color1CE3EE
                        },
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            })
    }
}