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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.bigbigdw.manavara.util.geMonthDate
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.ItemKeyword
import com.bigbigdw.manavara.util.screen.spannableString
import com.bigbigdw.manavara.util.weekList
import com.bigbigdw.manavara.util.weekListAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScreenTodayBest(
    viewModelMain: ViewModelMain,
    viewModelBest: ViewModelBest,
    getDetailPlatform: String,
    getDetailType: String,
    isExpandedScreen: Boolean,
    listState: LazyListState
) {

    val bestState = viewModelBest.state.collectAsState().value


    LaunchedEffect(getDetailPlatform){
        viewModelBest.getBestListToday(
            platform = getDetailPlatform,
            type = getDetailType,
        )
    }

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
                    index = index
                )
            }
        }
    }
}

@Composable
fun ListBestToday(
    itemBookInfo: ItemBookInfo,
    index: Int,
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
            modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(weekListAll()) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ItemKeyword(
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
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
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

            if(bestState.weekList[getWeekDate(getDate)].size > 0){
                LazyColumn(
                    modifier = Modifier
                        .background(colorF6F6F6)
                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                ) {

                    itemsIndexed(bestState.weekList[getWeekDate(getDate)]) { index, item ->
                        ListBestToday(
                            itemBookInfo = item,
                            index = index
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

                    if(type == "WEEK"){
                        ItemBestExpandWeek(
                            item = itemBookInfo,
                        )
                    } else{
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

    for(item in bestState.monthList){
        count += 1
        arrayList.add("${count}주차")
    }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(arrayList) { index, item ->

                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ItemKeyword(
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
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
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

            if(bestState.monthList[geMonthDate(getDate)].size > 0){
                LazyColumn(
                    modifier = Modifier
                        .background(colorF6F6F6)
                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                ) {

                    itemsIndexed(bestState.monthList[geMonthDate(getDate)]) { index, item ->

                        val itemBookInfo = bestState.itemBookInfoMap[item.bookCode]

                        if(itemBookInfo != null){
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
fun ItemBestExpandWeek(item: ItemBookInfo) {
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
                    text =  "${item.writer}/${item.cntChapter}",
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
fun ScreenEmpty(str : String = "마나바라") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {

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

            Spacer(
                modifier = Modifier.size(8.dp)
            )
            Text(
                text = str,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = color000000
            )
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
                    text =  "${item.writer}/${item.cntChapter}",
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