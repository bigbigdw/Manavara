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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.main.viewModels.ViewModelLogin
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.colorA7ACB7
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6

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

    val modifier = if(isExpandedLogin){
        Modifier.width(330.dp).fillMaxHeight()
    } else {
        Modifier.fillMaxSize()
    }

    Row{

        Box(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(color = colorF6F6F6)
                    .verticalScroll(rememberScrollState())
                    .semantics { contentDescription = "Overview Screen" },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MainHeader(image = R.drawable.ic_launcher, title = "세팅바라")

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp))

                if(isExpandedLogin){
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .width(260.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(width = 1.dp, color = color20459E),
                    ) {
                        Text(text = "회원 가입 진행", textAlign = TextAlign.Center, color = color20459E, fontSize = 16.sp)
                    }
                } else {
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
                        Text(text = "Google로 로그인", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp)
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
                        Text(text = "회원 가입", textAlign = TextAlign.Center, color = color20459E, fontSize = 16.sp)
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
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp))
                Text(
                    text = "BIGBIGDW",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = color898989,
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp))
            }
        }

        if(isExpandedLogin){
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

            MainHeader(image = R.drawable.ic_launcher, title = "세팅바라")

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Text(
                text = "마나바라 세팅앱",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = colorA7ACB7,
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
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "BIGBIGDW",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = color898989,
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplash() {
    ScreenSplash()
}