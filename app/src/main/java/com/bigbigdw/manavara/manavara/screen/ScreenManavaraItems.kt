package com.bigbigdw.manavara.manavara.screen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
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
import com.bigbigdw.manavara.manavara.editSharePickList
import com.bigbigdw.manavara.manavara.getFCMList
import com.bigbigdw.manavara.manavara.models.ItemAlert
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.room.DBRoom
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletContentWrap
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

    LaunchedEffect(child){
        getFCMList(child = child){
            viewModelManavara.setFcmList(it)
        }
    }

    val state = viewModelManavara.state.collectAsState().value

    LazyColumn {
        item {
            Box(modifier = Modifier.padding(8.dp)){
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
fun ItemTabletFCMList(item : ItemAlert, isLast: Boolean){

    val today = DBDate.dateMMDD()

    val year = item.date.substring(0,4)
    val month = item.date.substring(4,6)
    val day = item.date.substring(6,8)
    val hour = item.date.substring(8,10)
    val min = item.date.substring(10,12)
    val sec = if(item.date.length > 12){
        ":${item.date.substring(12,14)}"
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
                modifier = Modifier.background(color = if(item.date.contains(today)){
                    colorE9E9E9
                } else {
                    Color.Transparent
                }),
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

        if(!isLast){
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colorE9E9E9))
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenMiningStatus(
    viewModelMain : ViewModelMain,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    root: String
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    val manavaraState = viewModelManavara.state.collectAsState().value

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    var roomDao: DBRoom? = null

    LaunchedEffect(viewModelManavara, manavaraState.pickCategory) {

        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)

            roomDao = Room.databaseBuilder(
                context,
                DBRoom::class.java,
                "NOVEL"
            ).allowMainThreadQueries().build()

            val test =  roomDao?.roomdao()?.getAll()
        }


    }

    Scaffold(modifier = Modifier
        .wrapContentSize()
        .background(color = colorF6F6F6),
        floatingActionButton = {

            if(manavaraState.pickCategory.isNotEmpty()){
                FloatingActionButton(
                    modifier = Modifier.size(60.dp),
                    onClick = {

                        if(root == "PICK_SHARE"){
                            editSharePickList(
                                type = "NOVEL",
                                status = "SHARE",
                                listName = manavaraState.platform,
                                pickItemList = manavaraState.pickShareItemList[manavaraState.platform],
                                context = context,
                            )

                            Toast.makeText(context, "${manavaraState.platform}을 성공적으로 가져왔습니다.", Toast.LENGTH_SHORT).show()
                        }

                        if(root == "MY_SHARE"){
                            viewModelMain.setScreen(
                                detail = "웹소설 PICK 공유 리스트 만들기",
                            )
                        }
                    },
                    containerColor = if (root == "PICK_SHARE") {
                        color4AD7CF
                    } else {
                        color5372DE
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = if (root == "PICK_SHARE") {
                            "가져\n오기"
                        } else {
                            "+"
                        },
                        color = Color.White,
                        fontWeight = FontWeight(weight = 700),
                        textAlign = TextAlign.Center,
                        fontSize = if (root == "PICK_SHARE") {
                            14.sp
                        } else {
                            24.sp
                        }
                    )
                }
            }

        }, floatingActionButtonPosition = FabPosition.End) {

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(colorF6F6F6)) {

            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(manavaraState.pickCategory) { index, item ->
                    Box(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelManavara.setView(
                                        platform = item,
                                        type = "NOVEL",
                                        menu = manavaraState.menu,
                                    )

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = changePlatformNameKor(item),
                            getValue = manavaraState.platform
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            LazyColumn(state = listState) {

                if (manavaraState.pickCategory.isEmpty()) {
                    item {
                        ScreenManavaraItemMakeSharePick(
                            viewModelMain = viewModelMain,
                            menu = manavaraState.menu,
                            platform = manavaraState.platform,
                            type = manavaraState.type
                        )
                    }
                } else {

                    val list = manavaraState.pickShareItemList[manavaraState.platform]

                    if (list != null) {
                        itemsIndexed(ArrayList(list)) { index, item ->

                            Box(
                                modifier = Modifier
                                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                                    .wrapContentSize()
                            ) {
                                ScreenBookCard(
                                    mode = "PLATFORM",
                                    item = item,
                                    index = index,
                                ) {

                                    coroutineScope.launch {
                                        getBookItemWeekTrophy(
                                            bookCode = item.bookCode,
                                            type = "NOVEL",
                                            platform = item.platform
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

                        item {
                            Spacer(modifier = Modifier.size(60.dp))
                        }
                    }
                }
            }
        }
    }
}