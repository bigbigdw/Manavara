package com.bigbigdw.manavara.manavara.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.manavara.editSharePickList
import com.bigbigdw.manavara.manavara.getPickList
import com.bigbigdw.manavara.manavara.getPickShareList
import com.bigbigdw.manavara.manavara.getUserPickList
import com.bigbigdw.manavara.manavara.getUserPickShareListALL
import com.bigbigdw.manavara.manavara.setSharePickList
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.AlertOneBtn
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Collections

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMyPick(
    viewModelMain : ViewModelMain,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    root: String,
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(viewModelManavara) {
        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        getPickList(context = context, type = "NOVEL", root = root) { pickCategory, pickItemList ->
            viewModelManavara.setPickList(
                pickCategory = pickCategory,
                pickItemList = pickItemList,
                platform = pickCategory[0]
            )
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorF6F6F6)) {

        LazyRow(
            modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
        ) {
            itemsIndexed(state.pickCategory) { index, item ->
                Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                    ScreenItemKeyword(
                        getter = item,
                        onClick = {
                            coroutineScope.launch {
                                viewModelManavara.setView(
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

        Spacer(modifier = Modifier.size(8.dp))

        LazyColumn {

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
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenPickShare(
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

    LaunchedEffect(viewModelManavara, manavaraState.pickCategory) {

        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        if(root == "PICK_SHARE"){
            getPickShareList(context = context, type = "NOVEL") { pickCategory, pickShareItemList ->
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
        } else {
            getUserPickList(context = context, type = "NOVEL", root = root) { pickCategory, pickShareItemList ->
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

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMakeSharePick(
    viewModelMain : ViewModelMain
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }

    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state = viewModelManavara.state.collectAsState().value
    val listState = rememberLazyListState()
    val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }
    val (getCategoryName, setCategoryName) = remember { mutableStateOf("") }
    val (getPickCategory, setPickCategory) = remember { mutableStateOf(ArrayList<String>()) }

    LaunchedEffect(viewModelManavara) {

        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        getPickList(context = context, type = "NOVEL") { pickCategory, pickItemList ->
            viewModelManavara.setPickList(pickCategory = pickCategory, pickItemList = pickItemList)
        }
    }

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
                        pickCategory = getPickCategory,
                        pickItemList = state.pickItemList,
                        context = context,
                    )

                    viewModelMain.setScreen(detail = "")

                    Toast.makeText(context, "공유리스트 생성이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    setDialogOpen(false)
                },
                modifier = Modifier.requiredWidth(300.dp),
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
        .wrapContentSize()
        .background(color = colorF6F6F6),
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

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(colorF6F6F6)) {

            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(state.pickCategory) { index, item ->
                    Box(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelManavara.setView(
                                        platform = item,
                                        type = "NOVEL",
                                        menu = state.menu,
                                    )

                                    listState.scrollToItem(index = 0)
                                }
                            },
                            title = changePlatformNameKor(item),
                            getValue = state.platform
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            LazyColumn(state = listState) {

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

                val cmpAsc: java.util.Comparator<ItemBookInfo> =
                    Comparator { before, after -> before.title.compareTo(after.title) }
                Collections.sort(filterdList, cmpAsc)

                itemsIndexed(filterdList) { index, item ->
                    Row(
                        modifier = Modifier
                            .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    ) {
                        Checkbox(
                            checked = getPickCategory.contains(item.bookCode),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    getPickCategory.add(item.bookCode)
                                    setPickCategory(getPickCategory)
                                } else {
                                    getPickCategory.remove(item.bookCode)
                                    setPickCategory(getPickCategory)
                                }

                            })

                        ScreenBookCard(
                            mode = "PLATFORM",
                            item = item,
                            index = index,
                            needIntro = false
                        ) {

                            if (getPickCategory.contains(item.bookCode)) {
                                getPickCategory.remove(item.bookCode)
                                setPickCategory(getPickCategory)
                            } else {
                                getPickCategory.add(item.bookCode)
                                setPickCategory(getPickCategory)
                            }

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

@Composable
fun ScreenManavaraItemMakeSharePick(
    viewModelMain: ViewModelMain,
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
                viewModelMain.setScreen(
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenPickShareAll(
    viewModelMain: ViewModelMain,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(viewModelManavara) {

        coroutineScope.launch {
            viewModelManavara.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        val pickCategoryAll =  ArrayList<String>()

        getUserPickShareListALL(context = context, type = "NOVEL") { pickCategory, pickShareItemList ->

            Log.d("ScreenPickShareAll", "ScreenPickShareAll == $pickCategory $pickShareItemList")

            pickCategoryAll.add("전체")
            pickCategoryAll.addAll(pickCategory)

            viewModelManavara.setPickShareList(
                pickCategory = pickCategoryAll,
                pickShareItemList = pickShareItemList,
                platform = if(pickCategoryAll.isEmpty()){
                    ""
                } else {
                    pickCategoryAll[0]
                }
            )
        }
    }


    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(color = colorF6F6F6),
        floatingActionButton = {

            if(state.pickCategory.isNotEmpty() && state.platform != "전체" && state.platform != "내 작품들"){
                FloatingActionButton(
                    modifier = Modifier.size(60.dp),
                    onClick = {

                        val pickCategoryAll =  ArrayList<String>()

                        editSharePickList(
                            type = "NOVEL",
                            status = "DELETE",
                            listName = state.platform,
                            pickItemList = state.pickShareItemList[state.platform],
                            context = context,
                        )

                        getUserPickShareListALL(context = context, type = "NOVEL") { pickCategory, pickShareItemList ->

                            pickCategoryAll.add("전체")
                            pickCategoryAll.addAll(pickCategory)

                            viewModelManavara.setPickShareList(
                                pickCategory = pickCategoryAll,
                                pickShareItemList = pickShareItemList,
                                platform = if(pickCategoryAll.isEmpty()){
                                    ""
                                } else {
                                    pickCategoryAll[0]
                                }
                            )
                        }

                    },
                    containerColor = color998DF9,
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text =  "삭제\n하기",
                        color = Color.White,
                        fontWeight = FontWeight(weight = 700),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }

        }, floatingActionButtonPosition = FabPosition.End) {

        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(color = colorF6F6F6)) {

            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(state.pickCategory) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelManavara.setView(
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

            Spacer(modifier = Modifier.size(8.dp))

            LazyColumn {

                if (state.pickCategory.isEmpty()) {
                    item {
                        ScreenManavaraItemMakeSharePick(
                            viewModelMain = viewModelMain,
                            menu = state.menu,
                            platform = state.platform,
                            type = state.type
                        )
                    }
                } else {

                    val list = if(state.platform == "전체"){

                        val pickShareItemListAll = ArrayList<ItemBookInfo>()
                        val pickShareItemMapAll = mutableMapOf<String, ItemBookInfo>()

                        state.pickShareItemList.forEach { (_, array) ->
                            for(item in array){
                                pickShareItemMapAll[item.bookCode] = item
                            }
                        }

                        for(item in pickShareItemMapAll){
                            pickShareItemListAll.add(item.value)
                        }

                        pickShareItemListAll
                    } else {
                        val arrayList = state.pickShareItemList[state.platform]?.let { it1 -> ArrayList(it1) }
                        arrayList
                    }

                    Log.d("ScreenPickShareAll", "list.size == ${list?.size}")

                    if (list != null) {
                        itemsIndexed(list) { index, item ->

                            Box(
                                modifier = Modifier
                                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                                    .wrapContentSize()
                            ) {
                                ScreenBookCard(
                                    mode = "PLATFORM",
                                    item = item,
                                    index = index,
                                    boxColor = when (item.belong) {
                                        "SHARE" -> {
                                            Color(0xFFECFCFB)
                                        }
                                        "DOWNLOADED" -> {
                                            Color(0xFFE9EEFD)
                                        }
                                        else -> {
                                            Color(0xFFFFFFFF)
                                        }
                                    }
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