package com.bigbigdw.manavara.manavara.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.best.screen.BestBottomDialog
import com.bigbigdw.manavara.best.screen.BestDialog
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.menuListManavara
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ScreenMenuItem
import com.bigbigdw.manavara.util.screen.ScreenTopbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenManavara(
    isExpandedScreen: Boolean,
) {

    val context = LocalContext.current
    val viewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelManavara: ViewModelManavara = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )


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
                        itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                        item = state.itemBookInfo,
                        isExpandedScreen = isExpandedScreen
                    )
                }

                ScreenManavaraPropertyList(
                    viewModelManavara = viewModelManavara,
                    drawerState = drawerState
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenManavaraItem(
                    viewModelManavara = viewModelManavara,
                    modalSheetState = modalSheetState,
                    setDialogOpen = setDialogOpen
                )


            } else {

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenManavaraPropertyList(
                        viewModelManavara = viewModelManavara,
                        drawerState = drawerState
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenTopbar(detail = state.detail, menu = state.menu) {
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

                            if(state.detail.isEmpty()){
                                ScreenManavaraItem(
                                    viewModelManavara = viewModelManavara,
                                    modalSheetState = modalSheetState,
                                    setDialogOpen = null
                                )
                            } else {
                                ScreenManavaraItemDetail(
                                    viewModelManavara = viewModelManavara,
                                    modalSheetState = modalSheetState,
                                    setDialogOpen = null
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
                                    itemBestInfoTrophyList = state.itemBestInfoTrophyList,
                                    item = state.itemBookInfo,
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
    viewModelManavara: ViewModelManavara,
    drawerState: DrawerState?,
) {

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

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
                        viewModelManavara.setScreen(
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
    viewModelManavara: ViewModelManavara,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelManavara.state.collectAsState().value

    if (state.menu.contains("유저 옵션")) {
        ScreenUser()
    } else if (state.menu.contains("PICK 보기")) {

        if(state.menu.contains("다른")){
            ScreenPickShare(
                viewModelManavara = viewModelManavara,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen,
                root = "PICK_SHARE"
            )
        } else {
            ScreenMyPick(
                viewModelManavara = viewModelManavara,
                modalSheetState = modalSheetState,
                setDialogOpen = setDialogOpen,
                root = "MY"
            )
        }
    } else if (state.menu.contains("만들기")) {
        ScreenMakeSharePick(
            viewModelManavara = viewModelManavara
        )
    } else if (state.menu.contains("공유")) {
        ScreenPickShare(
            viewModelManavara = viewModelManavara,
            modalSheetState = modalSheetState,
            setDialogOpen = setDialogOpen,
            root = "SHARE"
        )
    } else if (state.menu.contains("PICK 작품들 보기")) {
        ScreenUser()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenManavaraItemDetail(
    viewModelManavara: ViewModelManavara,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelManavara.state.collectAsState().value

    if (state.detail.contains("공유 리스트 만들기")) {
        ScreenMakeSharePick(
            viewModelManavara = viewModelManavara
        )
    }
}


