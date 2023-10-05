package com.bigbigdw.manavara.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color52A9FF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color998DF9
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.ui.theme.colorea927C
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingleTablet
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletBorderLine

@Composable
fun ScreenBest(
    isExpandedScreen: Boolean,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain
) {


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if (isExpandedScreen) {

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }
                val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf("") }
                val (getDetailGenre, setDetailGenre) = remember { mutableStateOf("") }
                val (getDetailType, setDetailType) = remember { mutableStateOf("") }

                ScreenBestTabletList(
                    setMenu = setMenu,
                    getMenu = getMenu,
                    viewModelBest = viewModelBest,
                    viewModelMain = viewModelMain,
                    setDetailPlatform = setDetailPlatform,
                    setDetailGenre = setDetailGenre,
                    setDetailType = setDetailType
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenBestDetail(
                    getMenu = getMenu,
                    viewModelMain = viewModelMain,
                    getDetailPlatform = getDetailPlatform,
                    getDetailGenre = getDetailGenre,
                    getDetailType = getDetailType,
                )

            } else {
                ScreenTest()
            }
        }
    }
}

@Composable
fun ScreenBestTabletList(
    setMenu: (String) -> Unit,
    getMenu: String,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain,
    setDetailPlatform: (String) -> Unit,
    setDetailGenre: (String) -> Unit,
    setDetailType: (String) -> Unit
) {

    val mainState = viewModelMain.state.collectAsState().value

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
            text = "베스트",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight(weight = 700)
        )

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_setting_wht,
            title = "유저 옵션",
            body = "뷰모드 전환 및 플랫폼 선택",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_setting_wht,
            title = "작품 검색",
            body = "플랫폼과 무관하게 작품 검색 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_best_wht,
            title = "투데이 베스트",
            body = "투데이 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        mainState.platformRange.forEachIndexed{ index, item ->
            ItemBestListSingle(
                containerColor = color52A9FF,
                image = R.drawable.icon_best_wht,
                title = changePlatformNameKor(item),
                body = "투데이 베스트 관련 옵션 진행",
                setMenu = setMenu,
                getMenu = getMenu,
                bestType = "투데이 베스트",
                setDetailPlatform = { setDetailPlatform(item) },
                setDetailType = { setDetailType(mainState.userInfo.viewMode) },

            )
        }

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_best_wht,
            title = "주간 베스트",
            body = "주간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorea927C,
            image = R.drawable.icon_best_wht,
            title = "월간 베스트",
            body = "월간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {  },
        )
    }
}

@Composable
fun ItemBestListSingle(
    containerColor: Color,
    image: Int,
    title: String,
    body: String,
    setMenu: (String) -> Unit,
    getMenu: String,
    bestType: String,
    setDetailPlatform: () -> Unit,
    setDetailType: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (getMenu == title) {
                colorE9E9E9
            } else {
                colorF7F7F7
            }
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            setMenu("$bestType $title")
            setDetailPlatform()
            setDetailType()
        },
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 6.dp,
            end = 12.dp,
            bottom = 6.dp,
        ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    backgroundColor = containerColor,
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .height(28.dp)
                                .width(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                Column {
                    Row {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = color000000,

                            fontWeight = FontWeight(weight = 500)
                        )
                    }

                    Row {
                        Text(
                            text = body,
                            fontSize = 14.sp,
                            color = color8E8E8E,
                        )
                    }
                }
            }
        })
}

@Composable
fun ScreenBestDetail(
    getMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPlatform: String,
    getDetailGenre: String,
    getDetailType: String,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
                .background(color = colorF6F6F6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier
                    .padding(24.dp, 0.dp, 0.dp, 0.dp),
                text = "< $getMenu",
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )

            if (getMenu.contains("투데이")) {
                ScreenTest()
            }
        }
    }
}