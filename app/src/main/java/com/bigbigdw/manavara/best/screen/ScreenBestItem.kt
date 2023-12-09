package com.bigbigdw.manavara.best.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.requiredWidth
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
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.best.getBestListTodayJson
import com.bigbigdw.manavara.best.getBestListWeekJson
import com.bigbigdw.manavara.best.models.ItemBestInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color02BC77
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.geMonthDate
import com.bigbigdw.manavara.best.getBestMonthListStorage
import com.bigbigdw.manavara.best.getBestMonthTrophy
import com.bigbigdw.manavara.best.getBestWeekListStorage
import com.bigbigdw.manavara.best.getBestWeekTrophy
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.getBookItemWeekTrophyDialog
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.util.getWeekDate
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

    LaunchedEffect(state.platform, state.type){
        getBookMap(
            platform = state.platform,
            type = state.type
        ) {
            viewModelBest.setItemBookInfoMap(it)
        }

        getBestListTodayJson(
            platform = state.platform,
            type = state.type,
            context = context
        ) {
            viewModelBest.setItemBestInfoList(it)
        }
    }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(colorF6F6F6)
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .fillMaxSize(),
        ) {

            itemsIndexed(state.itemBookInfoList) { index, item ->
                ListBestToday(
                    itemBookInfo = item,
                    index = index,
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen,
                    viewModelBest = viewModelBest
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
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelBest: ViewModelBest,
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelBest.state.collectAsState().value

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

                getBookItemWeekTrophy(
                    bookCode = itemBookInfo.bookCode,
                    platform = state.platform,
                    type = state.type
                ){
                    viewModelBest.setItemBestInfoTrophyList(itemBestInfoTrophyList = it, itemBookInfo = itemBookInfo)
                }

                coroutineScope.launch {
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
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value
    val (getDate, setDate) = remember { mutableStateOf("전체") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    getBookMap(
        platform = state.platform,
        type = state.type
    ) {
        viewModelBest.setItemBookInfoMap(it)
    }

    getBestListWeekJson(
        context = context,
        platform = state.platform,
        type = state.type,
    ) {
        viewModelBest.setWeekList(it)
    }

    getBestWeekTrophy(
        platform = state.platform,
        type = state.type
    ) {
        viewModelBest.setWeekTrophyList(it)
    }

    val filteredList: ArrayList<ItemBookInfo> = ArrayList()

    if(state.weekTrophyList.isNotEmpty() && state.itemBookInfoMap.isNotEmpty()){
        for (trophyItem in state.weekTrophyList) {
            val bookCode = trophyItem.bookCode
            val bookInfo = state.itemBookInfoMap[bookCode]

            Log.d("WEEK-FILTER", "bookCode == ${bookCode} bookInfo == $bookInfo ")

            if (bookInfo != null) {
                filteredList.add(bookInfo)
            }
        }
    }

    Column(modifier = Modifier.background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
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

        LazyColumn(
            modifier = Modifier
                .background(colorF6F6F6)
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
        ) {
            if (getDate == "전체") {

                itemsIndexed(filteredList) { index, item ->

                    ListBest(
                        item = item,
                        type = "WEEK",
                        index = index
                    ){
                        coroutineScope.launch {
                            viewModelBest.getBookItemInfo(itemBookInfo = item)

                            getBookItemWeekTrophyDialog(
                                itemBookInfo = item,
                                type = state.type,
                                platform = state.platform
                            ) { itemBookInfo, itemBestInfoTrophyList ->
                                viewModelBest.setItemBestInfoTrophyList(
                                    itemBookInfo = itemBookInfo,
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
            } else {
                if (state.weekList[getWeekDate(getDate)].size > 0) {
                    itemsIndexed(state.weekList[getWeekDate(getDate)]) { index, item ->
                        ListBestToday(
                            itemBookInfo = item,
                            index = index,
                            modalSheetState = modalSheetState,
                            setDialogOpen = setDialogOpen,
                            viewModelBest = viewModelBest,
                        )
                    }
                } else {
                    item { ScreenEmpty(str = "데이터가 없습니다") }
                }
            }
        }
    }
}

@Composable
fun ListBest(
    item: ItemBookInfo,
    type: String,
    index: Int,
    onClick: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

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
            coroutineScope.launch {
                onClick()
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(24.dp, 4.dp)
            ) {

                Spacer(modifier = Modifier.size(4.dp))

                Spacer(modifier = Modifier.size(4.dp))

                Column(modifier = Modifier.fillMaxWidth()) {

                    ScreenItemBestCard(item = item, index = index)

                    if (type == "MONTH") {
                        Spacer(modifier = Modifier.size(8.dp))

                        ScreenItemBestCount(item = item)
                    }

                    if(item.intro.isNotEmpty()){
                        Spacer(modifier = Modifier.size(16.dp))

                        Text(
                            text = item.intro,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenTodayMonth(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value
    val (getDate, setDate) = remember { mutableStateOf("전체") }
    val monthList = state.monthList
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

    getBookMap(
        platform = state.platform,
        type = state.type
    ) {
        viewModelBest.setItemBookInfoMap(it)
    }

    getBestListWeekJson(
        platform = state.platform,
        type = state.type,
        bestType = "MONTH",
        context = context
    ){
        viewModelBest.setMonthList(it)
    }

    getBestMonthTrophy(
        platform = state.platform,
        type = state.type,
    ){
        viewModelBest.setMonthTrophyList(it)
    }

    val filteredList: ArrayList<ItemBookInfo> = ArrayList()

    if(state.monthTrophyList.isNotEmpty() && state.itemBookInfoMap.isNotEmpty()){
        for (trophyItem in state.monthTrophyList) {
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
                    ListBest(
                        item = item,
                        type = "MONTH",
                        index = index,
                    ){
                        coroutineScope.launch {
                            viewModelBest.getBookItemInfo(itemBookInfo = item)

                            getBookItemWeekTrophyDialog(
                                itemBookInfo = item,
                                type = state.type,
                                platform = state.platform
                            ) { itemBookInfo, itemBestInfoTrophyList ->
                                viewModelBest.setItemBestInfoTrophyList(
                                    itemBookInfo = itemBookInfo,
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

                        ListBest(
                            item = item,
                            type = "WEEK",
                            index = index,
                        ){
                            coroutineScope.launch {
                                viewModelBest.getBookItemInfo(itemBookInfo = item)

                                getBookItemWeekTrophyDialog(
                                    itemBookInfo = item,
                                    type = state.type,
                                    platform = state.platform
                                ) { itemBookInfo, itemBestInfoTrophyList ->
                                    viewModelBest.setItemBestInfoTrophyList(
                                        itemBookInfo = itemBookInfo,
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
                ScreenEmpty(str = "데이터가 없습니다")
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenDialogBest(
    item: ItemBookInfo,
    trophy: ArrayList<ItemBestInfo>,
    isExpandedScreen: Boolean,
    currentRoute: String,
    modalSheetState: ModalBottomSheetState?
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        ScreenItemBestCard(item = item, index = -1)

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

    if(!isExpandedScreen){
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color8F8F8F),

                onClick = {  },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)

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
                onClick = {
                    coroutineScope.launch {
                        val intent = Intent(context, ActivityBestDetail::class.java)
                        intent.putExtra("BOOKCODE", item.bookCode)
                        intent.putExtra("PLATFORM", item.type)
                        intent.putExtra("TYPE", currentRoute)
                        context.startActivity(intent)
                        modalSheetState?.hide()
                    }
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
fun ScreenItemBestCard(item: ItemBookInfo, index: Int){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {

        Box{

            Card(
                modifier = Modifier.background(Color.White),
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

            if(index > -1){
                Card(
                    modifier = Modifier.padding(6.dp),
                    colors = CardDefaults.cardColors(containerColor = color20459E),
                    shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp),
                    border = BorderStroke(width = 1.dp, color = Color.White)
                ) {
                    Box(modifier = Modifier.size(35.dp), contentAlignment = Alignment.Center) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = "${index + 1}",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = Modifier.wrapContentHeight()) {
            Text(
                text = item.title,
                color = color20459E,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (item.writer.isNotEmpty()) {
                Text(
                    text = item.writer,
                    color = color000000,
                    fontSize = 16.sp,
                )
            }

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

            if (item.cntRecom.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "작품 추천 수 : ",
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

            if (item.bookCode.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "북코드 : ",
                        color = color000000,
                        textEnd = item.bookCode
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
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