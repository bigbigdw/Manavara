package com.bigbigdw.manavara.manavara.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.analyze.screen.ScreenAnalyzeTopbar
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenManavara(
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState? = null,
    drawerState: DrawerState,
    viewModelAnalyze: ViewModelAnalyze,
    currentRoute: String?,
) {

    val context = LocalContext.current

    val state = viewModelAnalyze.state.collectAsState().value

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
//                                    ScreenDialogBest(
//                                        item = viewModelAnalyze.state.collectAsState().value.itemBookInfo,
//                                        trophy = viewModelAnalyze.state.collectAsState().value.itemBestInfoTrophyList,
//                                        isExpandedScreen = isExpandedScreen,
//                                        currentRoute = "NOVEL",
//                                        modalSheetState = modalSheetState
//                                    )
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
                            ScreenAnalyzeTopbar {
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

//                            ScreenAnalyzeItem(
//                                viewModelAnalyze = viewModelAnalyze,
//                                drawerState = drawerState,
//                                menu = "",
//                                setDetail = {},
//                                setPlatform = {},
//                                setType = {},
//                                platform = platform,
//                                type = type
//                            )
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

@Composable
fun ScreenManavaraPropertyList(
    viewModelAnalyze: ViewModelAnalyze,
) {

    val state = viewModelAnalyze.state.collectAsState().value

    Column(
        modifier = Modifier
            .width(330.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Column {
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = "마나바라 스페셜",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight(weight = 700)
            )


            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "나의 기록",
                body = "https://m.comic.naver.com/event/yearend/2023",
                current = "state.menu",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "내가 분석한 작품",
                body = "--------",
                current = "state.menu",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "옵션",
                body = "--------",
                current = "state.menu",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "메세지함",
                body = "--------",
                current = "",
                onClick = {  },
                value = "작품 검색",
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "나의 PICK 보기",
                body = "------",
                current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "다른 PICK 보기",
                body = "------",
                current = "",
                onClick = {  },
                value = "작품 검색",
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "이벤트",
                body = "------",
                current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "커뮤니티",
                body = "------",
                current = "",
                onClick = {  },
                value = "작품 검색",
            )
        }
    }
}


