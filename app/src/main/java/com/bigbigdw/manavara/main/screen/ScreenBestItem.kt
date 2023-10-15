package com.bigbigdw.manavara.main.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.geMonthDate
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import com.bigbigdw.manavara.util.weekList
import com.bigbigdw.manavara.util.weekListAll
import getBookCount
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScreenTodayBest(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    getDetailType: String,
    isExpandedScreen: Boolean,
    listState: LazyListState,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val bestState = viewModelBest.state.collectAsState().value

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyColumn(
            state = listState,
            modifier = if (!isExpandedScreen) {
                Modifier
                    .background(colorF6F6F6)
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxSize()
            } else {
                Modifier
                    .background(colorF6F6F6)
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
                    .fillMaxSize()
            },
        ) {

            itemsIndexed(bestState.itemBookInfoList) { index, item ->
                ListBestToday(
                    itemBookInfo = item,
                    index = index,
                    modalSheetState = modalSheetState,
                    viewModelBest = viewModelBest,
                    setDialogOpen = setDialogOpen
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListBestToday(
    itemBookInfo: ItemBookInfo,
    index: Int,
    modalSheetState: ModalBottomSheetState?,
    viewModelBest: ViewModelBest,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val coroutineScope = rememberCoroutineScope()

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
                    viewModelBest.getBookItemInfo(itemBookInfo = itemBookInfo)
                    modalSheetState?.show()

                    if (setDialogOpen != null) {
                        setDialogOpen(true)
                    }
                }
            },
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 0.dp,
            ),
            content = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

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

                    if (itemBookInfo.totalCount > 1) {
                        if (itemBookInfo.currentDiff > 0) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_drop_up_24px),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(20.dp)
                            )
                        } else if (itemBookInfo.currentDiff < 0) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_drop_down_24px),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = if (itemBookInfo.totalCount > 1) {
                            if (itemBookInfo.currentDiff != 0) {
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

                                if (itemBookInfo.currentDiff > 0) {
                                    color02BC77
                                } else if (itemBookInfo.currentDiff < 0) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTodayWeek(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
) {

    val bestState = viewModelBest.state.collectAsState().value

    val (getDate, setDate) = remember { mutableStateOf("전체") }

    val listState = rememberLazyListState()

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(weekListAll()) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = getDate,
                        setter = setDate,
                        title = item,
                        getValue = item,
                        viewModelBest = viewModelBest,
                        listState = listState
                    )
                }
            }
        }

        if (getDate == "전체") {
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {

                itemsIndexed(bestState.weekTrophyList) { index, item ->
                    ListBest(
                        itemBookInfoMap = bestState.itemBookInfoMap,
                        bookCode = item.bookCode,
                        type = "WEEK"
                    )
                }
            }
        } else {

            if (bestState.weekList[getWeekDate(getDate)].size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .background(colorF6F6F6)
                ) {

                    itemsIndexed(bestState.weekList[getWeekDate(getDate)]) { index, item ->
                        ListBestToday(
                            itemBookInfo = item,
                            index = index,
                            viewModelBest = viewModelBest,
                            modalSheetState = null,
                            setDialogOpen = null
                        )
                    }
                }
            } else {
                ScreenEmpty(str = "데이터가 없습니다")
            }
        }
    }
}

@Composable
fun ListBest(
    itemBookInfoMap: MutableMap<String, ItemBookInfo>,
    bookCode: String,
    type: String,
) {

    val itemBookInfo = itemBookInfoMap[bookCode]

    if (itemBookInfo != null) {

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

            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(24.dp, 4.dp)
                ) {

                    Spacer(modifier = Modifier.size(4.dp))

                    if (type == "WEEK") {
                        ScreenItemWeek(
                            item = itemBookInfo,
                        )
                    } else {
                        ItemBestExpandMonth(
                            item = itemBookInfo,
                        )
                    }

                    Spacer(modifier = Modifier.size(4.dp))
                }
            })

        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun ScreenTodayMonth(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
) {

    val bestState = viewModelBest.state.collectAsState().value

    val (getDate, setDate) = remember { mutableStateOf("전체") }

    val listState = rememberLazyListState()

    val monthList = bestState.monthList

    val arrayList = ArrayList<String>()
    arrayList.add("전체")

    var count = 0

    for (item in bestState.monthList) {
        count += 1
        arrayList.add("${count}주차")
    }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(arrayList) { index, item ->

                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = getDate,
                        setter = setDate,
                        title = item,
                        getValue = item,
                        viewModelBest = viewModelBest,
                        listState = listState
                    )
                }
            }
        }

        if (getDate == "전체") {
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {

                itemsIndexed(bestState.monthTrophyList) { index, item ->
                    ListBest(
                        itemBookInfoMap = bestState.itemBookInfoMap,
                        bookCode = item.bookCode,
                        type = "MONTH"
                    )
                }
            }
        } else {

            if (bestState.monthList[geMonthDate(getDate)].size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .background(colorF6F6F6)
                        .padding(16.dp, 0.dp, 16.dp, 0.dp)
                ) {

                    itemsIndexed(bestState.monthList[geMonthDate(getDate)]) { index, item ->

                        val itemBookInfo = bestState.itemBookInfoMap[item.bookCode]

                        if (itemBookInfo != null) {
                            Text(
                                modifier = Modifier.padding(16.dp, 8.dp),
                                text = weekList()[index],
                                fontSize = 16.sp,
                                color = color8E8E8E,
                                fontWeight = FontWeight(weight = 700)
                            )
                        }

                        ListBest(
                            itemBookInfoMap = bestState.itemBookInfoMap,
                            bookCode = item.bookCode,
                            type = "WEEK"
                        )
                    }
                }
            } else {
                ScreenEmpty(str = "데이터가 없습니다")
            }
        }
    }
}

@Composable
fun ScreenItemWeek(item: ItemBookInfo) {
    Spacer(modifier = Modifier.size(4.dp))

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {

            Box(
                modifier = Modifier
                    .height(160.dp)
                    .width(120.dp)
            ) {
                AsyncImage(
                    model = item.bookImg,
                    contentDescription = null,
                    modifier = Modifier
                        .height(160.dp)
                        .width(120.dp)
                )

            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = item.title,
                    color = color20459E,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(weight = 500),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${item.writer}/${item.cntChapter}",
                    color = color000000,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = spannableString(
                        textFront = "헌재 스코어 : ",
                        color = color000000,
                        textEnd = "${item.number}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "플랫폼 평점 : ",
                        color = color000000,
                        textEnd = item.cntRecom
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "베스트 총합 : ",
                        color = color000000,
                        textEnd = "${item.total}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "주간 총점 : ",
                        color = color000000,
                        textEnd = "${item.totalWeek}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "주간 베스트 횟수 : ",
                        color = color000000,
                        textEnd = item.totalWeekCount.toString()
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = item.intro,
            color = color8E8E8E,
            fontSize = 16.sp,
        )
    }

    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun ScreenBestDBListNovel(isInit: Boolean = true, type: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
        contentAlignment = if(isInit){
            Alignment.Center
        } else {
            Alignment.TopStart
        }
    ) {
        val context = LocalContext.current
        val dataStore = DataStoreManager(context)

        LazyColumn(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(isInit){
                item {
                    Card(
                        modifier = Modifier
                            .wrapContentSize(),
                        colors = CardDefaults.cardColors(containerColor = colorDCDCDD),
                        shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(90.dp)
                                .width(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(72.dp)
                                    .width(72.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.size(8.dp)) }

                item {
                    Text(
                        text = "마나바라에 기록된 작품들",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = color000000
                    )
                }
            }

            item { Spacer(modifier = Modifier.size(8.dp)) }

            if(type == "NOVEL"){
                itemsIndexed(novelListEng()) { index, item ->
                    Box(modifier = Modifier.padding(16.dp, 8.dp)) {
                        TabletContentWrapBtn(
                            onClick = {},
                            content = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Image(
                                        painter = painterResource(id = getPlatformLogoEng(item)),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )

                                    Spacer(modifier = Modifier.size(8.dp))

                                    getBookCount(context = context, type = type, platform = item)

                                    Text(
                                        text = spannableString(
                                            textFront = "${changePlatformNameKor(item)} : ",
                                            color = color000000,
                                            textEnd = "${dataStore.getDataStoreString(
                                                getPlatformDataKeyNovel(item)
                                            ).collectAsState(initial = "").value ?: "0"} 작품"
                                        ),
                                        color = color20459E,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        )
                    }
                }
            } else {
                itemsIndexed(comicListEng()) { index, item ->
                    Box(modifier = Modifier.padding(16.dp, 8.dp)) {
                        TabletContentWrapBtn(
                            onClick = {},
                            content = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Image(
                                        painter = painterResource(id = getPlatformLogoEng(item)),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )

                                    Spacer(modifier = Modifier.size(8.dp))

                                    getBookCount(context = context, type = type, platform = item)

                                    Text(
                                        text = spannableString(
                                            textFront = "${changePlatformNameKor(item)} : ",
                                            color = color000000,
                                            textEnd = "${dataStore.getDataStoreString(
                                                getPlatformDataKeyComic(item)
                                            ).collectAsState(initial = "").value ?: "0"} 작품"
                                        ),
                                        color = color20459E,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemBestExpandMonth(item: ItemBookInfo) {
    Spacer(modifier = Modifier.size(4.dp))

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {

            Box(
                modifier = Modifier
                    .height(200.dp)
                    .width(140.dp)
            ) {
                AsyncImage(
                    model = item.bookImg,
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .width(140.dp)
                )

            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = item.title,
                    color = color20459E,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(weight = 500),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${item.writer}/${item.cntChapter}",
                    color = color000000,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = spannableString(
                        textFront = "헌재 스코어 : ",
                        color = color000000,
                        textEnd = "${item.number}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "플랫폼 평점 : ",
                        color = color000000,
                        textEnd = item.cntRecom
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "베스트 총합 : ",
                        color = color000000,
                        textEnd = "${item.total}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "주간 총점 : ",
                        color = color000000,
                        textEnd = "${item.totalWeek}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "주간 베스트 횟수 : ",
                        color = color000000,
                        textEnd = item.totalWeekCount.toString()
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "월간 총점 : ",
                        color = color000000,
                        textEnd = "${item.totalMonth}"
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )

                Text(
                    text = spannableString(
                        textFront = "월간 베스트 횟수 : ",
                        color = color000000,
                        textEnd = item.totalMonthCount.toString()
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = item.intro,
            color = color8E8E8E,
            fontSize = 16.sp,
        )
    }

    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun ScreenDialogBest(item: ItemBookInfo) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {

            Card(
                modifier = Modifier
                    .requiredHeight(200.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                AsyncImage(
                    model = item.bookImg,
                    contentDescription = null,
                    modifier = Modifier
                        .requiredHeight(200.dp)
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = item.title,
                    color = color20459E,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(weight = 500),
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

//        if(item.intro.isNotEmpty()){
//
//            Spacer(modifier = Modifier.size(16.dp))
//
//            Text(
//                modifier = Modifier.padding(4.dp),
//                text = item.intro,
//                color = color8E8E8E,
//                fontSize = 16.sp,
//                maxLines = 5,
//                overflow = TextOverflow.Ellipsis
//            )
//        }

        Spacer(modifier = Modifier.size(8.dp))


    }

    Spacer(modifier = Modifier.size(4.dp))

    Spacer(modifier = Modifier.size(80.dp))
}

@Composable
fun ScreenItemBestCount(item: ItemBookInfo) {
    Row {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column {

                Row {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_best),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = spannableString(
                            textFront = "베스트 총합 : ",
                            color = color000000,
                            textEnd = "${item.total}"
                        ),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_trophy),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = spannableString(
                            textFront = "주간 총점 : ",
                            color = color000000,
                            textEnd = "${item.totalWeek}"
                        ),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_trophy),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = spannableString(
                            textFront = "월간 총점 : ",
                            color = color000000,
                            textEnd = "${item.totalMonth}"
                        ),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }
            }
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column {

                Row {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_best_gr),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = spannableString(
                            textFront = "총 베스트 횟수 : ",
                            color = color000000,
                            textEnd = "${item.totalCount}"
                        ),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_trophy_gr),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = spannableString(
                            textFront = "주간 베스트 횟수 : ",
                            color = color000000,
                            textEnd = item.totalWeekCount.toString()
                        ),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )

                }

                Row {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_trophy_gr),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = spannableString(
                            textFront = "월간 베스트 횟수 : ",
                            color = color000000,
                            textEnd = item.totalMonthCount.toString()
                        ),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )

                }
            }
        }
    }
}