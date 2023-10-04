package com.bigbigdw.manavara.main.screen

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.ui.theme.color4AD7CF
import com.bigbigdw.manavara.ui.theme.color52A9FF
import com.bigbigdw.manavara.ui.theme.color5372DE
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.screen.ItemMainSettingSingle
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
                val (getDetailPage, setDetailPage) = remember { mutableStateOf(false) }

                ScreenBestTabletList(
                    setMenu = setMenu,
                    getMenu = getMenu,
                    onClick = { setDetailPage(false) },
                    viewModelBest = viewModelBest,
                    viewModelMain = viewModelMain
                )

                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(color = colorF6F6F6)
                )

                ScreenTest()

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
    onClick: () -> Unit,
    viewModelBest: ViewModelBest,
    viewModelMain: ViewModelMain
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

        ItemMainSettingSingle(
            containerColor = color8F8F8F,
            image = R.drawable.icon_setting_wht,
            title = "유저 옵션",
            body = "장르 선택, 뷰 모드 포함 유저 설정 전반",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() },
            bestType = "투데이"
        )

        ItemMainSettingSingle(
            containerColor = color8F8F8F,
            image = R.drawable.icon_setting_wht,
            title = "작품 검색",
            body = "플랫폼과 무관하게 작품 검색 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() },
            bestType = "투데이"
        )

        TabletBorderLine()

        ItemMainSettingSingle(
            containerColor = color52A9FF,
            image = R.drawable.icon_best_wht,
            title = "투데이 베스트",
            body = "투데이 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() },
            bestType = "투데이"
        )

        mainState.platformRange.forEachIndexed{ index, item ->
            ItemMainSettingSingle(
                containerColor = color52A9FF,
                image = R.drawable.icon_best_wht,
                title = item,
                body = "투데이 베스트 관련 옵션 진행",
                bestType = "투데이",
                setMenu = setMenu,
                getMenu = getMenu,
                onClick = {

                    viewModelBest.getBestJsonList(
                        platform = changePlatformNameEng(item),
                        genre = "ALL",
                        type = mainState.userInfo.viewMode,
                        date = DBDate.dateYesterdayMMDD()
                    )
                }
            )
        }

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_best_wht,
            title = "주간 베스트",
            body = "주간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() },
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_best_wht,
            title = "월간 베스트",
            body = "월간 베스트 관련 옵션 진행",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() },
        )
    }
}