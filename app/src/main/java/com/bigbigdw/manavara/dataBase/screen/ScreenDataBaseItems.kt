package com.bigbigdw.manavara.dataBase.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.getTrophyWeekMonthJson
import com.bigbigdw.manavara.best.getBookMapJson
import com.bigbigdw.manavara.best.gotoUrl
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.screen.ScreenBestDetailInfo
import com.bigbigdw.manavara.best.setBestDetailInfo
import com.bigbigdw.manavara.dataBase.getGenreKeywordJson
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.MainHeader
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
    setDialogOpen: ((Boolean) -> Unit)?,
    isExpandedScreen: Boolean
) {

    val state = viewModelDatabase.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if(isExpandedScreen){

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
        }

        if (state.detail.isNotEmpty()) {

            ScreenAnalyzeItemDetail(
                viewModelDatabase = viewModelDatabase,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen
            )
        } else {

            ScreenDataBaseItem(
                viewModelDatabase = viewModelDatabase,
                drawerState = drawerState,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen,
                isExpandedScreen = isExpandedScreen
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDataBaseList(
    drawerState: DrawerState?,
    viewModelDatabase: ViewModelDatabase,
    isExpandedScreen : Boolean
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

        if(!isExpandedScreen){
            item{
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.size(16.dp))

                    MainHeader(image = R.drawable.logo_transparents, title = state.menuDesc)
                }
            }
        }

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
                                type = state.type,
                                menuDesc = state.menuDesc
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
    val context = LocalContext.current

    getBookMapJson(
        platform = state.platform,
        type = state.type,
        context = context
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
            ScreenBookCard(
                item = item,
                type = "MONTH",
                index = index,
            ) {
                coroutineScope.launch {

                    getBookItemWeekTrophy(
                        bookCode = item.bookCode,
                        type = state.type,
                        platform = state.platform
                    ) { itemBestInfoTrophyList ->

                        viewModelDatabase.setItemBestInfoTrophyList(
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
                viewModelDatabase.setScreen(detail = "", menu = state.menu, platform = state.platform, type = state.type, menuDesc = state.menuDesc)
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
    val context = LocalContext.current

    getJsonFiles(
        platform = state.platform,
        type = state.type,
        root = root,
    ) {

        Log.d("ScreenBestAnalyze", "getJsonFiles == ${it.size}")

        viewModelDatabase.setJsonNameList(it)

        if(it.isNotEmpty()){
            if (state.date.isEmpty()) {
                viewModelDatabase.setDate(date = it.get(0), jsonNameList = it, dateType = root)
            }
        }
    }

    if (state.jsonNameList.isNotEmpty()) {

        if (root == "BEST_WEEK") {
            getTrophyWeekMonthJson(
                platform = state.platform,
                type = state.type,
                root = state.date,
                dayType = "WEEK",
                context = context
            ) {
                viewModelDatabase.setWeekTrophyList(it)
            }
        } else {
            getTrophyWeekMonthJson(
                platform = state.platform,
                type = state.type,
                dayType = "MONTH",
                context = context
            ) { weekTrophyList ->
                viewModelDatabase.setWeekTrophyList(weekTrophyList)
            }
        }

        getBookMapJson(
            platform = state.platform,
            type = state.type,
            context = context
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
                                    viewModelDatabase.setDate(date = item)

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = if (root == "BEST_WEEK") {
                                convertDateStringWeek(item)
                            } else {
                                convertDateStringMonth(item)
                            },
                            getValue = if (root == "BEST_WEEK") {
                                convertDateStringWeek(state.date)
                            } else {
                                convertDateStringMonth(state.date)
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
                    ScreenBookCard(
                        item = item,
                        type = root,
                        index = index
                    ) {
                        coroutineScope.launch {

                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                type = state.type,
                                platform = state.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelDatabase.setItemBestInfoTrophyList(
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
            item { ScreenEmpty(str = "데이터가 없습니다") }
        }
    }
}

@Composable
fun ScreenGenreKeywordToday(
    viewModelDatabase: ViewModelDatabase,
    dataType: String,
    itemList: List<String> = novelListEng()
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDatabase.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.date) {
        getGenreKeywordJson(
            context = context,
            platform = state.platform.ifEmpty { "JOARA" },
            type = state.type,
            dataType = dataType
        ) {
            viewModelDatabase.setGenreKeywordList(genreKeywordList = it)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(colorF6F6F6),
    ) {

        item {
            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(itemList) { index, item ->
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
                            getValue = state.platform.ifEmpty { "JOARA" }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.size(4.dp))
        }

        itemsIndexed(state.genreKeywordList) { index, item ->

            val value = if(dataType == "KEYWORD"){
                (item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }).toString()
            } else {
                item.value
            }

            ListGenreKeywordToday(
                title = item.key,
                value = value,
                index = index,
            )
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestDataGenreKeywordList(
    drawerState: DrawerState?,
    viewModelDatabase: ViewModelDatabase,
    isExpandedScreen : Boolean,
    itemList: List<String> = novelListEng()
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelDatabase.state.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorF6F6F6),
    ) {

        if(!isExpandedScreen){
            item{
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.size(16.dp))

                    MainHeader(image = R.drawable.logo_transparents, title = state.menuDesc)
                }
            }
        }

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
                                type = state.type,
                                menuDesc = state.menuDesc
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenSearchDataBase(
    viewModelDatabase: ViewModelDatabase,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
) {

    val state = viewModelDatabase.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(state.platform, state.searchQuery, state.itemBookInfoMap){
        getBookMapJson(
            platform = state.platform,
            type = state.type,
            context = context
        ) {

            val result = ArrayList<ItemBookInfo>()

            for (item in it) {
                if (item.value.title.contains(state.searchQuery)) {
                    result.add(item.value)
                }
            }

            viewModelDatabase.setSearch(itemBookInfoMap = it, filteredList = result)
        }
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
                                        menuDesc = state.menuDesc
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
                    ScreenBookCard(
                        item = item,
                        index = index,
                    ) {
                        coroutineScope.launch {

                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                type = state.type,
                                platform = state.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelDatabase.setItemBestInfoTrophyList(
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenSearchDataBaseDetail(
    viewModelDatabase: ViewModelDatabase,
) {

    val state = viewModelDatabase.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(state.searchQuery) {
        setBestDetailInfo(
            platform = state.platform,
            bookCode = state.searchQuery,
            context = context
        ){
            viewModelDatabase.setBestDetailInfo(it)
        }
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
                label = { Text("북코드 입력 ex) 1724803", color = color898989) },
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
                                        menuDesc = state.menuDesc
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
            Spacer(modifier = Modifier.size(16.dp))
        }

        if(state.searchQuery.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            item {
                ScreenItemSearchBookCodeCard(
                    viewModelDatabase = viewModelDatabase
                )
            }
        }
    }
}

@Composable
fun ScreenItemSearchBookCodeCard(
    viewModelDatabase: ViewModelDatabase
) {

    val context = LocalContext.current
    val state = viewModelDatabase.state.collectAsState().value

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
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                AsyncImage(
                    model = state.itemBestDetailInfo.bookImg,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredWidth(140.dp)
                        .requiredHeight(200.dp)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                modifier = Modifier.padding(16.dp, 0.dp),
                text = state.itemBestDetailInfo.title,
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
                gotoUrl(
                    platform = state.platform,
                    bookCode = state.itemBestDetailInfo.bookCode,
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

        ScreenBestDetailInfo(item = state.itemBestDetailInfo)

        Spacer(modifier = Modifier.size(32.dp))
    }
}