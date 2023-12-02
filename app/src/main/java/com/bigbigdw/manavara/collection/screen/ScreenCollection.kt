package com.bigbigdw.manavara.collection.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.analyze.screen.ScreenManavaDetail
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraItemDetail
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraPropertyList
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraTopbar
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenCollection(
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState? = null,
    drawerState: DrawerState,
    viewModelAnalyze: ViewModelAnalyze,
    currentRoute: String?,
) {

    val context = LocalContext.current

    val state = viewModelAnalyze.state.collectAsState().value

    LaunchedEffect(state.platform, state.type) {
        viewModelAnalyze.getBestListTodayStorage(
            context = context,
        )

        viewModelAnalyze.getBestWeekTrophy()
        viewModelAnalyze.getBestWeekListStorage(context)
        viewModelAnalyze.getBestMonthTrophy()
        viewModelAnalyze.getBestMonthListStorage(context)
    }

    val coroutineScope = rememberCoroutineScope()

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
                            isShow = {  },
                            onFetchClick = { },
                            btnLeft = "취소",
                            btnRight = "확인",
                            modifier = Modifier.requiredWidth(400.dp),
                            contents = {
                                if (modalSheetState != null) {
                                    ScreenDialogBest(
                                        item = viewModelAnalyze.state.collectAsState().value.itemBookInfo,
                                        trophy = viewModelAnalyze.state.collectAsState().value.itemBestInfoTrophyList,
                                        isExpandedScreen = isExpandedScreen,
                                        currentRoute = "NOVEL",
                                        modalSheetState = modalSheetState
                                    )
                                }
                            })
                    }
                }

                ScreenManavaraPropertyList(
                    viewModelAnalyze = viewModelAnalyze,
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenManavaDetail(
                    viewModelAnalyze = viewModelAnalyze
                )

            } else {

                val modalSheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
                    skipHalfExpanded = false
                )

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenManavaraPropertyList(
                        viewModelAnalyze = viewModelAnalyze,
                    )

                }) {
                    Scaffold(
                        topBar = {
                            ScreenManavaraTopbar(
                                viewModelAnalyze = viewModelAnalyze,
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                })
                        },

                        ) {
                        Box(
                            Modifier
                                .padding(it)
                                .background(color = colorF6F6F6)
                                .fillMaxSize()
                        ) {
                            ScreenManavaraItemDetail(
                                viewModelAnalyze = viewModelAnalyze
                            )
                        }
                    }

                    ModalBottomSheetLayout(
                        sheetState = modalSheetState,
                        sheetElevation = 50.dp,
                        sheetShape = RoundedCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp
                        ),
                        sheetContent = {

//                            if(currentRoute == "NOVEL" || currentRoute == "COMIC"){
//
//                                Spacer(modifier = Modifier.size(4.dp))
//
//                                ScreenDialogBest(
//                                    item = state.itemBookInfo,
//                                    trophy = state.itemBestInfoTrophyList,
//                                    isExpandedScreen = isExpandedScreen,
//                                    currentRoute = currentRoute,
//                                    modalSheetState = modalSheetState
//                                )
//                            } else {
//                                ScreenTest()
//                            }
                        },
                    ) {}
                }

                BackOnPressedMobile(modalSheetState = modalSheetState)

            }
        }
    }
}