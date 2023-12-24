package com.bigbigdw.manavara.dataBase.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.dataBase.ActivityDataBaseDetail
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.best.screen.ListBestToday
import com.bigbigdw.manavara.dataBase.getGenreListWeekJson
import com.bigbigdw.manavara.dataBase.setBookNewInfo
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.getWeekDate
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.weekListAll
import convertDateStringMonth
import convertDateStringWeek
import kotlinx.coroutines.launch


@Composable
fun ScreenItemKeywordToday(
    genreKeywordList: ArrayList<ItemKeyword> = ArrayList()
) {

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        itemsIndexed(genreKeywordList) { index, item ->

            ListGenreKeywordToday(
                title = item.key,
                value = item.value,
                index = index
            )
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@Composable
fun ScreenItemKeywordWeek(
    genreList: ArrayList<ItemKeyword> = ArrayList(),
    keywordList: ArrayList<ItemKeyword> = ArrayList(),
    mode: String = "GENRE",
    dateType: String = "WEEK",
    jsonNameList: List<String>,
    listState: LazyListState,
    date: String
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 8.dp),
            ) {
                itemsIndexed(jsonNameList) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = if (dateType == "WEEK") {
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
                            title = if (dateType == "WEEK") {
                                convertDateStringWeek(item)
                            } else {
                                convertDateStringMonth(item)
                            },
                            getValue = if (dateType == "WEEK") {
                                convertDateStringWeek(date)
                            } else {
                                convertDateStringMonth(date)
                            },
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.size(4.dp)) }

        if (mode == "KEYWORD") {
            itemsIndexed(keywordList) { index, item ->

                ListGenreKeywordToday(
                    title = item.key,
                    value = item.value,
                    index = index
                )
            }
        } else {
            itemsIndexed(genreList) { index, item ->
                ListGenreKeywordToday(
                    title = item.key,
                    value = item.value,
                    index = index
                )
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@Composable
fun ScreenGenreDetail(
    mode: String,
    viewModelMain: ViewModelMain
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(mainState.platform, mainState.type) {
        getJsonFiles(
            platform = mainState.platform,
            type = mainState.type,
            root = "BEST_MONTH",
        ) { jsonNameList ->

            if (manavaraState.date.isEmpty()) {
                viewModelDatabase.setDate(date = jsonNameList[0], jsonNameList = jsonNameList)
            } else {
                viewModelDatabase.setJsonNameList(jsonNameList)
            }
        }

    }

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        itemsIndexed(manavaraState.jsonNameList) { index, item ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp, 0.dp, 0.dp, 0.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    onClick = {
                        val intent = Intent(context, ActivityDataBaseDetail::class.java)
                        intent.putExtra(
                            "TITLE",
                            if (mode.contains("GENRE")) {
                                "${changePlatformNameKor(mainState.platform)} ${
                                    convertDateStringMonth(
                                        item
                                    )
                                } 장르"
                            } else {
                                "${changePlatformNameKor(mainState.platform)} ${
                                    convertDateStringMonth(
                                        item
                                    )
                                } 키워드"
                            }
                        )
                        intent.putExtra("JSON", item)
                        intent.putExtra("PLATFORM", mainState.platform)
                        intent.putExtra("TYPE", mainState.type)
                        intent.putExtra("MODE", mode)
                        context.startActivity(intent)
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
                                text = convertDateStringMonth(item),
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(1f),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Left,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    })
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }

}


@Composable
fun ListGenreKeywordToday(
    title: String,
    value: String,
    index: Int,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = {},
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
                        text = title,
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

                    Text(
                        text = value,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        color = color1CE3EE,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            })
    }
}

@Composable
fun ScreenGenreKeyword(
    menuType: String,
    dataType: String,
    viewModelMain: ViewModelMain
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(menuType, mainState.platform, mainState.type, manavaraState.date) {
        getJsonFiles(
            platform = mainState.platform,
            type = mainState.type,
            root = if (menuType == "주간") {
                "BEST_WEEK"
            } else {
                "BEST_MONTH"
            },
        ) { jsonNameList ->

            getGenreListWeekJson(
                platform = mainState.platform,
                type = mainState.type,
                root = manavaraState.date.ifEmpty {
                    jsonNameList[0]
                },
                dayType = if (menuType == "주간") {
                    "WEEK"
                } else {
                    "MONTH"
                },
                dataType = dataType,
                context = context
            ) { weekList, list ->

                viewModelDatabase.setGenreKeywordWeekList(
                    genreWeekList = weekList,
                    genreKeywordList = list,
                    jsonNameList = jsonNameList,
                    date = manavaraState.date.ifEmpty {
                        jsonNameList[0]
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    if(manavaraState.jsonNameList.isNotEmpty()){
        ScreenItemKeywordWeek(
            genreList = manavaraState.genreKeywordList,
            dateType = if (menuType == "주간") {
                "WEEK"
            } else {
                "MONTH"
            },
            jsonNameList = manavaraState.jsonNameList,
            listState = listState,
            date = manavaraState.date.ifEmpty {
                manavaraState.jsonNameList[0]
            }
        )
    }
}

@Composable
fun GenreDetailJson(
    menuType: String,
    type: String = "GENRE",
    viewModelMain: ViewModelMain
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val dataBaseState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(menuType, dataBaseState.platform, dataBaseState.type) {
        getGenreListWeekJson(
            context = context,
            platform = dataBaseState.platform.ifEmpty { mainState.platform },
            type = dataBaseState.type.ifEmpty { mainState.type },
            dayType = if (menuType == "주간") {
                "WEEK"
            } else {
                "MONTH"
            },
            dataType = type,
        ) { genreWeekList, genreList ->
            viewModelDatabase.setGenreKeywordWeekList(
                genreWeekList = genreWeekList,
                genreKeywordList = genreList
            )
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    when (menuType) {
        "주간" -> {

            val (getDate, setDate) = remember { mutableStateOf("전체") }

            Column(modifier = Modifier.background(color = colorF6F6F6)) {
                LazyRow(
                    modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
                ) {

                    itemsIndexed(weekListAll()) { index, item ->
                        Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                            ScreenItemKeyword(
                                getter = getDate,
                                onClick = {
                                    setDate(item)
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

                        itemsIndexed(dataBaseState.genreKeywordList) { index, item ->
                            ListGenreKeywordToday(
                                title = item.key,
                                value = if (type == "GENRE") {
                                    item.value
                                } else {
                                    val wordCount = item.value.split("\\s+".toRegex())
                                        .count { it.isNotEmpty() }
                                    wordCount.toString()
                                },
                                index = index
                            )
                        }
                    }
                } else {

                    if (dataBaseState.genreKeywordWeekList[getWeekDate(getDate)].size > 0) {
                        LazyColumn(
                            modifier = Modifier
                                .background(colorF6F6F6)
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        ) {

                            itemsIndexed(dataBaseState.genreKeywordWeekList[getWeekDate(getDate)]) { index, item ->
                                ListGenreKeywordToday(
                                    title = item.key,
                                    value = if (type == "GENRE") {
                                        item.value
                                    } else {
                                        val wordCount = item.value.split("\\s+".toRegex())
                                            .count { it.isNotEmpty() }
                                        wordCount.toString()
                                    },
                                    index = index
                                )
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

        else -> {
            val (getDate, setDate) = remember { mutableStateOf("전체") }

            val arrayList = ArrayList<String>()
            arrayList.add("전체")

            var count = 0

            for (item in dataBaseState.genreKeywordWeekList) {
                count += 1
                arrayList.add("${count}일")
            }

            Column(modifier = Modifier.background(color = colorF6F6F6)) {
                LazyRow(
                    modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
                ) {
                    itemsIndexed(arrayList) { index, item ->
                        Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                            ScreenItemKeyword(
                                getter = getDate,
                                onClick = {
                                    setDate(item)
                                },
                                title = item,
                                getValue = item
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.size(60.dp)) }
                }

                if (getDate == "전체") {
                    LazyColumn(
                        modifier = Modifier
                            .background(colorF6F6F6)
                            .padding(16.dp, 0.dp, 16.dp, 0.dp)
                    ) {

                        item {
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                content = {

                                    Column {

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "전체 리스트는 장르 누적 순위로 표시됩니다.",
                                            color = color8E8E8E,
                                            fontSize = 16.sp,
                                        )

                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "각 일별 순위는 1위부터 5위까지 표시됩니다. 일별 탭에서 모든 순위를 확인 할 수 있습니다.",
                                            color = color8E8E8E,
                                            fontSize = 16.sp,
                                        )

                                    }
                                }
                            )
                        }

                        item {
                            ItemTabletTitle(
                                str = "${DBDate.month()}월 전체",
                                isTopPadding = false
                            )
                        }

                        itemsIndexed(dataBaseState.genreKeywordList) { index, item ->
                            ListGenreKeywordToday(
                                title = item.key,
                                value = item.value,
                                index = index
                            )
                        }

                        itemsIndexed(dataBaseState.genreKeywordWeekList) { index, item ->

                            if (item.size > 0) {

                                ItemTabletTitle(str = "${DBDate.month()}월 ${index + 1}일")

                                item.forEachIndexed { innerIndex, innnerItem ->

                                    if (innerIndex < 5) {
                                        ListGenreKeywordToday(
                                            title = innnerItem.key,
                                            value = innnerItem.value,
                                            index = index
                                        )
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.size(60.dp)) }
                    }
                } else {

                    if (dataBaseState.genreKeywordWeekList[getDate.replace("일", "")
                            .toInt() - 1].size > 0
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .background(colorF6F6F6)
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        ) {

                            itemsIndexed(
                                dataBaseState.genreKeywordWeekList[getDate.replace("일", "")
                                    .toInt() - 1]
                            ) { index, item ->
                                ListGenreKeywordToday(
                                    title = item.key,
                                    value = if (type == "GENRE") {
                                        item.value
                                    } else {
                                        val wordCount = item.value.split("\\s+".toRegex())
                                            .count { it.isNotEmpty() }

                                        wordCount.toString()
                                    },
                                    index = index
                                )
                            }

                            item { Spacer(modifier = Modifier.size(60.dp)) }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ScreenEmpty(str = "데이터가 없습니다")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBookList(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    list: List<String>,
    viewModelMain: ViewModelMain
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelDatabase: ViewModelDatabase = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val dataBaseState = viewModelDatabase.state.collectAsState().value
    val mainState = viewModelMain.state.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(dataBaseState.platform, dataBaseState.type) {
        setBookNewInfo(
            platform = dataBaseState.platform.ifEmpty { mainState.platform },
            context = context
        ) {
            viewModelDatabase.setFilteredList(filteredList = it)
        }
    }

    Column(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
            .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        LazyRow(
            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
        ) {
            itemsIndexed(list) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = item,
                        onClick = {
                            coroutineScope.launch {
                                viewModelDatabase.setView(
                                    platform = item,
                                )
                            }
                        },
                        title = changePlatformNameKor(item),
                        getValue = dataBaseState.platform
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(4.dp))

        LazyColumn(
            modifier = Modifier
                .background(colorF6F6F6)
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .fillMaxSize(),
        ) {

            itemsIndexed(dataBaseState.filteredList) { index, item ->
                ListBestToday(
                    itemBookInfo = item,
                    index = index,
                ) {

                    getBookItemWeekTrophy(
                        bookCode = item.bookCode,
                        platform = mainState.platform,
                        type = mainState.type
                    ) {
                        viewModelMain.setItemBestInfoTrophyList(
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
        }

        Spacer(modifier = Modifier.size(60.dp))
    }
}