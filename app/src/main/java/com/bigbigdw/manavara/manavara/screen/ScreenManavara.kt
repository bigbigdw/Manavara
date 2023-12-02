package com.bigbigdw.manavara.manavara.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraItemDetail
import com.bigbigdw.manavara.analyze.screen.ScreenManavaraTopbar
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyze
import com.bigbigdw.manavara.best.screen.ScreenDialogBest
import com.bigbigdw.manavara.best.models.ItemKeyword
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color1E4394
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color21C2EC
import com.bigbigdw.manavara.ui.theme.color31C3AE
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color536FD2
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color555b68
import com.bigbigdw.manavara.ui.theme.color64C157
import com.bigbigdw.manavara.ui.theme.color7C81FF
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorABD436
import com.bigbigdw.manavara.ui.theme.colorDCDCDD
import com.bigbigdw.manavara.ui.theme.colorEA927C
import com.bigbigdw.manavara.ui.theme.colorF17666
import com.bigbigdw.manavara.ui.theme.colorF17FA0
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.changeDetailNameKor
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.comicListEng
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformDataKeyComic
import com.bigbigdw.manavara.util.getPlatformDataKeyNovel
import com.bigbigdw.manavara.util.getPlatformLogo
import com.bigbigdw.manavara.util.getPlatformLogoEng
import com.bigbigdw.manavara.util.manavaraListKor
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BackOnPressedMobile
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.TabletBorderLine
import com.bigbigdw.manavara.util.screen.TabletContentWrapBtn
import com.bigbigdw.manavara.util.screen.spannableString
import getBookCount
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

                com.bigbigdw.manavara.analyze.screen.ScreenManavaDetail(
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
                current = state.menu,
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "내가 분석한 작품",
                body = "--------",
                current = state.menu,
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "옵션",
                body = "--------",
                current = state.menu,
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "메세지함",
                body = "--------",
                current = state.menu,
                onClick = {  },
                value = "작품 검색",
            )

            TabletBorderLine()

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "이벤트",
                body = "------",
                current = state.menu,
                onClick = {  },
                value = "작품 검색",
            )

            ItemMainSettingSingleTablet(
                containerColor = color7C81FF,
                image = R.drawable.ic_launcher,
                title = "커뮤니티",
                body = "------",
                current = state.menu,
                onClick = {  },
                value = "작품 검색",
            )
        }
    }
}


