package com.bigbigdw.manavara.main.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.models.DataFCMBodyNotification
import com.bigbigdw.manavara.main.viewModels.ViewModelLogin
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.novelKor
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemMainTabletContent
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenTabletWrap
import com.bigbigdw.manavara.util.screen.ScreenTest
import com.bigbigdw.manavara.util.screen.TabletContentWrap

@Composable
fun ScreenLogin(
    doLogin: () -> Unit,
    widthSizeClass: WindowWidthSizeClass,
    viewModelLogin: ViewModelLogin
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    viewModelLogin.isExpandedScreen(bool = isExpandedScreen)

    val state = viewModelLogin.state.collectAsState().value

    val isExpandedLogin = state.isResgister && state.isExpandedScreen

    val modifier = if (isExpandedLogin) {
        Modifier
            .width(330.dp)
            .fillMaxHeight()
    } else {
        Modifier.fillMaxSize()
    }

    Row {

        Box(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorF6F6F6)
                    .verticalScroll(rememberScrollState())
                    .semantics { contentDescription = "Overview Screen" },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MainHeader(image = R.drawable.ic_launcher, title = "마나바라")

                if (isExpandedLogin) {
                    Text(
                        text = "웹툰/웹소설 통계 어플리케이션",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = color000000,
                    )
                } else {

                    Spacer(modifier = Modifier.size(22.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                        onClick = {
                            doLogin()
                        },
                        modifier = Modifier
                            .width(260.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(50.dp)

                    ) {
                        Text(
                            text = "Google로 로그인",
                            textAlign = TextAlign.Center,
                            color = colorEDE6FD,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    OutlinedButton(
                        onClick = {
                            doLogin()
                        },
                        modifier = Modifier
                            .width(260.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(width = 1.dp, color = color20459E),
                    ) {
                        Text(
                            text = "회원 가입",
                            textAlign = TextAlign.Center,
                            color = color20459E,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "By 김대우",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = color898989,
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Text(
                    text = "BIGBIGDW",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = color898989,
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }

        if (isExpandedLogin) {
            ScreenTabletWrap(title = "회원가입", contents = { ScreenRegister() })
        } else {
            ScreenTest()
        }
    }
}

@Composable
fun ScreenSplash() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorF6F6F6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainHeader(image = R.drawable.ic_launcher, title = "마나바라")

            Text(
                text = "웹툰/웹소설 통계 어플리케이션",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = color000000,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By 김대우",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = color898989,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Text(
                text = "BIGBIGDW",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = color898989,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenRegister() {

    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    TabletContentWrap {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "마나바라 회원가입",
                color = color000000,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "마나바라는 김대우가 AOS/iOS 스터디를 위해 혼자 만든 웹소설/웹툰 통계 어플리케이션입니다. 유익하게 사용 하실 수 있는 분이라면 유익하게 사용해주시면 감사드리겠습니다.",
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }
    }

    ItemTabletTitle("회원 정보 입력")

    TabletContentWrap {
        TextField(
            value = getFCM.title,
            onValueChange = {
                setFCM(getFCM.copy(title = it))
            },
            label = { Text("닉네임을 입력해 주십시오", color = color898989) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0),
                textColor = color000000
            ),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = getFCM.title,
            onValueChange = {
                setFCM(getFCM.copy(title = it))
            },
            label = { Text("이메일 주소", color = color898989) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0),
                textColor = color000000
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(16.dp))
    }

    ItemTabletTitle("웹소설 / 웹툰 선택")

    TabletContentWrap {
        ItemMainTabletContent(
            title = "웹소설",
            value = "",
            isLast = false
        )
        ItemMainTabletContent(
            title = "웹툰",
            value = "",
            isLast = true
        )
    }

    ItemTabletTitle("플랫폼 선택(다중 선택 가능)")

    TabletContentWrap {
        novelKor().forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item,
                value = "",
                isLast = novelKor().size - 1 == index
            )
        }
    }

    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        BtnMobile(
            func = { },
            btnText = "회원 가입"
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewScreenLogin() {
    ScreenTabletWrap(title = "회원가입", contents = { ScreenRegister() })
}