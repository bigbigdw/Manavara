package com.bigbigdw.manavara.manavara.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.dataBase.screen.ScreenChoosePlatformFab
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.manavara.editSharePickList
import com.bigbigdw.manavara.manavara.getPickList
import com.bigbigdw.manavara.manavara.getUserPickShareList
import com.bigbigdw.manavara.manavara.setSharePickList
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.AlertOneBtn
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletContentWrap
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMyPick(
    viewModelManavara: ViewModelManavara,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    root: String,
) {

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModelManavara) {

        getPickList(type = "NOVEL", root = root) { pickCategory, pickItemList ->
            viewModelManavara.setPickList(
                pickCategory = pickCategory,
                pickItemList = pickItemList,
                platform = pickCategory[0]
            )
        }
    }

    LazyColumn {
        item {
            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(state.pickCategory) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelManavara.setScreen(
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

        if (state.pickItemList.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            var filterdList = ArrayList<ItemBookInfo>()

            if (state.platform == "전체") {
                filterdList = state.pickItemList

            } else {
                for (item in state.pickItemList) {
                    if (item.platform == state.platform) {
                        filterdList.add(item)
                    }
                }
            }

            itemsIndexed(filterdList) { index, item ->

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

                                viewModelManavara.setItemBestInfoTrophyList(
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenPickShare(
    viewModelManavara: ViewModelManavara,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    root: String
) {

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModelManavara, state.pickCategory) {
        getUserPickShareList(type = "NOVEL", root = root) { pickCategory, pickShareItemList ->
            viewModelManavara.setPickShareList(
                pickCategory = pickCategory,
                pickShareItemList = pickShareItemList,
                platform = if(pickCategory.isEmpty()){
                    ""
                } else {
                    pickCategory[0]
                }
            )
        }
    }

    Scaffold(modifier = Modifier.wrapContentSize(),
        floatingActionButton = {

            if(state.pickCategory.isNotEmpty()){
                FloatingActionButton(
                    modifier = Modifier.size(60.dp),
                    onClick = {

                        if(root == "PICK_SHARE"){
                            editSharePickList(
                                type = "NOVEL",
                                status = if (root == "PICK_SHARE" || state.pickCategory.isEmpty()) {
                                    "SHARE"
                                } else {
                                    "DELETE"
                                },
                                listName = state.platform,
                                pickItemList = state.pickShareItemList[state.platform]
                            )
                        }

                        if(root == "MY_SHARE"){
                            viewModelManavara.setScreen(
                                detail = "웹소설 PICK 공유 리스트 만들기",
                            )

//                            getUserPickShareList(type = "NOVEL", root = root) { pickCategory, pickShareItemList ->
//                                viewModelManavara.setPickShareList(
//                                    pickCategory = pickCategory,
//                                    pickShareItemList = pickShareItemList,
//                                    platform = if(pickCategory.isEmpty()){
//                                        ""
//                                    } else {
//                                        pickCategory[0]
//                                    }
//                                )
//                            }
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
//                            "삭제\n하기"
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

        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Spacer(modifier = Modifier.size(8.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
                ) {
                    itemsIndexed(state.pickCategory) { index, item ->
                        Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                            ScreenItemKeyword(
                                getter = item,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModelManavara.setScreen(
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

            if (state.pickCategory.isEmpty()) {
                item {
                    ScreenManavaraItemMakeSharePick(
                        viewModelManavara = viewModelManavara,
                        menu = state.menu,
                        platform = state.platform,
                        type = state.type
                    )
                }
            } else {

                val list = state.pickShareItemList[state.platform]

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

                                        viewModelManavara.setItemBestInfoTrophyList(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMakeSharePick(
    viewModelManavara: ViewModelManavara
) {

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val itemList = state.pickCategory
    val context = LocalContext.current

    LaunchedEffect(viewModelManavara) {
        getPickList(type = "NOVEL") { _, pickItemList ->
            viewModelManavara.setPickList(pickCategory = ArrayList(), pickItemList = pickItemList)
        }
    }

    val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }
    val (getCategoryName, setCategoryName) = remember { mutableStateOf("") }

    if (getDialogOpen) {
        Dialog(
            onDismissRequest = { setDialogOpen(false) },
        ) {
            AlertOneBtn(
                isShow = {
                    setSharePickList(
                        type = "NOVEL",
                        initTitle = { setCategoryName("") },
                        listName = getCategoryName,
                        pickCategory = state.pickCategory,
                        pickItemList = state.pickItemList
                    )

                    viewModelManavara.setScreen(detail = "")

                    Toast.makeText(context, "공유리스트 생성이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    setDialogOpen(false)
                },
                modifier = Modifier.Companion.requiredWidth(300.dp),
                btnText = "리스트 만들기",
                contents = {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "공유 리스트 이름입력",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    TextField(
                        value = getCategoryName,
                        onValueChange = { setCategoryName(it) },
                        label = { Text("리스트 이름을 입력해 주십시오", color = color898989) },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0),
                            textColor = color000000
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp)
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                }
            )
        }
    }

    Scaffold(modifier = Modifier
        .wrapContentSize(),
        bottomBar = {
            if (state.pickCategory.isNotEmpty()) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                    onClick = {
                        setDialogOpen(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Text(
                        text = "공유 리스트 생성하기",
                        textAlign = TextAlign.Center,
                        color = colorEDE6FD,
                        fontSize = 16.sp
                    )
                }
            }
        }) {

        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Spacer(modifier = Modifier.size(8.dp))
            }

            itemsIndexed(state.pickItemList) { index, item ->
                Row(
                    modifier = Modifier
                        .padding(16.dp, 8.dp, 16.dp, 8.dp)
                ) {
                    Checkbox(
                        checked = itemList.contains(item.bookCode),
                        onCheckedChange = { isChecked ->
                            val updatedItemList = if (isChecked) {
                                itemList + item.bookCode
                            } else {
                                itemList - item.bookCode
                            }

                            viewModelManavara.setPickItems(updatedItemList as ArrayList<String>)
                        })

                    ScreenBookCard(
                        mode = "PLATFORM",
                        item = item,
                        index = index,
                        needIntro = false
                    ) {

                        val updatedItemList = if (itemList.contains(item.bookCode)) {
                            itemList - item.bookCode
                        } else {
                            itemList + item.bookCode
                        }

                        viewModelManavara.setPickItems(updatedItemList as ArrayList<String>)

                        coroutineScope.launch {
                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                type = "NOVEL",
                                platform = item.platform
                            ) { itemBestInfoTrophyList ->

                                viewModelManavara.setItemBestInfoTrophyList(
                                    itemBookInfo = item,
                                    itemBestInfoTrophyList = itemBestInfoTrophyList
                                )
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

@Composable
fun ScreenManavaraItemMakeSharePick(
    viewModelManavara: ViewModelManavara,
    menu: String,
    platform: String,
    type: String,
    comment: String = "데이터가 없습니다.\n공유하실 작품 리스트를 추가해주세요."
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        ScreenEmpty(str = comment)

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = color20459E),
            onClick = {
                viewModelManavara.setScreen(
                    menu = menu,
                    platform = platform,
                    detail = "웹소설 PICK 공유 리스트 만들기",
                    type = type
                )
            },
            modifier = Modifier
                .width(260.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50.dp)

        ) {
            Text(
                text = "공유 PICK 리스트 추가하기",
                textAlign = TextAlign.Center,
                color = colorEDE6FD,
                fontSize = 16.sp
            )
        }
    }
}