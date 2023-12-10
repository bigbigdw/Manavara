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
import com.bigbigdw.manavara.dataBase.ActivityDataBaseDetail
import com.bigbigdw.manavara.dataBase.getGenreDay
import com.bigbigdw.manavara.dataBase.getJsonFiles
import com.bigbigdw.manavara.dataBase.getJsonGenreMonthList
import com.bigbigdw.manavara.dataBase.getJsonGenreWeekList
import com.bigbigdw.manavara.dataBase.getJsonKeywordList
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelDatabase
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.dataBase.getJsonGenreList
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
import java.util.Collections


@Composable
fun ScreenItemKeywordToday(
    genreList: ArrayList<ItemKeyword> = ArrayList(),
    keywordList: ArrayList<ItemKeyword> = ArrayList(),
    mode: String = "GENRE"
) {

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        if (mode == "KEYWORD") {
            itemsIndexed(keywordList) { index, item ->
                val wordCount = item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                ListGenreToday(
                    title = item.key,
                    value = wordCount.toString(),
                    index = index
                )
            }
        } else {
            itemsIndexed(genreList) { index, item ->
                ListGenreToday(
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
fun ScreenItemKeywordWeek(
    genreList: ArrayList<ItemKeyword> = ArrayList(),
    keywordList: ArrayList<ItemKeyword> = ArrayList(),
    mode: String = "GENRE",
    jsonNameList: List<String>,
    listState: LazyListState,
    week: String,
    viewModelDatabase: ViewModelDatabase
) {

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
                            getter = convertDateStringWeek(item),
                            onClick = {
                                coroutineScope.launch {
                                    viewModelDatabase.setDate(week = item)

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = convertDateStringWeek(item),
                            getValue = convertDateStringWeek(week)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.size(4.dp)) }

        if (mode == "KEYWORD") {
            itemsIndexed(keywordList) { index, item ->
                val wordCount = item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }
                ListGenreToday(
                    title = item.key,
                    value = wordCount.toString(),
                    index = index
                )
            }
        } else {
            itemsIndexed(genreList) { index, item ->
                ListGenreToday(
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
fun ScreenItemKeywordMonth(
    genreList: ArrayList<ItemKeyword> = ArrayList(),
    keywordList: ArrayList<ItemKeyword> = ArrayList(),
    mode: String = "GENRE",
    jsonNameList: List<String>,
    listState: LazyListState,
    month: String,
    viewModelDatabase: ViewModelDatabase
) {

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
                            getter = convertDateStringMonth(item),
                            onClick = {
                                coroutineScope.launch {
                                    viewModelDatabase.setDate(month = item)

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = convertDateStringMonth(item),
                            getValue = convertDateStringMonth(month)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.size(4.dp)) }


        if (mode == "KEYWORD") {
            itemsIndexed(keywordList) { index, item ->
                val wordCount = item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                ListGenreToday(
                    title = item.key,
                    value = wordCount.toString(),
                    index = index
                )
            }
        } else {
            itemsIndexed(genreList) { index, item ->
                ListGenreToday(
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
fun ScreenKeyword(
    menuType: String,
    viewModelDatabase: ViewModelDatabase
) {

    val state = viewModelDatabase.state.collectAsState().value
    val listState = rememberLazyListState()

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month) {
        when (menuType) {
            "투데이" -> {
                getJsonKeywordList(platform = state.platform, type = state.type) {
                    viewModelDatabase.setKeywordDay(it)
                }
            }

            "주간" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "BEST_WEEK",
                ) {
                    viewModelDatabase.setJsonNameList(it)

                    if (state.week.isEmpty()) {
                        viewModelDatabase.setDate(week = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonGenreWeekList(
                        platform = state.platform,
                        type = state.type,
                        root = state.week
                    ) { weekList, list ->
                        viewModelDatabase.setKeywordWeek(keywordDay = list)
                    }
                }

            }

            else -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "BEST_MONTH",
                ) {
                    viewModelDatabase.setJsonNameList(it)

                    if (state.month.isEmpty()) {
                        viewModelDatabase.setDate(month = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonGenreMonthList(
                        platform = state.platform,
                        type = state.type,
                        root = state.month
                    ) { monthList, list ->
                        viewModelDatabase.setKeywordWeek(list)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    val cmpAsc: java.util.Comparator<ItemKeyword> =
        Comparator { o1, o2 ->
            (o2.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                .compareTo(
                    (o1.value.split("\\s+".toRegex()).count { it.isNotEmpty() })
                )
        }
    Collections.sort(state.keywordDay, cmpAsc)

    when (menuType) {
        "투데이" -> {
            ScreenItemKeywordToday(keywordList = state.keywordDay, mode = "KEYWORD")
        }

        "주간" -> {

            ScreenItemKeywordWeek(
                keywordList = state.keywordDay,
                jsonNameList = state.jsonNameList,
                mode = "KEYWORD",
                listState = listState,
                week = state.week,
                viewModelDatabase = viewModelDatabase
            )
        }

        else -> {

            ScreenItemKeywordMonth(
                keywordList = state.keywordDay,
                jsonNameList = state.jsonNameList,
                mode = "KEYWORD",
                listState = listState,
                month = state.month,
                viewModelDatabase = viewModelDatabase
            )
        }
    }
}

@Composable
fun ScreenGenreDetail(
    viewModelDatabase: ViewModelDatabase,
    mode : String
) {

    val state = viewModelDatabase.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month) {
        getJsonFiles(
            platform = state.platform,
            type = state.type,
            root = "BEST_MONTH",
        ) {
            viewModelDatabase.setJsonNameList(it)

            if (state.month.isEmpty()) {
                viewModelDatabase.setDate(month = it.get(0))
            }
        }

    }

    Spacer(modifier = Modifier.size(8.dp))

    LazyColumn(
        modifier = Modifier
            .background(colorF6F6F6)
            .padding(16.dp, 0.dp, 16.dp, 0.dp)
    ) {

        itemsIndexed(state.jsonNameList) { index, item ->

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
                                "${changePlatformNameKor(state.platform)} ${convertDateStringMonth(item)} 장르"
                            } else {
                                "${changePlatformNameKor(state.platform)} ${convertDateStringMonth(item)} 키워드"
                            }
                        )
                        intent.putExtra("JSON", item)
                        intent.putExtra("PLATFORM", state.platform)
                        intent.putExtra("TYPE", state.type)
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
fun ListGenreToday(
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
fun ScreenGenre(
    menuType: String,
    viewModelDatabase: ViewModelDatabase
) {

    val state = viewModelDatabase.state.collectAsState().value
    val listState = rememberLazyListState()

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month) {
        when (menuType) {
            "투데이" -> {
                getJsonGenreList(platform = state.platform, type = state.type) {
                    viewModelDatabase.setGenreList(it)
                }
            }

            "주간" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "BEST_WEEK",
                ) {
                    viewModelDatabase.setJsonNameList(it)

                    if (state.week.isEmpty()) {
                        viewModelDatabase.setDate(week = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonGenreWeekList(
                        platform = state.platform,
                        type = state.type,
                        root = state.week,
                    ) { weekList, list ->
                        viewModelDatabase.setGenreList(list)
                    }
                }

            }

            else -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "BEST_MONTH",
                ) {
                    viewModelDatabase.setJsonNameList(it)

                    if (state.month.isEmpty()) {
                        viewModelDatabase.setDate(month = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonGenreMonthList(
                        platform = state.platform,
                        type = state.type,
                        root = state.month
                    ) { monthList, list ->
                        viewModelDatabase.setGenreList(list)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    when (menuType) {
        "투데이" -> {
            ScreenItemKeywordToday(state.genreList)
        }

        "주간" -> {

            ScreenItemKeywordWeek(
                genreList = state.genreList,
                jsonNameList = state.jsonNameList,
                listState = listState,
                week = state.week,
                viewModelDatabase = viewModelDatabase
            )
        }

        else -> {

            ScreenItemKeywordMonth(
                genreList = state.genreList,
                jsonNameList = state.jsonNameList,
                listState = listState,
                month = state.month,
                viewModelDatabase = viewModelDatabase
            )
        }
    }
}

@Composable
fun GenreDetailJson(
    viewModelDatabase: ViewModelDatabase,
    menuType: String,
    type : String = "GENRE"
) {

    val state = viewModelDatabase.state.collectAsState().value

    LaunchedEffect(menuType, state.platform, state.type) {

        when (menuType) {

            "주간" -> {

                if(type == "GENRE"){
                    getJsonGenreWeekList(
                        platform = state.platform,
                        type = state.type,
                    ) { genreWeekList, genreList ->
                        viewModelDatabase.setGenreWeekList(genreWeekList = genreWeekList, genreList = genreList)
                    }
                } else {
                    getJsonGenreWeekList(
                        platform = state.platform,
                        type = state.type,
                        dataType = "KEYWORD"
                    ) { genreWeekList, genreList ->
                        viewModelDatabase.setGenreWeekList(genreWeekList = genreWeekList, genreList = genreList)
                    }
                }
            }

            else -> {
                if(type == "GENRE"){
                    getJsonGenreMonthList(
                        platform = state.platform,
                        type = state.type,
                    ) { monthList, list ->
                        viewModelDatabase.setGenreWeekList(genreWeekList = monthList, genreList = list)
                    }
                } else {
                    getJsonGenreMonthList(
                        platform = state.platform,
                        type = state.type,
                        dataType = "KEYWORD"
                    ) { monthList, list ->
                        viewModelDatabase.setGenreWeekList(genreWeekList = monthList, genreList = list)
                    }
                }

            }
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    when (menuType) {
        "주간" -> {

            val (getDate, setDate) = remember { mutableStateOf("전체") }

            Column(modifier = Modifier.background(color = colorF6F6F6)) {
                LazyRow(
                    modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
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

                        itemsIndexed(state.genreList) { index, item ->
                            ListGenreToday(
                                title = item.key,
                                value = item.value,
                                index = index
                            )
                        }
                    }
                } else {

                    if(state.genreWeekList[getWeekDate(getDate)].size > 0){
                        LazyColumn(
                            modifier = Modifier
                                .background(colorF6F6F6)
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        ) {

                            itemsIndexed(state.genreWeekList[getWeekDate(getDate)]) { index, item ->
                                ListGenreToday(
                                    title = item.key,
                                    value = if(type == "GENRE"){
                                        item.value
                                    } else{
                                        val wordCount = item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                                        wordCount.toString()
                                    },
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
        else -> {
            val (getDate, setDate) = remember { mutableStateOf("전체") }

            val arrayList = ArrayList<String>()
            arrayList.add("전체")

            var count = 0

            for(item in state.genreWeekList){
                count += 1
                arrayList.add("${count}일")
            }

            Column(modifier = Modifier.background(color = colorF6F6F6)) {
                LazyRow(
                    modifier =  Modifier.padding(16.dp, 8.dp, 0.dp, 8.dp),
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

                        item{
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                onClick = {  },
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

                        item { ItemTabletTitle(str = "${DBDate.month()}월 전체", isTopPadding = false) }

                        itemsIndexed(state.genreList) { index, item ->
                            ListGenreToday(
                                title = item.key,
                                value = item.value,
                                index = index
                            )
                        }

                        itemsIndexed(state.genreWeekList) { index, item ->

                            if(item.size > 0){

                                ItemTabletTitle(str = "${DBDate.month()}월 ${index + 1}일")

                                item.forEachIndexed{ innerIndex, innnerItem ->

                                    if(innerIndex < 5){
                                        ListGenreToday(
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

                    if(state.genreWeekList[getDate.replace("일","").toInt() - 1].size > 0){
                        LazyColumn(
                            modifier = Modifier
                                .background(colorF6F6F6)
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        ) {

                            itemsIndexed(state.genreWeekList[getDate.replace("일","").toInt() - 1]) { index, item ->
                                ListGenreToday(
                                    title = item.key,
                                    value = if(type == "GENRE"){
                                        item.value
                                    } else{
                                        val wordCount = item.value.split("\\s+".toRegex()).count { it.isNotEmpty() }

                                        wordCount.toString()
                                    },
                                    index = index
                                )
                            }

                            item { Spacer(modifier = Modifier.size(60.dp)) }
                        }
                    } else {
                        ScreenEmpty(str = "데이터가 없습니다")
                    }
                }
            }
        }
    }
}