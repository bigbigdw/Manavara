package com.bigbigdw.manavara.dataBase.screen

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.bigbigdw.manavara.dataBase.setBookSearch
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import convertDateStringMonth
import convertDateStringWeek
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenDataBaseItems(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    isExpandedScreen: Boolean,
    viewModelMain: ViewModelMain
) {

    val state = viewModelMain.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if(isExpandedScreen){

            Spacer(modifier = Modifier.size(16.dp))

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

        ScreenDataBaseItem(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            viewModelMain = viewModelMain
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBookMap(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelMain: ViewModelMain
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val dataBaseState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(mainState.platform, mainState.type){
        getBookMapJson(
            platform = mainState.platform,
            type = mainState.type,
            context = context
        ) {
            viewModelDatabase.setItemBookInfoMap(it)
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        itemsIndexed(ArrayList(dataBaseState.itemBookInfoMap.values)) { index, item ->
            ScreenBookCard(
                type = "MONTH",
                item = item,
                index = index,
            ) {
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
    viewModelMain: ViewModelMain,
) {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    val coroutineScope = rememberCoroutineScope()

    val state = viewModelMain.state.collectAsState().value

    BackHandler(enabled = true) {

        if (state.detail.isNotEmpty()) {
            coroutineScope.launch {
                viewModelMain.setScreen(menu = state.menu, platform = state.platform, type = state.type)
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
    root: String,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelMain: ViewModelMain,
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val dataBaseState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(root, mainState.platform, dataBaseState.date){
        getJsonFiles(
            platform = mainState.platform,
            type = mainState.type,
            root = root,
        ) { jsonNameList ->

            if (root == "BEST_WEEK") {
                getTrophyWeekMonthJson(
                    platform = mainState.platform,
                    type = mainState.type,
                    root = dataBaseState.date.ifEmpty { jsonNameList[0] },
                    dayType = "WEEK",
                    context = context
                ) { weekTrophyList ->

                    getBookMapJson(
                        platform = mainState.platform,
                        type = mainState.type,
                        context = context
                    ) { itemBookInfoMap ->

                        viewModelDatabase.setScreenBestAnalyze(
                            date = dataBaseState.date.ifEmpty { jsonNameList[0] },
                            jsonNameList = jsonNameList,
                            dateType = root,
                            weekTrophyList = weekTrophyList,
                            itemBookInfoMap = itemBookInfoMap
                        )
                    }
                }
            } else {
                getTrophyWeekMonthJson(
                    platform = mainState.platform,
                    type = mainState.type,
                    dayType = "MONTH",
                    context = context
                ) { weekTrophyList ->

                    getBookMapJson(
                        platform = mainState.platform,
                        type = mainState.type,
                        context = context
                    ) { itemBookInfoMap ->

                        viewModelDatabase.setScreenBestAnalyze(
                            date = dataBaseState.date.ifEmpty { jsonNameList[0] },
                            jsonNameList = jsonNameList,
                            dateType = root,
                            weekTrophyList = weekTrophyList,
                            itemBookInfoMap = itemBookInfoMap
                        )
                    }
                }
            }
        }
    }


    Column(modifier = Modifier.background(colorF6F6F6))
    {
        LazyRow(
            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
        ) {
            itemsIndexed(dataBaseState.jsonNameList) { index, item ->
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
                            convertDateStringWeek(dataBaseState.date)
                        } else {
                            convertDateStringMonth(dataBaseState.date)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        if (dataBaseState.jsonNameList.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .background(colorF6F6F6)
            ) {

                itemsIndexed(dataBaseState.filteredList) { index, item ->

                    Column(
                        modifier = Modifier
                            .background(colorF6F6F6)
                            .padding(16.dp, 0.dp, 16.dp, 0.dp)
                    ) {
                        ScreenBookCard(
                            type = root,
                            item = item,
                            index = index
                        ) {
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

                                if (setDialogOpen != null) {
                                    setDialogOpen(true)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.size(60.dp))
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                ScreenEmpty(str = "데이터가 없습니다")
            }
        }
    }
}

@Composable
fun ScreenGenreKeywordToday(
    dataType: String,
    itemList: List<String> = novelListEng(),
    viewModelMain: ViewModelMain
) {

    val coroutineScope = rememberCoroutineScope()
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(manavaraState.platform, manavaraState.type) {
        getGenreKeywordJson(
            context = context,
            platform = manavaraState.platform.ifEmpty { mainState.platform },
            type = manavaraState.type.ifEmpty { mainState.type },
            dataType = dataType
        ) {
            viewModelDatabase.setGenreKeywordList(genreKeywordList = it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(colorF6F6F6),
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        LazyRow(
            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
        ) {
            itemsIndexed(itemList) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = item,
                        onClick = {
                            coroutineScope.launch {
                                viewModelDatabase.setView(
                                    platform = item,
                                    type = "NOVEL",
                                )
                            }
                        },
                        title = changePlatformNameKor(item),
                        getValue = manavaraState.platform.ifEmpty { "JOARA" }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(4.dp))

        LazyColumn {

            itemsIndexed(manavaraState.genreKeywordList) { index, item ->

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
        }

        Spacer(modifier = Modifier.size(60.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenSearchDataBase(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelMain: ViewModelMain,
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(mainState.platform, manavaraState.searchQuery, manavaraState.itemBookInfoMap){
        getBookMapJson(
            platform = mainState.platform,
            type = mainState.type,
            context = context
        ) {

            val result = ArrayList<ItemBookInfo>()

            for (item in it) {
                if (item.value.title.contains(manavaraState.searchQuery)) {
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
                value = manavaraState.searchQuery,
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
            Spacer(modifier = Modifier.size(4.dp))
        }

        if(manavaraState.filteredList.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            itemsIndexed(manavaraState.filteredList) { index, item ->

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
                                type = mainState.type,
                                platform = mainState.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelMain.setItemBestInfoTrophyList(
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenSearchAPI(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    viewModelMain: ViewModelMain,
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(mainState.platform, manavaraState.searchQuery, manavaraState.itemBookInfoMap){
        setBookSearch(
            query = manavaraState.searchQuery,
            platform = manavaraState.platform.ifEmpty { mainState.platform },
            context = context
        ) {
            viewModelDatabase.setFilteredList(filteredList = it)
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
                value = manavaraState.searchQuery,
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

        if(manavaraState.filteredList.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            itemsIndexed(manavaraState.filteredList) { index, item ->

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
                                type = mainState.type,
                                platform = mainState.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelMain.setItemBestInfoTrophyList(
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
fun ScreenSearchDataBaseDetail(viewModelMain: ViewModelMain) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(mainState.platform, manavaraState.searchQuery) {
        setBestDetailInfo(
            platform = mainState.platform,
            bookCode = manavaraState.searchQuery,
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
                value = manavaraState.searchQuery,
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
            Spacer(modifier = Modifier.size(16.dp))
        }

        if(manavaraState.searchQuery.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            item {
                ScreenItemSearchBookCodeCard(
                    viewModelDatabase = viewModelDatabase,
                    viewModelMain = viewModelMain
                )
            }
        }
    }
}

@Composable
fun ScreenItemSearchBookCodeCard(
    viewModelDatabase: ViewModelDatabase,
    viewModelMain: ViewModelMain
) {

    val context = LocalContext.current
    val dataBaseState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value

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
                    model = dataBaseState.itemBestDetailInfo.bookImg,
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
                text = dataBaseState.itemBestDetailInfo.title,
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
                    platform = mainState.platform,
                    bookCode = dataBaseState.itemBestDetailInfo.bookCode,
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

        ScreenBestDetailInfo(item = dataBaseState.itemBestDetailInfo)

        Spacer(modifier = Modifier.size(32.dp))
    }
}