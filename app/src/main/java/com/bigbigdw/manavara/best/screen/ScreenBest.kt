package com.bigbigdw.manavara.best.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.ActivityBestDetail
import com.bigbigdw.manavara.main.screen.ScreenBestDBListNovel
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.screen.ScreenUser
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.comicListKor
import com.bigbigdw.manavara.util.getPlatformColor
import com.bigbigdw.manavara.util.getPlatformDescription
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.novelListKor
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    setMenu: (String) -> Unit,
    getMenu: String,
    setPlatform: (String) -> Unit,
    getPlatform: String,
    getType: String,
    listState: LazyListState,
    setBestType: (String) -> Unit,
    getBestType: String,
    modalSheetState: ModalBottomSheetState? = null,
) {

    val context = LocalContext.current
    val item = viewModelBest.state.collectAsState().value.itemBookInfo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

                if(getDialogOpen){
                    Dialog(
                        onDismissRequest = { setDialogOpen(false) },
                    ) {
                        AlertTwoBtn(
                            isShow = { setDialogOpen(false) },
                            onFetchClick = {
                                val intent = Intent(context, ActivityBestDetail::class.java)
                                intent.putExtra("BOOKCODE", item.bookCode)
                                intent.putExtra("PLATFORM", item.type)
                                context.startActivity(intent)
                            },
                            btnLeft = "취소",
                            btnRight = "작품 보러가기",
                            modifier = Modifier.requiredWidth(400.dp),
                            contents = {
                                ScreenDialogBest(
                                    item = item,
                                    trophy = viewModelBest.state.collectAsState().value.itemBestInfoTrophyList,
                                    isExpandedScreen = isExpandedScreen
                                )
                            })
                    }
                }

                ScreenBestPropertyList(
                    setMenu = setMenu,
                    getMenu = getMenu,
                    setPlatform = setPlatform,
                    listState = listState,
                    isExpandedScreen = isExpandedScreen,
                    setBestType = setBestType,
                    getBestType = getBestType,
                    getType = getType
                ) {}

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenMainBestDetail(
                    getMenu = getMenu,
                    getType = getType,
                    viewModelBest = viewModelBest,
                    listState = listState,
                    setDialogOpen = setDialogOpen,
                    getBestType = getBestType,
                    getPlatform = getPlatform
                )

            } else {

                ScreenMainBestItemDetail(
                    viewModelBest = viewModelBest,
                    getPlatform = getPlatform,
                    getType = getType,
                    listState = listState,
                    getBestType = getBestType,
                    modalSheetState = modalSheetState,
                    setDialogOpen = null,
                    isExpandedScreen = isExpandedScreen,
                )

            }
        }
    }
}

@Composable
fun ScreenBestPropertyList(
    setMenu: (String) -> Unit,
    getMenu: String,
    setPlatform: (String) -> Unit,
    listState: LazyListState,
    isExpandedScreen: Boolean,
    setBestType: (String) -> Unit,
    getBestType: String,
    getType: String,
    onClick: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    if (isExpandedScreen) {
        Column(
            modifier = Modifier
                .width(330.dp)
                .fillMaxHeight()
                .background(color = colorF6F6F6)
                .padding(8.dp, 0.dp)
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Column {
                Spacer(modifier = Modifier.size(16.dp))

                LaunchedEffect(getBestType){
                    setPlatform("JOARA")
                }

                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                    text = if (getType == "NOVEL") {
                        "웹소설 베스트"
                    } else {
                        "웹툰 베스트"
                    },
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(weight = 700)
                )

                ItemMainSettingSingleTablet(
                    containerColor = color4AD7CF,
                    image = R.drawable.ic_launcher,
                    title = "투데이 베스트",
                    body = "베스트 모드를 투데이로 전환",
                    settter = setBestType,
                    getter = getBestType,
                    value = "TODAY_BEST",
                    onClick = {  },
                )

                ItemMainSettingSingleTablet(
                    containerColor = color5372DE,
                    image = R.drawable.ic_launcher,
                    title = "주간 베스트",
                    body = "베스트 모드를 주간으로 전환",
                    settter = setBestType,
                    getter = getBestType,
                    onClick = { onClick() },
                    value = "WEEK_BEST",
                )

                ItemMainSettingSingleTablet(
                    containerColor = color998DF9,
                    image = R.drawable.ic_launcher,
                    title = "월간 베스트",
                    body = "베스트 모드를 월간으로 전환",
                    settter = setBestType,
                    getter = getBestType,
                    onClick = { onClick() },
                    value = "MONTH_BEST",
                )

                TabletBorderLine()
            }

            if (getType == "NOVEL") {
                LazyColumn {
                    itemsIndexed(novelListKor()) { _, item ->
                        ItemBestListSingle(
                            containerColor = getPlatformColor(item),
                            image = getPlatformLogo(item),
                            title = item,
                            body = getPlatformDescription(item),
                            setMenu = setMenu,
                            getMenu = getMenu,
                            setDetailPlatform = { setPlatform(changePlatformNameEng(item)) }
                        ) {
                            coroutineScope.launch {
                                listState.scrollToItem(index = 0)
                            }
                            onClick()
                        }
                    }
                }
            } else {
                LazyColumn {
                    itemsIndexed(comicListKor()) { _, item ->
                        ItemBestListSingle(
                            containerColor = getPlatformColor(item),
                            image = getPlatformLogo(item),
                            title = item,
                            body = getPlatformDescription(item),
                            setMenu = setMenu,
                            getMenu = getMenu,
                            setDetailPlatform = { setPlatform(changePlatformNameEng(item)) }
                        ) {
                            coroutineScope.launch {
                                listState.scrollToItem(index = 0)
                            }
                            onClick()
                        }
                    }
                }
            }


        }
    } else {
        Column(
            modifier = Modifier
                .width(330.dp)
                .fillMaxHeight()
                .background(color = colorF6F6F6)
                .padding(8.dp, 0.dp)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            ItemMainSettingSingleTablet(
                containerColor = color4AD7CF,
                image = R.drawable.ic_launcher,
                title = "유저 옵션",
                body = "마나바라 유저 옵션",
                settter = setBestType,
                getter = getBestType,
                value = "USER_OPTION",
                onClick = {
                    setMenu("USER_OPTION")
                    onClick()
                },
            )

            TabletBorderLine()

            novelListKor().forEachIndexed { _, item ->
                ItemBestListSingle(
                    containerColor = getPlatformColor(item),
                    image = getPlatformLogo(item),
                    title = item,
                    body = getPlatformDescription(item),
                    setMenu = setMenu,
                    getMenu = getMenu,
                    setDetailPlatform = {
                        setPlatform(changePlatformNameEng(item))

                        if (getBestType == "USER_OPTION") {
                            setBestType("TODAY_BEST")
                        }
                    }
                ) {
                    coroutineScope.launch {
                        listState.scrollToItem(index = 0)
                    }
                    onClick()
                }
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
    setMenu: (String) -> Unit,
    getMenu: String,
    setDetailPlatform: () -> Unit,
    setDetailType: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (getMenu == title) {
                colorE9E9E9
            } else {
                colorF7F7F7
            }
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            setMenu(title)
            setDetailPlatform()
            setDetailType()
        },
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 6.dp,
            end = 12.dp,
            bottom = 6.dp,
        ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
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
                            text = title,
                            fontSize = 16.sp,
                            color = color000000,

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
    getMenu: String,
    getType: String,
    viewModelBest: ViewModelBest,
    listState: LazyListState,
    setDialogOpen: (Boolean) -> Unit,
    getBestType: String,
    getPlatform: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorF6F6F6)
    ) {

        if(getMenu.isNotEmpty()){
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
                    text = changeDetailNameKor(getMenu),
                    fontSize = 24.sp,
                    color = color000000,
                    fontWeight = FontWeight(weight = 700)
                )
            }
        }

        ScreenMainBestItemDetail(
            viewModelBest = viewModelBest,
            getPlatform = getPlatform,
            getType = getType,
            listState = listState,
            getBestType = getBestType,
            modalSheetState = null,
            setDialogOpen = setDialogOpen,
            isExpandedScreen = false
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMainBestItemDetail(
    viewModelBest: ViewModelBest,
    getPlatform: String,
    getType: String,
    listState: LazyListState,
    getBestType: String,
    modalSheetState: ModalBottomSheetState? = null,
    setDialogOpen: ((Boolean) -> Unit)?,
    isExpandedScreen: Boolean,
) {
    if(getBestType.isEmpty() && isExpandedScreen){
        ScreenBestDBListNovel(type = "NOVEL")
    } else if (getBestType.contains("TODAY_BEST")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayBest(
            viewModelBest = viewModelBest,
            listState = listState,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            getType = getType,
            getPlatform = getPlatform,
            getBestType = getBestType
        )

    } else if (getBestType.contains("WEEK_BEST")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayWeek(
            viewModelBest = viewModelBest,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            getType = getType,
            getPlatform = getPlatform,
            getBestType = getBestType
        )

    } else if (getBestType.contains("MONTH_BEST")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenTodayMonth(
            viewModelBest = viewModelBest,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            getType = getType,
            getPlatform = getPlatform,
            getBestType = getBestType
        )

    } else if (getBestType.contains("USER_OPTION")) {

        Spacer(modifier = Modifier.size(16.dp))

        ScreenUser()

    }
}
