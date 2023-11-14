package com.bigbigdw.manavara.login.screen

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.requiredWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.firebase.DataFCMBody
import com.bigbigdw.manavara.firebase.DataFCMBodyData
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.login.events.StateLogin
import com.bigbigdw.manavara.main.models.UserInfo
import com.bigbigdw.manavara.login.viewModels.ViewModelLogin
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.screen.AlertTwoBtn
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.MainHeader
import com.bigbigdw.manavara.util.screen.ScreenTabletWrap
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import com.bigbigdw.manavara.util.screen.spannableString
import postFCM

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
                contents = { RegisterInfo(state = state) },
                modifier = Modifier.Companion.requiredWidth(220.dp)
            )
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

                MainHeader(image = R.drawable.logo_transparents, title = "마나바라")

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

            MainHeader(image = R.drawable.logo_transparents, title = "마나바라")

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

    val context = LocalContext.current

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
                        "\n\n회원 가입 후 승인이 되어야 마나바라 서비스를 이용할 수 있습니다.",
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

        Spacer(modifier = Modifier.size(16.dp))
    }

    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        BtnMobile(
            func = {
                viewModelLogin.doRegister(
                    getUserInfo = getUserInfo
                )

                val year = DBDate.dateMMDDHHMMss().substring(0, 4)
                val month = DBDate.dateMMDDHHMMss().substring(4, 6)
                val day = DBDate.dateMMDDHHMMss().substring(6, 8)
                val hour = DBDate.dateMMDDHHMMss().substring(8, 10)
                val min = DBDate.dateMMDDHHMMss().substring(10, 12)
                val sec = DBDate.dateMMDDHHMMss().substring(12, 14)

                val fcmBody = DataFCMBody(
                    "/topics/user",
                    "high",
                    DataFCMBodyData("마나바라", " 회원가입 - ${viewModelLogin.state.value.userInfo.userEmail}"),
                    DataFCMBodyNotification(
                        title = "마나바라",
                        body = "${year}.${month}.${day} ${hour}:${min}:${sec}  회원가입 - ${viewModelLogin.state.value.userInfo.userEmail}",
                        click_action = "best"
                    ),
                )

                postFCM(
                    context = context,
                    fcmBody = fcmBody,
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
fun ScreenAfterSplash(
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
                contents = { RegisterInfo(state = state) },
                modifier = Modifier.Companion.requiredWidth(400.dp)
            )
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
            image = R.drawable.logo_transparents,
            title = "회원 가입"
        )

        ScreenRegister(
            viewModelLogin = viewModelLogin,
            setUserInfo = setUserInfo,
            getUserInfo = getUserInfo
        )
    }
}