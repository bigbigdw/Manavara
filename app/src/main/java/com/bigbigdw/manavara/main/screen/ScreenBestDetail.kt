package com.bigbigdw.manavara.main.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.screen.ItemKeyword
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import com.bigbigdw.manavara.util.screen.spannableString
import com.bigbigdw.manavara.util.weekList
import getNaverSeriesGenreEngToKor
import getPlatformGenre
import getWeekDate
import kotlinx.coroutines.launch

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
                        title = getNaverSeriesGenreEngToKor(item),
                        getValue = item,
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
            modifier = Modifier.padding(16.dp, 20.dp, 0.dp, 20.dp),
        ) {
            itemsIndexed(weekList()) { index, item ->
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

        if(getDate == "전체"){
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(0.dp, 0.dp, 16.dp, 0.dp)
            ) {

                itemsIndexed(bestState.weekTrophyList) { index, item ->
                    ListBestMonth(
                        itemBookInfoMap = bestState.itemBookInfoMap,
                        bookCode = item.bookCode,
                        index = index
                    )
                }
            }
        } else {
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
        }
    }
}

@Composable
fun ListBestMonth(
    itemBookInfoMap: MutableMap<String, ItemBookInfo>,
    bookCode: String,
    index: Int,
) {

    val coroutineScope = rememberCoroutineScope()
    val itemBookInfo = itemBookInfoMap[bookCode]

    if(itemBookInfo != null){
        TabletContentWrap {
            Spacer(modifier = Modifier.size(8.dp))

            ItemBestExpand(
                item = itemBookInfo,
            )

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun ItemBestExpand(item: ItemBookInfo){

    Column {

        Spacer(modifier = Modifier.size(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically){
            AsyncImage(
                model = item.bookImg,
                contentDescription = null,
                modifier = Modifier
                    .height(220.dp)
                    .widthIn(0.dp, 220.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        color = color000000,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(weight = 500)
                    )
                }

                Spacer(modifier = Modifier.size(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작가명 : ", color = color000000, textEnd = item.writer),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "북코드 : ", color = color000000, textEnd = item.bookCode),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "헌재 스코어 : ", color = color000000, textEnd = item.current.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "타입 : ", color = color000000, textEnd = item.type),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작품 정보1 : ", color = color000000, textEnd = item.info1),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작품 정보2 : ", color = color000000, textEnd = item.info2),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "작품 정보3 : ", color = color000000, textEnd = item.info3),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 총합 : ", color = color000000, textEnd = item.total.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 총합 카운트 : ", color = color000000, textEnd = item.totalCount.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 주간 :", color = color000000, textEnd = item.totalWeek.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 주간 카운트 : ", color = color000000, textEnd = item.totalWeekCount.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 월간 : ", color = color000000, textEnd = item.totalMonth.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spannableString(textFront = "베스트 월간 카운트 : ", color = color000000, textEnd = item.totalMonthCount.toString()),
                        color = color8E8E8E,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}