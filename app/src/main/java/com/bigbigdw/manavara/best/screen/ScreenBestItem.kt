package com.bigbigdw.manavara.best.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.getBestListTodayJson
import com.bigbigdw.manavara.best.getBestListWeekJson
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.getBookMapJson
import com.bigbigdw.manavara.best.getTrophyWeekMonthJson
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.setIsPicked
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color02BC77
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenBookCardItem
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.spannableString
import com.bigbigdw.manavara.util.weekListAll
import com.bigbigdw.manavara.util.weekListOneWord
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTodayBest(
    listState: LazyListState,
    modalSheetState: ModalBottomSheetState?,
    dialogOpen: MutableState<Boolean>?,
    viewModelMain: ViewModelMain,
) {

    val mainState = viewModelMain.state.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelBest: ViewModelBest = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val bestState = viewModelBest.state.collectAsState().value

    LaunchedEffect(mainState.platform, mainState.isPickedBookCode){

        setIsPicked(
            context = context,
            platform = mainState.platform,
            type = mainState.type,
        ) { itemPickInfo ->

            getBookMapJson(
                platform = mainState.platform,
                type = mainState.type,
                context = context
            ){ itemBookInfoMap ->

                getBestListTodayJson(
                    context = context,
                    platform = mainState.platform,
                    type = mainState.type,
                    itemPickMap = itemPickInfo
                ) { itemBookInfoList ->

                    viewModelBest.setItemBestInfoList(itemBookInfoMap = itemBookInfoMap, itemBookInfoList = itemBookInfoList)
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .fillMaxSize(),
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(bestState.itemBookInfoList) { index, item ->
            ListBestToday(
                itemBookInfo = item,
                index = index,
                backGroundColor = if(item.belong == "SHARED"){
                    Color(0xFFEFE1FC)
                } else {
                    Color.White
                }
            ){

                getBookItemWeekTrophy(
                    bookCode = item.bookCode,
                    platform = mainState.platform,
                    type = mainState.type
                ){
                    viewModelMain.setItemBestInfoTrophyList(
                        itemBestInfoTrophyList = it,
                        itemBookInfo = item
                    )
                }

                coroutineScope.launch {
                    modalSheetState?.show()

                    dialogOpen?.value = true
                }
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@Composable
fun ListBestToday(
    itemBookInfo: ItemBookInfo,
    index: Int,
    backGroundColor : Color = Color.White,
    needStatus : Boolean = true,
    onClick: () -> Unit
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp),
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
            colors = ButtonDefaults.buttonColors(containerColor = backGroundColor),
            onClick = {
                onClick()
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

                    if(needStatus){
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
                }
            })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTodayWeek(
    modalSheetState: ModalBottomSheetState?,
    dialogOpen: MutableState<Boolean>?,
    viewModelMain: ViewModelMain,
    dayType : String = "WEEK"
) {

    val mainState = viewModelMain.state.collectAsState().value
    val (getDate, setDate) = remember { mutableStateOf("전체") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelBest: ViewModelBest = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val bestState = viewModelBest.state.collectAsState().value

    LaunchedEffect(mainState.platform, mainState.isPickedBookCode){
        setIsPicked(
            context = context,
            platform = mainState.platform,
            type = mainState.type,
        ) { itemPickInfo ->

            getBookMapJson(
                platform = mainState.platform,
                type = mainState.type,
                context = context
            ) { itemBookInfoMap ->

                getBestListWeekJson(
                    context = context,
                    platform = mainState.platform,
                    type = mainState.type,
                    itemPickInfo = itemPickInfo
                ) { weekList ->

                    getTrophyWeekMonthJson(
                        platform = mainState.platform,
                        type = mainState.type,
                        dayType = dayType,
                        context = context
                    ) { weekTrophyList ->

                        val filteredList: ArrayList<ItemBookInfo> = ArrayList()

                        for (trophyItem in weekTrophyList) {
                            val bookCode = trophyItem.bookCode
                            val bookInfo = itemBookInfoMap[bookCode]

                            if (bookInfo != null) {

                                if(itemPickInfo[bookInfo.bookCode] != null){
                                    bookInfo.belong = "SHARED"
                                } else {
                                    bookInfo.belong = ""
                                }

                                filteredList.add(bookInfo)
                            }
                        }

                        viewModelBest.setWeekList(
                            weekTrophyList = weekTrophyList,
                            weekList = weekList,
                            itemBookInfoMap = itemBookInfoMap,
                            filteredList = filteredList
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    )  {
        Spacer(modifier = Modifier.size(8.dp))

        LazyRow(
            modifier = Modifier
                .padding(8.dp, 8.dp, 0.dp, 8.dp)
                .background(color = colorF6F6F6),
        ) {

            itemsIndexed(if(dayType == "WEEK"){
                weekListAll()
            } else {
                val arrayList = ArrayList<String>()
                var count = 0

                arrayList.add("전체")

                for (item in bestState.weekMonthList) {
                    count += 1
                    arrayList.add("${count}주차")
                }

                arrayList
            }) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = getDate,
                        onClick = {
                            coroutineScope.launch {
                                setDate(item)
                                listState.scrollToItem(index = 0)
                            }
                        },
                        title = item,
                        getValue = item
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn(state = listState) {

            if (getDate == "전체") {

                itemsIndexed(bestState.filteredList) { index, item ->

                    ScreenBookCard(
                        type = dayType,
                        item = item,
                        index = index,
                        backgroundColor = if(item.belong == "SHARED"){
                            Color(0xFFEFE1FC)
                        } else {
                            Color.White
                        }
                    ){
                        coroutineScope.launch {

                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                type = mainState.type,
                                platform = mainState.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelMain.setItemBestInfoTrophyList(
                                    itemBookInfo = item,
                                    itemBestInfoTrophyList = itemBestInfoTrophyList
                                )

                            }

                            modalSheetState?.show()

                            dialogOpen?.value = true

                            listState.scrollToItem(index = 0)
                        }
                    }

                }
            } else {
                if (bestState.weekMonthList[getWeekDate(getDate)].size > 0) {
                    itemsIndexed(bestState.weekMonthList[getWeekDate(getDate)]) { index, item ->
                        ListBestToday(
                            itemBookInfo = item,
                            index = index,
                            backGroundColor = if(item.belong == "SHARED"){
                                Color(0xFFEFE1FC)
                            } else {
                                Color.White
                            }
                        ){

                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                platform = mainState.platform,
                                type = mainState.type
                            ){
                                viewModelMain.setItemBestInfoTrophyList(
                                    itemBestInfoTrophyList = it,
                                    itemBookInfo = item
                                )
                            }

                            coroutineScope.launch {
                                modalSheetState?.show()

                                dialogOpen?.value = true
                            }
                        }
                    }
                } else {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ScreenEmpty(str = "데이터가 없습니다")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ScreenDialogBest(
    item: ItemBookInfo,
    isPicked : Boolean,
    trophy: ArrayList<ItemBestInfo>,
    isExpandedScreen: Boolean,
    needTrophyList: Boolean = true,
    btnPickText: String = "작품 PICK 하기",
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        ScreenBookCardItem(mode = "NUMBER", item = item, index = -1)

        Spacer(modifier = Modifier.size(8.dp))

        if(isExpandedScreen){

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

    if(needTrophyList){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 16.dp),
        ) {
            trophy.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = weekListOneWord()[index],
                            color = if (item.number > -1) {
                                color1CE3EE
                            } else {
                                color8F8F8F
                            },
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp, 0.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(
                                    id = if (item.number > -1) {
                                        R.drawable.icon_trophy_fill_on
                                    } else {
                                        R.drawable.icon_trophy_fill_off
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(28.dp)
                            )
                        }

                        if(item.number > -1){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "${item.number + 1}등",
                                color = color1CE3EE,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    if(!isExpandedScreen){
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPicked) {
                        color4AD7CF
                    } else {
                        color8F8F8F
                    }
                ),

                onClick = { onClickLeft() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)

            ) {
                Text(
                    text = btnPickText,
                    textAlign = TextAlign.Center,
                    color = if (isPicked) {
                        Color.Black
                    } else {
                        Color.White
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                onClick = {
                    onClickRight()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)

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