package com.bigbigdw.manavara.best.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.screen.ScreenUser
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListKor
import com.bigbigdw.manavara.util.getPlatformColor
import com.bigbigdw.manavara.util.getPlatformDescription
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.novelListKor
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    listState: LazyListState,
    modalSheetState: ModalBottomSheetState? = null,
    needDataUpdate: Boolean,
    viewModelBest: ViewModelBest,
    drawerState: DrawerState,
) {

    val context = LocalContext.current
    val state = viewModelBest.state.collectAsState().value
    val item = state.itemBookInfo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

                if (getDialogOpen) {
                    Dialog(
                        onDismissRequest = { setDialogOpen(false) },
                    ) {
                        AlertTwoBtn(isShow = { setDialogOpen(false) },
                            onFetchClick = {
                                val intent = Intent(context, ActivityBestDetail::class.java)
                                intent.putExtra("BOOKCODE", item.bookCode)
                                intent.putExtra("PLATFORM", item.type)
                                intent.putExtra("TYPE", state.type)
                                context.startActivity(intent)
                            },
                            btnLeft = "취소",
                            btnRight = "작품 보러가기",
                            modifier = Modifier.requiredWidth(400.dp),
                            contents = {
                                ScreenDialogBest(
                                    item = item,
                                    trophy = viewModelBest.state.collectAsState().value.itemBestInfoTrophyList,
                                    isExpandedScreen = isExpandedScreen,
                                    currentRoute = state.type,
                                    modalSheetState = modalSheetState
                                )
                            }
                        )
                    }
                }

                ScreenBestPropertyList(
                    viewModelBest = viewModelBest,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                    drawerState = drawerState,
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenMainBestDetail(
                    listState = listState,
                    setDialogOpen = setDialogOpen,
                    needDataUpdate = needDataUpdate,
                    viewModelBest = viewModelBest,

                    )

                Log.d("RECOMPOSE???", "ScreenBest")

            } else {

                ScreenMainBestItemDetail(
                    modalSheetState = modalSheetState,
                    setDialogOpen = null,
                    needDataUpdate = needDataUpdate,
                    listState = listState,
                    viewModelBest = viewModelBest
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBestPropertyList(
    viewModelBest: ViewModelBest,
    listState: LazyListState,
    isExpandedScreen: Boolean,
    drawerState: DrawerState?,
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelBest.state.collectAsState().value

    if (isExpandedScreen) {
        LazyColumn(
            modifier = Modifier
                .width(330.dp)
                .fillMaxHeight()
                .background(color = colorF6F6F6)
                .padding(8.dp, 0.dp)
                .semantics { contentDescription = "Overview Screen" },
        ) {

            item { Spacer(modifier = Modifier.size(16.dp)) }

            item {
                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                    text = if (state.type == "NOVEL") {
                        "${changePlatformNameKor(state.platform)} 베스트"
                    } else {
                        "웹툰 베스트"
                    },
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(weight = 700)
                )
            }

            item { Spacer(modifier = Modifier.size(16.dp)) }

            item {
                ItemMainSettingSingleTablet(
                    containerColor = color4AD7CF,
                    image = R.drawable.ic_launcher,
                    title = "투데이 베스트",
                    body = "베스트 모드를 투데이로 전환",
                    current = state.bestType,
                    value = "TODAY_BEST",
                    onClick = {
                        coroutineScope.launch {
                            viewModelBest.setBest(bestType = "TODAY_BEST")
                            drawerState?.close()
                        }
                    },
                )
            }

            item {
                ItemMainSettingSingleTablet(
                    containerColor = color5372DE,
                    image = R.drawable.ic_launcher,
                    title = "주간 베스트",
                    body = "베스트 모드를 주간으로 전환",
                    current = state.bestType,
                    value = "WEEK_BEST",
                    onClick = {
                        coroutineScope.launch {
                            viewModelBest.setBest(bestType = "WEEK_BEST")
                            drawerState?.close()
                        }
                    },
                )
            }

            item {
                ItemMainSettingSingleTablet(
                    containerColor = color998DF9,
                    image = R.drawable.ic_launcher,
                    title = "월간 베스트",
                    body = "베스트 모드를 월간으로 전환",
                    current = state.bestType,
                    value = "MONTH_BEST",
                    onClick = {
                        coroutineScope.launch {
                            viewModelBest.setBest(bestType = "MONTH_BEST")
                            drawerState?.close()
                        }
                    },
                )
            }

            item { TabletBorderLine() }

            itemsIndexed(
                if (state.type == "NOVEL") {
                    novelListKor()
                } else {
                    comicListKor()
                }
            ) { _, item ->
                ItemBestListSingle(
                    containerColor = getPlatformColor(item),
                    image = getPlatformLogo(item),
                    title = item,
                    body = getPlatformDescription(item),
                    current = changePlatformNameKor(state.platform),
                    onClick = {
                        coroutineScope.launch {
                            viewModelBest.setBest(platform = changePlatformNameEng(item))
                            listState.scrollToItem(index = 0)
                            drawerState?.close()
                        }
                    })
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .width(330.dp)
                .fillMaxHeight()
                .background(color = colorF6F6F6)
                .padding(8.dp, 0.dp)
                .semantics { contentDescription = "Overview Screen" },
        ) {

            item { Spacer(modifier = Modifier.size(16.dp)) }

            item {
                ItemMainSettingSingleTablet(
                    containerColor = color4AD7CF,
                    image = R.drawable.ic_launcher,
                    title = "유저 옵션",
                    body = "마나바라 유저 옵션",
                    current = state.bestType,
                    value = "USER_OPTION",
                    onClick = {
                        coroutineScope.launch {
                            viewModelBest.setBest(bestType = "USER_OPTION", platform = "USER_OPTION")
                            drawerState?.close()
                        }
                    },
                )
            }

            item { TabletBorderLine() }

            itemsIndexed(
                if (state.type == "NOVEL") {
                    novelListKor()
                } else {
                    comicListKor()
                }
            ) { _, item ->
                ItemBestListSingle(
                    containerColor = getPlatformColor(item),
                    image = getPlatformLogo(item),
                    title = item,
                    body = getPlatformDescription(item),
                    current = changePlatformNameKor(state.platform),
                    onClick = {
                        coroutineScope.launch {

                            if(state.bestType == "USER_OPTION"){
                                viewModelBest.setBest(platform = changePlatformNameEng(item), bestType = "TODAY_BEST")
                            } else {
                                viewModelBest.setBest(platform = changePlatformNameEng(item))
                            }

                            listState.scrollToItem(index = 0)
                            drawerState?.close()
                        }
                    })
            }

        }
    }
}

@Composable
fun ItemBestListSingle(
    containerColor: Color,
    image: Int,
    title: String,
    body: String,
    current: String,
    onClick: () -> Unit,
) {

    Button(colors = ButtonDefaults.buttonColors(
        containerColor = if (current == title) {
            colorE9E9E9
        } else {
            colorF7F7F7
        }
    ), shape = RoundedCornerShape(50.dp), onClick = {
        onClick()
    }, contentPadding = PaddingValues(
        start = 12.dp,
        top = 6.dp,
        end = 12.dp,
        bottom = 6.dp,
    ), content = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.wrapContentSize(),
                backgroundColor = containerColor,
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .height(28.dp)
                            .width(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Row {
                    Text(
                        text = title, fontSize = 16.sp, color = color000000,

                        fontWeight = FontWeight(weight = 500)
                    )
                }

                Row {
                    Text(
                        text = body,
                        fontSize = 14.sp,
                        color = color8E8E8E,
                    )
                }
            }
        }
    })
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenMainBestDetail(
    listState: LazyListState,
    setDialogOpen: (Boolean) -> Unit,
    needDataUpdate: Boolean,
    viewModelBest: ViewModelBest,
) {

    val state = viewModelBest.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if (state.menu.isNotEmpty()) {
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
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                    text = changeDetailNameKor(state.menu),
                    fontSize = 24.sp,
                    color = color000000,
                    fontWeight = FontWeight(weight = 700)
                )
            }
        }

        ScreenMainBestItemDetail(
            modalSheetState = null,
            setDialogOpen = setDialogOpen,
            needDataUpdate = needDataUpdate,
            listState = listState,
            viewModelBest = viewModelBest,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMainBestItemDetail(
    modalSheetState: ModalBottomSheetState? = null,
    setDialogOpen: ((Boolean) -> Unit)?,
    needDataUpdate: Boolean,
    listState: LazyListState,
    viewModelBest: ViewModelBest,
) {

    Log.d("RECOMPOSE???", "ScreenMainBestItemDetail")

    val state = viewModelBest.state.collectAsState().value

    val bestType = remember { derivedStateOf { state.bestType } }


    if (bestType.value.contains("TODAY_BEST")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayBest(
            listState = listState,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            needDataUpdate = needDataUpdate,
            viewModelBest = viewModelBest,
        )

    } else if (bestType.value.contains("WEEK_BEST")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayWeek(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            needDataUpdate = needDataUpdate,
            viewModelBest = viewModelBest
        )

    } else if (bestType.value.contains("MONTH_BEST")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayMonth(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            needDataUpdate = needDataUpdate,
            viewModelBest = viewModelBest
        )

    } else if (bestType.value.contains("USER_OPTION")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenUser()

    }
}

@Composable
fun ScreenBestTopbar(viewModelBest : ViewModelBest, onClick: () -> Unit){
    val state = viewModelBest.state.collectAsState().value

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

                androidx.compose.material.Text(
                    text = if (state.bestType == "USER_OPTION") {
                        "유저 옵션"
                    } else {
                        "${changePlatformNameKor(state.platform)} 베스트"
                    },
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )

            }

        }

        if (state.bestType != "USER_OPTION") {
            androidx.compose.material.Text(
                modifier = Modifier.clickable {
                    viewModelBest.setBest(bestType = "TODAY_BEST")
                },
                text = "투데이",
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                color = if (state.bestType.contains("TODAY_BEST")) {
                    color1E4394
                } else {
                    color555b68
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier
                    .wrapContentWidth()
                    .width(16.dp)
            )

            androidx.compose.material.Text(
                modifier = Modifier.clickable {
                    viewModelBest.setBest(bestType = "WEEK_BEST")
                },
                text = "주간",
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                color = if (state.bestType.contains("WEEK_BEST")) {
                    color1E4394
                } else {
                    color555b68
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier
                    .wrapContentWidth()
                    .width(16.dp)
            )

            androidx.compose.material.Text(
                modifier = Modifier.clickable {
                    viewModelBest.setBest(bestType = "MONTH_BEST")
                },
                text = "월간",
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                color = if (state.bestType.contains("MONTH_BEST")) {
                    color1E4394
                } else {
                    color555b68
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}
