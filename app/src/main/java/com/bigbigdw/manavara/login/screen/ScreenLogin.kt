package com.bigbigdw.manavara.login.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.login.events.StateLogin
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.login.viewModels.ViewModelLogin
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.changePlatformNameEng
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.MainHeader
import com.bigbigdw.manavara.util.screen.ScreenTabletWrap
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import com.bigbigdw.manavara.util.screen.spannableString

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ScreenLogin(
    doLogin: () -> Unit,
    widthSizeClass: WindowWidthSizeClass,
    viewModelLogin: ViewModelLogin,
    activity: ComponentActivity
) {

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    val state = viewModelLogin.state.collectAsState().value

    val isExpandedLogin = state.isResgister && state.isExpandedScreen

    val (getUserInfo, setUserInfo) = remember { mutableStateOf(state.userInfo) }

    viewModelLogin.isExpandedScreen(bool = isExpandedScreen)

    val modifier = if (isExpandedLogin) {
        Modifier
            .width(360.dp)
            .fillMaxHeight()
    } else {
        Modifier.fillMaxSize()
    }

    if (isExpandedLogin && state.isRegisterConfirm) {
        Dialog(
            onDismissRequest = { viewModelLogin.setIsRegisterConfirm(false) },
        ) {
            AlertTwoBtn(
                isShow = { viewModelLogin.setIsRegisterConfirm(false) },
                onFetchClick = {
                    viewModelLogin.setIsRegisterConfirm(false)
                    viewModelLogin.finishRegister(activity = activity)
                },
                btnLeft = "뒤로가기",
                btnRight = "확인",
                contents = { RegisterInfo(state = state) })
        }
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
                        text = "웹툰/웹소설 수집 어플리케이션",
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
            ScreenTabletWrap(
                title = "회원가입",
                contents = {
                    ScreenRegister(
                        viewModelLogin = viewModelLogin,
                        setUserInfo = setUserInfo,
                        getUserInfo = getUserInfo
                    )
                })
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
                text = "웹툰/웹소설 수집 어플리케이션",
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
fun ScreenRegister(
    viewModelLogin: ViewModelLogin,
    setUserInfo: (UserInfo) -> Unit,
    getUserInfo: UserInfo,
    isEdit: Boolean = false
) {

    if (isEdit) {
        Spacer(modifier = Modifier.size(4.dp))
    }

    TabletContentWrap {

        if (!isEdit) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "마나바라 회원가입",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "마나바라는 김대우가 AOS/iOS 스터디를 위해 혼자 만든 웹소설/웹툰 수집 어플리케이션입니다. 유익하게 사용 하실 수 있는 분이라면 유익하게 사용해주시면 감사드리겠습니다." +
                        "\n\n마나바라는 기존에 유성아 파트장님, 박주은 프로님과 같이 진행했던 웹소설 모음 어플리케이션 모아바라 어플리케이션의 개량형입니다.",
                color = color8E8E8E,
                fontSize = 16.sp,
            )
        }
    }

    ItemTabletTitle(
        if (isEdit) {
            "회원정보 수정"
        } else {
            "회원정보 입력"
        }
    )

    TabletContentWrap {
        TextField(
            value = getUserInfo.userNickName,
            onValueChange = { setUserInfo(getUserInfo.copy(userNickName = it)) },
            label = { Text("닉네임을 입력해 주십시오", color = color898989) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0),
                textColor = color000000
            ),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = getUserInfo.userEmail,
            enabled = false,
            onValueChange = {},
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

    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        BtnMobile(
            func = {
                viewModelLogin.doRegister(
                    getUserInfo = getUserInfo
                )
            },
            btnText = "회원 가입"
        )
    }

    if (isEdit) {
        Spacer(modifier = Modifier.size(20.dp))
    }

}

@Composable
fun ItemChooseGenre(
    title: String,
    isLast: Boolean,
    setRange: (ArrayList<String>) -> Unit,
    getRange: ArrayList<String>,
) {

    Column {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 0.dp,
            ),
            shape = RoundedCornerShape(0.dp),
            onClick = {
                if (getRange.contains(changePlatformNameEng(title))) {
                    getRange.remove(changePlatformNameEng(title))
                    setRange(getRange)
                    Log.d("HIHIHI", "getRangeComic == $getRange")
                } else {
                    getRange.add(changePlatformNameEng(title))
                    setRange(getRange)
                    Log.d("HIHIHI", "getRangeComic == $getRange")
                }
            },
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = if (getRange.contains(changePlatformNameEng(title))) {
                            color1CE3EE
                        } else {
                            color000000
                        },
                        fontWeight = if (getRange.contains(changePlatformNameEng(title))) {
                            FontWeight(weight = 800)
                        } else {
                            FontWeight(weight = 400)
                        }
                    )
                }
            })

        if (!isLast) {
            Spacer(modifier = Modifier.size(2.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorE9E9E9)
            )
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun RegisterInfo(state: StateLogin) {
    Row(
        Modifier
            .padding(20.dp, 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "회원정보",
            color = color1CE3EE,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
        Text(
            text = "를 확인해 주세요.",
            color = color000000,
            fontSize = 16.sp
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
    )

    Row(
        Modifier
            .padding(20.dp, 0.dp)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = spannableString(
                textFront = "닉네임 : ",
                color = color1CE3EE,
                textEnd = state.userInfo.userNickName
            ),
            color = color000000,
            fontSize = 14.sp
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )

    Row(
        Modifier
            .padding(20.dp, 0.dp)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = spannableString(
                textFront = "이메일 : ",
                color = color1CE3EE,
                textEnd = state.userInfo.userEmail
            ),
            color = color000000,
            fontSize = 14.sp
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    )
}

@Composable
fun ScreenRegisterMobile(
    viewModelLogin: ViewModelLogin,
    setUserInfo: (UserInfo) -> Unit,
    getUserInfo: UserInfo,
    activity: ComponentActivity
) {

    val state = viewModelLogin.state.collectAsState().value

    if (state.isRegisterConfirm) {
        Dialog(
            onDismissRequest = { viewModelLogin.setIsRegisterConfirm(false) },
        ) {
            AlertTwoBtn(
                isShow = { viewModelLogin.setIsRegisterConfirm(false) },
                onFetchClick = {
                    viewModelLogin.setIsRegisterConfirm(false)
                    viewModelLogin.finishRegister(activity = activity)
                },
                btnLeft = "뒤로가기",
                btnRight = "확인",
                contents = { RegisterInfo(state = state) })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
            .padding(16.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        MainHeader(
            image = R.drawable.ic_launcher,
            title = "회원 가입"
        )

        ScreenRegister(
            viewModelLogin = viewModelLogin,
            setUserInfo = setUserInfo,
            getUserInfo = getUserInfo
        )
    }
}