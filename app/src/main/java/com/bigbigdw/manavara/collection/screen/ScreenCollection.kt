package com.bigbigdw.manavara.collection.screen

import android.widget.Toast
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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.analyze.screen.ScreenManavaItems
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraItem
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraTopbar
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.ui.theme.color64C157
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenCollection(
    isExpandedScreen: Boolean,
    modalSheetState: ModalBottomSheetState? = null,
    drawerState: DrawerState,
    currentRoute: String?,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) { "ViewModelStoreOwner is null." }
    val viewModelAnalyze: ViewModelAnalyze = viewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state = viewModelAnalyze.state.collectAsState().value

    DisposableEffect(context) {

        viewModelAnalyze.sideEffects
            .onEach { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            .launchIn(coroutineScope)

        onDispose {
            // 컴포넌트가 detached 될 때 실행되는 코드
            // 이 부분에 필요한 clean-up 코드를 작성할 수 있습니다.
        }
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

                ScreenCollectionPropertyList(
                    viewModelAnalyze = viewModelAnalyze,
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

            } else {

                ModalNavigationDrawer(drawerState = drawerState, drawerContent = {

                    ScreenCollectionPropertyList(
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
                            ScreenManavaraItem(
                                viewModelAnalyze = viewModelAnalyze,
                                drawerState = drawerState,
                                menu = "",
                                setDetail = {},
                                setPlatform = {},
                                setType = {}
                            )
                        }
                    }

                    if (modalSheetState != null) {
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
                }

                if (modalSheetState != null) {
                    BackOnPressedMobile(modalSheetState = modalSheetState)
                }

            }
        }
    }
}

@Composable
fun ScreenCollectionPropertyList(
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
                title = "웹소설 신규 작품",
                body = "------",
                 current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "웹툰 신규 작품",
                body = "------",
                 current = "",
                onClick = {  },
                value = "작품 검색",
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.icon_search_wht,
                title = "작품 링크",
                body = "타 플랫폼에 있는 작품과 링크",
                 current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.icon_search_wht,
                title = "작품 링크 리스트",
                body = "내가 링크한 작품 리스트",
                 current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.icon_search_wht,
                title = "공유된 링크 리스트",
                body = "타인이 링크한 작품 리스트",
                 current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color64C157,
                image = R.drawable.icon_search_wht,
                title = "나의 콜렉션 분석 명세서",
                body = "내가 수집한 작품들 분석 현황 보기",
                 current = "",
                onClick = {  },
                value = "북코드 검색",
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.icon_search_wht,
                title = "작품 검색",
                body = "플랫폼과 무관하게 작품 검색 진행",
                 current = "",
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color64C157,
                image = R.drawable.icon_search_wht,
                title = "북코드 검색",
                body = "플랫폼과 무관하게 작품 검색 진행",
                 current = "",
                onClick = {  },
                value = "북코드 검색",
            )
        }
    }
}