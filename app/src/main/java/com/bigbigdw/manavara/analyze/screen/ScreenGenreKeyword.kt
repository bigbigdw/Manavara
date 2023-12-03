package com.bigbigdw.manavara.analyze.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.bigbigdw.manavara.analyze.ActivityGenreDetail
import com.bigbigdw.manavara.analyze.getGenreDay
import com.bigbigdw.manavara.analyze.getJsonFiles
import com.bigbigdw.manavara.analyze.getJsonGenreMonthList
import com.bigbigdw.manavara.analyze.getJsonGenreWeekList
import com.bigbigdw.manavara.analyze.getJsonKeywordList
import com.bigbigdw.manavara.analyze.getJsonKeywordMonthList
import com.bigbigdw.manavara.analyze.getJsonKeywordWeekList
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.best.models.ItemGenre
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import convertDateStringMonth
import convertDateStringWeek
import kotlinx.coroutines.launch
import java.util.Collections


@Composable
fun ScreenItemGenreToday(
    genreList: ArrayList<ItemGenre> = ArrayList(),
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
                    title = item.title,
                    value = item.value,
                    index = index
                )
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@Composable
fun ScreenItemGenreWeek(
    genreList: ArrayList<ItemGenre> = ArrayList(),
    keywordList: ArrayList<ItemKeyword> = ArrayList(),
    mode: String = "GENRE",
    jsonNameList: List<String>,
    listState: LazyListState,
    week: String,
    viewModelAnalyze: ViewModelAnalyze
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
                                    viewModelAnalyze.setDate(week = item)

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
                    title = item.title,
                    value = item.value,
                    index = index
                )
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@Composable
fun ScreenItemGenreMonth(
    genreList: ArrayList<ItemGenre> = ArrayList(),
    keywordList: ArrayList<ItemKeyword> = ArrayList(),
    mode: String = "GENRE",
    jsonNameList: List<String>,
    listState: LazyListState,
    month: String,
    viewModelAnalyze: ViewModelAnalyze
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
                                    viewModelAnalyze.setDate(month = item)

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
                    title = item.title,
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
    viewModelAnalyze: ViewModelAnalyze
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val listState = rememberLazyListState()

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month) {
        when (menuType) {
            "투데이" -> {
                getJsonKeywordList(platform = state.platform, type = state.type) {
                    viewModelAnalyze.setKeywordDay(it)
                }
            }

            "주간" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "WEEK",
                ) {
                    viewModelAnalyze.setJsonNameList(it)

                    if (state.week.isEmpty()) {
                        viewModelAnalyze.setDate(week = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonKeywordWeekList(
                        platform = state.platform,
                        type = state.type,
                        root = state.week
                    ) { weekList, list ->
                        viewModelAnalyze.setKeywordWeek(keywordDay = list)
                    }
                }

            }

            else -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "MONTH",
                ) {
                    viewModelAnalyze.setJsonNameList(it)

                    if (state.month.isEmpty()) {
                        viewModelAnalyze.setDate(month = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonKeywordMonthList(
                        platform = state.platform,
                        type = state.type,
                        root = state.month
                    ) { monthList, list ->
                        viewModelAnalyze.setKeywordWeek(list)
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
            ScreenItemGenreToday(keywordList = state.keywordDay, mode = "KEYWORD")
        }

        "주간" -> {

            ScreenItemGenreWeek(
                keywordList = state.keywordDay,
                jsonNameList = state.jsonNameList,
                mode = "KEYWORD",
                listState = listState,
                week = state.week,
                viewModelAnalyze = viewModelAnalyze
            )
        }

        else -> {

            ScreenItemGenreMonth(
                keywordList = state.keywordDay,
                jsonNameList = state.jsonNameList,
                mode = "KEYWORD",
                listState = listState,
                month = state.month,
                viewModelAnalyze = viewModelAnalyze
            )
        }
    }
}

@Composable
fun ScreenGenreDetail(
    viewModelAnalyze: ViewModelAnalyze
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month) {
        getJsonFiles(
            platform = state.platform,
            type = state.type,
            root = "MONTH",
        ) {
            viewModelAnalyze.setJsonNameList(it)

            if (state.month.isEmpty()) {
                viewModelAnalyze.setDate(month = it.get(0))
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
                        val intent = Intent(context, ActivityGenreDetail::class.java)
                        intent.putExtra(
                            "TITLE",
                            "${changePlatformNameKor(state.platform)} ${convertDateStringMonth(item)} 장르"
                        )
                        intent.putExtra("JSON", item)
                        intent.putExtra("PLATFORM", state.platform)
                        intent.putExtra("TYPE", state.type)
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
    viewModelAnalyze: ViewModelAnalyze
) {

    val state = viewModelAnalyze.state.collectAsState().value
    val listState = rememberLazyListState()

    LaunchedEffect(state.platform, state.type, state.jsonNameList, state.week, state.month) {
        when (menuType) {
            "투데이" -> {
                getGenreDay(platform = state.platform, type = state.type) {
                    viewModelAnalyze.setGenreList(it)
                }
            }

            "주간" -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "WEEK",
                ) {
                    viewModelAnalyze.setJsonNameList(it)

                    if (state.week.isEmpty()) {
                        viewModelAnalyze.setDate(week = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonGenreWeekList(
                        platform = state.platform,
                        type = state.type,
                        root = state.week
                    ) { weekList, list ->
                        viewModelAnalyze.setGenreList(list)
                    }
                }

            }

            else -> {

                getJsonFiles(
                    platform = state.platform,
                    type = state.type,
                    root = "MONTH",
                ) {
                    viewModelAnalyze.setJsonNameList(it)

                    if (state.month.isEmpty()) {
                        viewModelAnalyze.setDate(month = it.get(0))
                    }
                }

                if (state.jsonNameList.isNotEmpty()) {
                    getJsonGenreMonthList(
                        platform = state.platform,
                        type = state.type,
                        root = state.month
                    ) { monthList, list ->
                        viewModelAnalyze.setGenreList(list)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.size(8.dp))

    when (menuType) {
        "투데이" -> {
            ScreenItemGenreToday(state.genreList)
        }

        "주간" -> {

            ScreenItemGenreWeek(
                genreList = state.genreList,
                jsonNameList = state.jsonNameList,
                listState = listState,
                week = state.week,
                viewModelAnalyze = viewModelAnalyze
            )
        }

        else -> {

            ScreenItemGenreMonth(
                genreList = state.genreList,
                jsonNameList = state.jsonNameList,
                listState = listState,
                month = state.month,
                viewModelAnalyze = viewModelAnalyze
            )
        }
    }
}