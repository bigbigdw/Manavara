package com.bigbigdw.manavara.best.screen

import android.util.Log
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
import com.bigbigdw.manavara.best.getBestListTodayJson
import com.bigbigdw.manavara.best.getBestListWeekJson
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.getBookMapJson
import com.bigbigdw.manavara.best.getTrophyWeekMonthJson
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color02BC77
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.geMonthDate
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenBookCardItem
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.spannableString
import com.bigbigdw.manavara.util.weekList
import com.bigbigdw.manavara.util.weekListAll
import com.bigbigdw.manavara.util.weekListOneWord
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTodayBest(
    listState: LazyListState,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Log.d("ScreenTodayBest", "state.platform == ${state.platform}")

    LaunchedEffect(state.platform){

        Log.d("ScreenTodayBest", "LaunchedEffect state.platform == ${state.platform}")

        getBookMapJson(
            platform = state.platform,
            type = state.type,
            context = context
        ){ itemBookInfoMap ->

            Log.d("ScreenTodayBest", "itemBookInfoMap == ${itemBookInfoMap.size}")

            getBestListTodayJson(
                context = context,
                platform = state.platform,
                type = state.type
            ) { itemBookInfoList ->

                Log.d("ScreenTodayBest", "itemBookInfoMap == ${itemBookInfoMap.size} itemBookInfoList == ${itemBookInfoList.size}")

                viewModelBest.setItemBestInfoList(itemBookInfoMap = itemBookInfoMap, itemBookInfoList = itemBookInfoList)
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

        itemsIndexed(state.itemBookInfoList) { index, item ->
            ListBestToday(
                itemBookInfo = item,
                index = index,
            ){

                getBookItemWeekTrophy(
                    bookCode = item.bookCode,
                    platform = state.platform,
                    type = state.type
                ){
                    viewModelBest.setItemBestInfoTrophyList(
                        itemBestInfoTrophyList = it,
                        itemBookInfo = item
                    )
                }

                coroutineScope.launch {
                    modalSheetState?.show()

                    if (setDialogOpen != null) {
                        setDialogOpen(true)
                    }
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
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
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
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value
    val (getDate, setDate) = remember { mutableStateOf("전체") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    getBookMapJson(
        platform = state.platform,
        type = state.type,
        context = context
    ) { itemBookInfoMap ->

        getBestListWeekJson(
            context = context,
            platform = state.platform,
            type = state.type,
        ) { weekList ->

            getTrophyWeekMonthJson(
                platform = state.platform,
                type = state.type,
                dayType = "WEEK",
                context = context
            ) { weekTrophyList ->

                viewModelBest.setWeekList(
                    weekTrophyList = weekTrophyList,
                    weekList = weekList,
                    itemBookInfoMap = itemBookInfoMap
                )
            }
        }
    }

    val filteredList: ArrayList<ItemBookInfo> = ArrayList()

    if(state.weekMonthTrophyList.isNotEmpty() && state.itemBookInfoMap.isNotEmpty()){
        for (trophyItem in state.weekMonthTrophyList) {
            val bookCode = trophyItem.bookCode
            val bookInfo = state.itemBookInfoMap[bookCode]

            if (bookInfo != null) {
                filteredList.add(bookInfo)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp).background(color = colorF6F6F6),
                state = listState,
            ) {

                itemsIndexed(weekListAll()) { index, item ->
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
        }

        if (getDate == "전체") {

            itemsIndexed(filteredList) { index, item ->

                ScreenBookCard(
                    item = item,
                    type = "WEEK",
                    index = index
                ){
                    coroutineScope.launch {

                        getBookItemWeekTrophy(
                            bookCode = item.bookCode,
                            type = state.type,
                            platform = state.platform
                        ) { itemBestInfoTrophyList ->

                            viewModelBest.setItemBestInfoTrophyList(
                                itemBookInfo = item,
                                itemBestInfoTrophyList = itemBestInfoTrophyList
                            )

                        }

                        modalSheetState?.show()

                        if (setDialogOpen != null) {
                            setDialogOpen(true)
                        }

                        listState.scrollToItem(index = 0)
                    }
                }

            }
        } else {
            if (state.weekMonthList[getWeekDate(getDate)].size > 0) {
                itemsIndexed(state.weekMonthList[getWeekDate(getDate)]) { index, item ->
                    ListBestToday(
                        itemBookInfo = item,
                        index = index,
                    ){

                        getBookItemWeekTrophy(
                            bookCode = item.bookCode,
                            platform = state.platform,
                            type = state.type
                        ){
                            viewModelBest.setItemBestInfoTrophyList(
                                itemBestInfoTrophyList = it,
                                itemBookInfo = item
                            )
                        }

                        coroutineScope.launch {
                            modalSheetState?.show()

                            if (setDialogOpen != null) {
                                setDialogOpen(true)
                            }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTodayMonth(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value
    val (getDate, setDate) = remember { mutableStateOf("전체") }
    val monthList = state.weekMonthList
    val arrayList = ArrayList<String>()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    arrayList.add("전체")

    var count = 0

    for (item in monthList) {
        count += 1
        arrayList.add("${count}주차")
    }

    getBookMapJson(
        platform = state.platform,
        type = state.type,
        context = context
    ) { itemBookInfoMap ->

        getBestListWeekJson(
            context = context,
            platform = state.platform,
            type = state.type,
            bestType = "MONTH"
        ){ weekList ->
            getTrophyWeekMonthJson(
                platform = state.platform,
                type = state.type,
                dayType = "MONTH",
                context = context
            ) { weekTrophyList ->

                viewModelBest.setWeekList(
                    weekTrophyList = weekTrophyList,
                    weekList = weekList,
                    itemBookInfoMap = itemBookInfoMap
                )
            }
        }
    }

    val filteredList: ArrayList<ItemBookInfo> = ArrayList()

    if(state.weekMonthTrophyList.isNotEmpty() && state.itemBookInfoMap.isNotEmpty()){
        for (trophyItem in state.weekMonthTrophyList) {
            val bookCode = trophyItem.bookCode
            val bookInfo = state.itemBookInfoMap[bookCode]

            if (bookInfo != null) {
                filteredList.add(bookInfo)
            }
        }

    }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(arrayList) { index, item ->

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

        if (getDate == "전체") {
            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {

                itemsIndexed(filteredList) { index, item ->
                    ScreenBookCard(
                        item = item,
                        type = "MONTH",
                        index = index,
                    ){
                        coroutineScope.launch {

                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                type = state.type,
                                platform = state.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelBest.setItemBestInfoTrophyList(
                                    itemBookInfo = item,
                                    itemBestInfoTrophyList = itemBestInfoTrophyList
                                )

                            }

                            modalSheetState?.show()

                            if (setDialogOpen != null) {
                                setDialogOpen(true)
                            }
                        }
                    }
                }
            }
        } else {

            if (monthList[geMonthDate(getDate)].size > 0) {
                LazyColumn(
                    modifier = Modifier
                        .background(colorF6F6F6)
                        .padding(16.dp, 0.dp, 16.dp, 0.dp)
                ) {

                    itemsIndexed(filteredList) { index, item ->

                        val itemBookInfo = state.itemBookInfoMap[item.bookCode]

                        if (itemBookInfo != null) {
                            Text(
                                modifier = Modifier.padding(16.dp, 8.dp),
                                text = weekList()[index],
                                fontSize = 16.sp,
                                color = color8E8E8E,
                                fontWeight = FontWeight(weight = 700)
                            )
                        }

                        ScreenBookCard(
                            item = item,
                            type = "WEEK",
                            index = index,
                        ){
                            coroutineScope.launch {
                                viewModelBest.getBookItemInfo(itemBookInfo = item)

                                getBookItemWeekTrophy(
                                    bookCode = item.bookCode,
                                    type = state.type,
                                    platform = state.platform
                                ) { itemBestInfoTrophyList ->

                                    viewModelBest.setItemBestInfoTrophyList(
                                        itemBookInfo = item,
                                        itemBestInfoTrophyList = itemBestInfoTrophyList
                                    )

                                }

                                modalSheetState?.show()

                                if (setDialogOpen != null) {
                                    setDialogOpen(true)
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
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