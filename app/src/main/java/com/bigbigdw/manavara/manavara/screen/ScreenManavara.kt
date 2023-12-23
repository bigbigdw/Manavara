package com.bigbigdw.manavara.manavara.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.screen.BestBottomDialog
import com.bigbigdw.manavara.best.screen.BestDialog
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.menuListManavara
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ScreenMenuItem
import com.bigbigdw.manavara.util.screen.ScreenTopbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenManavara(
    isExpandedScreen: Boolean,
) {

    val context = LocalContext.current
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }

    val viewModelMain: ViewModelMain = viewModel(viewModelStoreOwner = viewModelStoreOwner)

    val mainState = viewModelMain.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    LaunchedEffect(viewModelMain){
        coroutineScope.launch {
            viewModelMain.sideEffects.onEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }.launchIn(coroutineScope)
        }

        viewModelMain.setScreen(
            menu = "웹소설 PICK 작품들 보기",
            platform = "전체",
            detail = "",
            type = "NOVEL"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
    ) {

        Row {
            if (isExpandedScreen) {

                val (getDialogOpen, setDialogOpen) = remember { mutableStateOf(false) }

                if(getDialogOpen){
                    BestDialog(
                        onDismissRequest = { setDialogOpen(false) },
                        itemBestInfoTrophyList = mainState.itemBestInfoTrophyList,
                        item = mainState.itemBookInfo,
                        isExpandedScreen = isExpandedScreen,
                        type = mainState.type
                    )
                }

                ScreenManavaraPropertyList(
                    viewModelMain = viewModelMain,
                    drawerState = drawerState
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                Column {
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
                            text = mainState.menu,
                            fontSize = 24.sp,
                            color = color000000,
                            fontWeight = FontWeight(weight = 700)
                        )
                    }

                    if(mainState.detail.isEmpty()){
                        ScreenManavaraItem(
                            modalSheetState = modalSheetState,
                            setDialogOpen = null,
                            viewModelMain = viewModelMain,
                            isExpandedScreen = isExpandedScreen
                        )
                    } else {
                        ScreenManavaraItemDetail(
                            viewModelMain = viewModelMain,
                            isExpandedScreen = isExpandedScreen
                        )
                    }
                }
            } else {

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenManavaraPropertyList(
                        drawerState = drawerState,
                        viewModelMain = viewModelMain
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenTopbar(detail = mainState.detail, menu = mainState.menu) {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        },

                        ) {
                        Box(
                            Modifier
                                .padding(it)
                                .background(color = colorF6F6F6)
                                .fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.size(8.dp))

                            if(mainState.detail.isEmpty()){
                                ScreenManavaraItem(
                                    modalSheetState = modalSheetState,
                                    setDialogOpen = null,
                                    viewModelMain = viewModelMain,
                                    isExpandedScreen = isExpandedScreen
                                )
                            } else {
                                ScreenManavaraItemDetail(
                                    viewModelMain = viewModelMain,
                                    isExpandedScreen = isExpandedScreen
                                )
                            }
                        }
                    }

                    if(modalSheetState.isVisible){
                        ModalBottomSheetLayout(
                            sheetState = modalSheetState,
                            sheetElevation = 50.dp,
                            sheetShape = RoundedCornerShape(
                                topStart = 25.dp,
                                topEnd = 25.dp
                            ),
                            sheetContent = {

                                Spacer(modifier = Modifier.size(4.dp))

                                BestBottomDialog(
                                    itemBestInfoTrophyList = mainState.itemBestInfoTrophyList,
                                    item = mainState.itemBookInfo,
                                    isExpandedScreen = isExpandedScreen,
                                    modalSheetState = modalSheetState,
                                    currentRoute = "NOVEL",
                                )
                            },
                        ) {}
                    }
                }

                BackOnPressedMobile(modalSheetState = modalSheetState)

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManavaraPropertyList(
    drawerState: DrawerState?,
    viewModelMain: ViewModelMain,
) {

    val coroutineScope = rememberCoroutineScope()
    val state = viewModelMain.state.collectAsState().value

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

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "마나바라 스페셜",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        menuListManavara.forEachIndexed { index, item ->
            ScreenMenuItem(
                item = item,
                index = index,
                current = state.menu,
                onClick ={
                    coroutineScope.launch {
                        viewModelMain.setScreen(
                            menu = item.menu,
                            detail = "",
                            type = state.type
                        )
                        drawerState?.close()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenManavaraItem(
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?,
    isExpandedScreen : Boolean,
    viewModelMain: ViewModelMain
) {

    val state = viewModelMain.state.collectAsState().value

    if (state.menu.contains("유저 옵션")) {
        ScreenUser()
    } else if (state.menu.contains("메세지함") || state.menu.contains("최신화 현황판")) {
        ContentsFCMList(
            child = if(state.menu.contains("메세지함")){
                "NOTICE"
            } else {
                "ALERT"
            }
        )
    } else if (state.menu.contains("PICK 보기")) {

        if(state.menu.contains("공유된")){
            ScreenPickShare(
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen,
                root = "PICK_SHARE",
                viewModelMain = viewModelMain
            )
        } else {
            ScreenMyPick(
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen,
                root = "MY",
                viewModelMain = viewModelMain
            )
        }
    } else if (state.menu.contains("만들기")) {
        ScreenMakeSharePick(
            viewModelMain = viewModelMain,
            mode = "SHARE",
            isExpandedScreen = isExpandedScreen
        )
    } else if (state.menu.contains("공유")) {
        ScreenPickShare(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            root = "MY_SHARE",
            viewModelMain = viewModelMain
        )
    } else if (state.menu.contains("PICK 작품들 보기")) {
        ScreenPickShareAll(
            viewModelMain = viewModelMain,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
        )
    } else if (state.menu.contains("분석 현황")) {
        ScreenMiningStatus(
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            type = "NOVEL",
            viewModelMain = viewModelMain
        )
    }
}

@Composable
fun ScreenManavaraItemDetail(
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {

    val state = viewModelMain.state.collectAsState().value

    if(state.detail.contains("리스트 만들기")){
        ScreenMakeSharePick(
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen,
            mode = if(state.detail.contains("공유 리스트 만들기")){
                "SHARE"
            } else {
                "MINING"
            }
        )
    } else if(state.detail.contains("리스트 삭제하기")){
        ScreenMiningDelete(
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    }
    

}


