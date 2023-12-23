package com.bigbigdw.manavara.manavara.screen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.manavara.getFCMList
import com.bigbigdw.manavara.manavara.models.ItemAlert
import com.bigbigdw.manavara.manavara.setMiningList
import com.bigbigdw.manavara.manavara.setMiningListDelete
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.room.DBBookInfo
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorFF2366
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import postFCMAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {

    val context = LocalContext.current
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    LazyColumn {

        item { ItemTabletTitle(str = "문의 사항 전송") }

        item {
            TabletContentWrap {
                TextField(
                    value = getFCM.title,
                    onValueChange = {
                        setFCM(getFCM.copy(title = it))
                    },
                    label = { Text("푸시 알림 제목", color = color898989) },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0),
                        textColor = color000000
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    value = getFCM.body,
                    onValueChange = {
                        setFCM(getFCM.copy(body = it))
                    },
                    label = { Text("푸시 알림 내용", color = color898989) },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0),
                        textColor = color000000
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                    BtnMobile(
                        func = { postFCMAlert(context = context, getFCM = getFCM) },
                        btnText = "문의사항 등록"
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }

    }
}

@Composable
fun ContentsFCMList(child: String) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    LaunchedEffect(child) {
        getFCMList(child = child) {
            viewModelManavara.setFcmList(it)
        }
    }

    val state = viewModelManavara.state.collectAsState().value

    LazyColumn {
        item {
            Box(modifier = Modifier.padding(8.dp)) {
                TabletContentWrap {
                    Spacer(modifier = Modifier.size(8.dp))

                    state.fcmList.forEachIndexed { index, item ->
                        ItemTabletFCMList(
                            item = item,
                            isLast = state.fcmList.size - 1 == index
                        )
                    }

                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }

        item { Spacer(modifier = Modifier.size(60.dp)) }
    }
}

@Composable
fun ItemTabletFCMList(item: ItemAlert, isLast: Boolean) {

    val today = DBDate.dateMMDD()

    val year = item.date.substring(0, 4)
    val month = item.date.substring(4, 6)
    val day = item.date.substring(6, 8)
    val hour = item.date.substring(8, 10)
    val min = item.date.substring(10, 12)
    val sec = if (item.date.length > 12) {
        ":${item.date.substring(12, 14)}"
    } else {
        ""
    }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.background(
                    color = if (item.date.contains(today)) {
                        colorE9E9E9
                    } else {
                        Color.Transparent
                    }
                ),
                text = "${year}.${month}.${day} ${hour}:${min}${sec}",
                color = color000000,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.body,
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }

        if (!isLast) {
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorE9E9E9)
            )
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenMiningStatus(
    viewModelMain: ViewModelMain,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    type: String = "NOVEL"
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(viewModelManavara) {

        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val roomDao: DBBookInfo?

            roomDao = Room.databaseBuilder(
                context,
                DBBookInfo::class.java,
                type
            ).build()

            viewModelManavara.setItemBookInfoList(
                itemBookInfoList = roomDao.bookInfoDao().getAll()
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .wrapContentSize()
            .background(color = colorF6F6F6),
        floatingActionButton = {

            if (manavaraState.itemBookInfoList?.isEmpty() == false) {

                Column {
                    FloatingActionButton(
                        modifier = Modifier.size(60.dp),
                        onClick = {
                            viewModelMain.setScreen(
                                detail = "웹소설 마이닝 리스트 삭제하기",
                            )
                        },
                        containerColor = colorFF2366,
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "-",
                            color = Color.White,
                            fontWeight = FontWeight(weight = 700),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    FloatingActionButton(
                        modifier = Modifier.size(60.dp),
                        onClick = {
                            viewModelMain.setScreen(
                                menu = "웹소설 분석 현황",
                                detail = "웹소설 마이닝 리스트 만들기",
                            )
                        },
                        containerColor = color998DF9,
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "+",
                            color = Color.White,
                            fontWeight = FontWeight(weight = 700),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp
                        )
                    }
                }
            }

        }, floatingActionButtonPosition = FabPosition.End
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(colorF6F6F6)
        ) {

            LazyColumn(state = listState) {

                if (manavaraState.itemBookInfoList?.isEmpty() == true) {
                    item {
                        ScreenManavaraItemMakeSharePick(
                            viewModelMain = viewModelMain,
                            menu = manavaraState.menu,
                            platform = manavaraState.platform,
                            type = manavaraState.type,
                            comment = "수집하고 있는 작품이 없습니다.\n마이닝할 작품 리스트를 추가해주세요.",
                            mode = "ROOM"
                        )
                    }
                } else {

                    if (manavaraState.itemBookInfoList?.isNotEmpty() == true) {
                        items(manavaraState.itemBookInfoList.size) { index ->

                            Box(
                                modifier = Modifier
                                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                                    .wrapContentSize()
                            ) {
                                ScreenBookCard(
                                    mode = "PLATFORM",
                                    item = manavaraState.itemBookInfoList[index],
                                    index = index,
                                ) {

                                    coroutineScope.launch {
                                        getBookItemWeekTrophy(
                                            bookCode = manavaraState.itemBookInfoList[index].bookCode,
                                            type = "NOVEL",
                                            platform = manavaraState.itemBookInfoList[index].platform
                                        ) { itemBestInfoTrophyList ->

                                            viewModelMain.setItemBestInfoTrophyList(
                                                itemBookInfo = manavaraState.itemBookInfoList[index],
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

                    item {
                        Spacer(modifier = Modifier.size(60.dp))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMiningDelete(
    viewModelMain: ViewModelMain,
    type: String = "NOVEL",
    isExpandedScreen: Boolean
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val manavaraState = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val pickCategory = remember { mutableStateListOf<String>() }

    LaunchedEffect(viewModelManavara) {

        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val roomDao: DBBookInfo?

            roomDao = Room.databaseBuilder(
                context,
                DBBookInfo::class.java,
                type
            ).build()

            viewModelManavara.setItemBookInfoList(
                itemBookInfoList = roomDao.bookInfoDao().getAll()
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .wrapContentSize()
            .background(color = colorF6F6F6),
        bottomBar = {
            if (manavaraState.pickCategory.isNotEmpty() && !isExpandedScreen) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                    onClick = {
                        setMiningListDelete(
                            context = context,
                            type = "NOVEL",
                            pickCategory = pickCategory.toMutableList() as ArrayList<String>,
                            pickItemList = manavaraState.itemBookInfoList,
                        ) {
                            viewModelMain.setScreen(detail = "")
                        }

                        Toast.makeText(context, "마이닝 작품 리스트가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Text(
                        text = "마이닝 작품 삭제하기",
                        textAlign = TextAlign.Center,
                        color = colorEDE6FD,
                        fontSize = 16.sp
                    )
                }
            }
        },
        floatingActionButton = {

            if (manavaraState.itemBookInfoList?.isEmpty() == false && isExpandedScreen) {

                FloatingActionButton(
                    modifier = Modifier.size(60.dp),
                    onClick = {
                        setMiningListDelete(
                            context = context,
                            type = "NOVEL",
                            pickCategory = pickCategory.toMutableList() as ArrayList<String>,
                            pickItemList = manavaraState.itemBookInfoList,
                        ) {
                            viewModelMain.setScreen(detail = "")
                        }

                        Toast.makeText(context, "마이닝 작품 리스트가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    },
                    containerColor = colorFF2366,
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "삭제\n하기",
                        color = Color.White,
                        fontWeight = FontWeight(weight = 700),
                        textAlign = TextAlign.Center,
                    )
                }
            }

        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(colorF6F6F6),
            state = listState
        ) {

            if (manavaraState.itemBookInfoList?.isNotEmpty() == true) {
                items(manavaraState.itemBookInfoList.size) { index ->

                    val item = manavaraState.itemBookInfoList[index]

                    Row(
                        modifier = Modifier
                            .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    ) {
                        Checkbox(
                            checked = pickCategory.contains(item.bookCode),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    pickCategory.add(item.bookCode)
                                } else {
                                    pickCategory.remove(item.bookCode)
                                }

                            })

                        ScreenBookCard(
                            mode = "PLATFORM",
                            item = item,
                            index = index,
                            needIntro = false
                        ) {
                            if (pickCategory.contains(item.bookCode)) {
                                pickCategory.remove(item.bookCode)
                            } else {
                                pickCategory.add(item.bookCode)
                            }

                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.size(60.dp))
            }
        }
    }
}