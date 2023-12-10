package com.bigbigdw.manavara.dataBase.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.getBestMonthTrophy
import com.bigbigdw.manavara.best.getBestWeekTrophy
import com.bigbigdw.manavara.best.getBookItemWeekTrophyDialog
import com.bigbigdw.manavara.best.getBookMap
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.screen.ListBest
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import convertDateStringMonth
import convertDateStringWeek
import getBookCount
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenDataBaseItems(
    viewModelDatabase: ViewModelDatabase,
    drawerState: DrawerState?,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelDatabase.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        if (state.detail.isNotEmpty()) {

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
                    text = state.detail,
                    fontSize = 24.sp,
                    color = color000000,
                    fontWeight = FontWeight(weight = 700)
                )
            }


        } else if (state.menu.isNotEmpty()) {

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
                    text = state.menu,
                    fontSize = 24.sp,
                    color = color000000,
                    fontWeight = FontWeight(weight = 700)
                )
            }
        }

        if (state.detail.isNotEmpty()) {

            ScreenAnalyzeItemDetail(
                viewModelDatabase = viewModelDatabase,
                drawerState = drawerState,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen
            )
        } else {

            ScreenDataBaseItem(
                viewModelDatabase = viewModelDatabase,
                drawerState = drawerState,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen
            )
        }
    }
}

@Composable
fun ScreenDataBaseTopbar(onClick: () -> Unit) {

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
                modifier = Modifier.clickable { onClick() }) {
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
                    text = "마나바라",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )

            }

        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDataBaseList(
    drawerState: DrawerState?,
    viewModelDatabase: ViewModelDatabase
) {
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDatabase.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6),
    ) {

        item { Spacer(modifier = Modifier.size(8.dp)) }

        val itemList = if (state.type == "NOVEL") novelListEng() else comicListEng()

        itemsIndexed(itemList) { index, item ->
            Box(modifier = Modifier.padding(8.dp)) {
                TabletContentWrapBtn(
                    onClick = {
                        coroutineScope.launch {

                            viewModelDatabase.setScreen(
                                menu = state.menu,
                                detail = "${changePlatformNameKor(item)} ${state.menu}",
                                platform = item,
                                type = state.type
                            )
                            drawerState?.close()
                        }
                    },
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

                            getBookCount(context = context, type = state.type, platform = item)

                            Text(
                                text = spannableString(
                                    textFront = "${changePlatformNameKor(item)} : ",
                                    color = color000000,
                                    textEnd = "${
                                        dataStore.getDataStoreString(
                                            if (state.type == "NOVEL") getPlatformDataKeyNovel(item) else getPlatformDataKeyComic(
                                                item
                                            )
                                        ).collectAsState(initial = "").value ?: "0"
                                    } 작품"
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBookMap(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelDatabase: ViewModelDatabase
) {

    val state = viewModelDatabase.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    getBookMap(
        platform = state.platform,
        type = state.type
    ) {
        viewModelDatabase.setItemBookInfoMap(it)
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(ArrayList(state.itemBookInfoMap.values)) { index, item ->
            ListBest(
                item = item,
                type = "MONTH",
                index = index,
            ) {
                coroutineScope.launch {
                    viewModelDatabase.setItemBookInfo(itemBookInfo = item)

                    getBookItemWeekTrophyDialog(
                        itemBookInfo = item,
                        type = state.type,
                        platform = state.platform
                    ) { itemBookInfo, itemBestInfoTrophyList ->
                        viewModelDatabase.setItemBestInfoTrophyList(
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
}

@Composable
fun BestAnalyzeBackOnPressed(
    viewModelDatabase: ViewModelDatabase,
) {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    val coroutineScope = rememberCoroutineScope()

    val state = viewModelDatabase.state.collectAsState().value

    BackHandler(enabled = true) {

        if (state.detail.isNotEmpty()) {
            coroutineScope.launch {
                viewModelDatabase.setScreen(detail = "", menu = state.menu)
            }
        } else {
            if (System.currentTimeMillis() - backPressedTime <= 400L) {
                // 앱 종료
                (context as Activity).finish()
            } else {
                backPressedState = true
                Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBestAnalyze(
    viewModelDatabase: ViewModelDatabase,
    root: String,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelDatabase.state.collectAsState().value
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    getJsonFiles(
        platform = state.platform,
        type = state.type,
        root = root,
    ) {
        viewModelDatabase.setJsonNameList(it)

        if (root == "BEST_WEEK") {
            if (state.week.isEmpty()) {
                viewModelDatabase.setDate(week = it.get(0))
            }
        } else {
            if (state.month.isEmpty()) {
                viewModelDatabase.setDate(month = it.get(0))
            }
        }
    }

    if (state.jsonNameList.isNotEmpty()) {

        if (root == "BEST_WEEK") {
            getBestWeekTrophy(
                platform = state.platform,
                type = state.type,
                root = state.week
            ) {
                viewModelDatabase.setWeekTrophyList(it)
            }
        } else {
            getBestMonthTrophy(
                platform = state.platform,
                type = state.type,
                root = state.month
            ) {
                viewModelDatabase.setWeekTrophyList(it)
            }
        }

        getBookMap(
            platform = state.platform,
            type = state.type
        ) {
            viewModelDatabase.setItemBookInfoMap(it)
        }

        viewModelDatabase.setFilteredListTrophy()
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
    ) {

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
            ) {
                itemsIndexed(state.jsonNameList) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = if (root == "BEST_WEEK") {
                                convertDateStringWeek(item)
                            } else {
                                convertDateStringMonth(item)
                            },
                            onClick = {
                                coroutineScope.launch {
                                    if (root == "BEST_WEEK") {
                                        viewModelDatabase.setDate(week = item)
                                    } else {
                                        viewModelDatabase.setDate(month = item)
                                    }

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = if (root == "BEST_WEEK") {
                                convertDateStringWeek(item)
                            } else {
                                convertDateStringMonth(item)
                            },
                            getValue = if (root == "BEST_WEEK") {
                                convertDateStringWeek(state.week)
                            } else {
                                convertDateStringMonth(state.month)
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.size(8.dp))
        }

        if (state.jsonNameList.isNotEmpty()) {
            itemsIndexed(state.filteredList) { index, item ->

                Box(
                    modifier = Modifier
                        .background(colorF6F6F6)
                        .padding(16.dp, 0.dp, 16.dp, 0.dp)
                ) {
                    ListBest(
                        item = item,
                        type = root,
                        index = index
                    ) {
                        coroutineScope.launch {
                            viewModelDatabase.setItemBookInfo(itemBookInfo = item)

                            getBookItemWeekTrophyDialog(
                                itemBookInfo = item,
                                type = state.type,
                                platform = state.platform
                            ) { itemBookInfo, itemBestInfoTrophyList ->
                                viewModelDatabase.setItemBestInfoTrophyList(
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
            item { ScreenEmpty(str = "데이터가 없습니다") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDataBaseList(
    drawerState: DrawerState?,
    viewModelDatabase: ViewModelDatabase,
    itemList: List<String> = novelListEng()
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDatabase.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorF6F6F6),
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(itemList) { index, item ->
            Box(modifier = Modifier.padding(8.dp)) {
                TabletContentWrapBtn(
                    onClick = {
                        coroutineScope.launch {

                            viewModelDatabase.setScreen(
                                menu = state.menu,
                                detail = "${changePlatformNameKor(item)} ${state.menu}",
                                platform = item,
                                type = state.type
                            )
                            drawerState?.close()
                        }
                    },
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

                            Text(
                                text = changePlatformNameKor(item),
                                color = color000000,
                                fontSize = 18.sp,
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ListSearch(
    platform: String,
    item: ItemBookInfo,
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

                Spacer(modifier = Modifier.size(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {

                    ScreenItemSearch(item = item, index = index, platform = platform)

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

@Composable
fun ScreenItemSearch(item: ItemBookInfo, index: Int, platform: String){
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
                        AsyncImage(
                            model = getPlatformLogoEng(platform),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenSearchDataBase(
    viewModelDatabase: ViewModelDatabase,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelDatabase.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.platform){
        getBookMap(
            platform = state.platform,
            type = state.type
        ) {

            val list = state.filteredList.map { it } as ArrayList

            viewModelDatabase.setSearch(itemBookInfoMap = it, filteredList = list)
        }
    }

    LaunchedEffect(state.searchQuery, state.itemBookInfoMap){
        val result = ArrayList<ItemBookInfo>()

        for (item in state.itemBookInfoMap) {
            if (item.value.title.contains(state.searchQuery)) {
                result.add(item.value)
            }
        }

        viewModelDatabase.setFilteredList(result)
    }

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6)
    ) {

        item{
            TextField(
                value = state.searchQuery,
                onValueChange = {
                    viewModelDatabase.setSearchQuery(it)
                },
                label = { Text("검색어 입력", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(novelListEng()) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelDatabase.setScreen(
                                        platform = item,
                                        type = "NOVEL",
                                        menu = state.menu,
                                    )
                                }
                            },
                            title = changePlatformNameKor(item),
                            getValue = state.platform
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.size(4.dp))
        }

        if(state.filteredList.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            itemsIndexed(state.filteredList) { index, item ->

                Box(modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    .wrapContentSize()){
                    ListSearch(
                        item = item,
                        platform = state.platform,
                        index = index,
                    ) {
                        coroutineScope.launch {
                            viewModelDatabase.setItemBookInfo(itemBookInfo = item)

                            getBookItemWeekTrophyDialog(
                                itemBookInfo = item,
                                type = state.type,
                                platform = state.platform
                            ) { itemBookInfo, itemBestInfoTrophyList ->
                                viewModelDatabase.setItemBestInfoTrophyList(
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenSearchDataBaseDetail(
    viewModelDatabase: ViewModelDatabase,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelDatabase.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.platform){
        getBookMap(
            platform = state.platform,
            type = state.type
        ) {
            viewModelDatabase.setSearch(itemBookInfoMap = it, filteredList = ArrayList())
        }
    }

    LaunchedEffect(state.searchQuery, state.itemBookInfoMap){
        val result = ArrayList<ItemBookInfo>()

        for (item in state.itemBookInfoMap) {
            if (item.value.title.contains(state.searchQuery)) {
                result.add(item.value)
            }
        }

        viewModelDatabase.setFilteredList(result)
    }

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorF6F6F6)
    ) {

        item{
            TextField(
                value = state.searchQuery,
                onValueChange = {
                    viewModelDatabase.setSearchQuery(it)
                },
                label = { Text("검색어 입력", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(novelListEng()) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelDatabase.setScreen(
                                        platform = item,
                                        type = "NOVEL",
                                        menu = state.menu,
                                    )
                                }
                            },
                            title = changePlatformNameKor(item),
                            getValue = state.platform
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.size(4.dp))
        }

        if(state.filteredList.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            itemsIndexed(state.filteredList) { index, item ->

                Box(modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    .wrapContentSize()){
                    ListSearch(
                        item = item,
                        platform = state.platform,
                        index = index,
                    ) {
                        coroutineScope.launch {
                            viewModelDatabase.setItemBookInfo(itemBookInfo = item)

                            getBookItemWeekTrophyDialog(
                                itemBookInfo = item,
                                type = state.type,
                                platform = state.platform
                            ) { itemBookInfo, itemBestInfoTrophyList ->
                                viewModelDatabase.setItemBestInfoTrophyList(
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
        }
    }
}