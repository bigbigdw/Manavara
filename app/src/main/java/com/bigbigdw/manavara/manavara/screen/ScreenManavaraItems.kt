package com.bigbigdw.manavara.manavara.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.manavara.getPickList
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import postFCMAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {

    val context = LocalContext.current
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    LazyColumn {

        item {ItemTabletTitle(str = "문의 사항 전송")}

        item{
            TabletContentWrap {
                TextField(
                    value = getFCM.title,
                    onValueChange = {
                        setFCM(getFCM.copy(title = it))
                    },
                    label = { Text("푸시 알림 제목", color = color898989) },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0),
                        textColor = color000000
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    value = getFCM.body,
                    onValueChange = {
                        setFCM(getFCM.copy(body = it))
                    },
                    label = { Text("푸시 알림 내용", color = color898989) },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0),
                        textColor = color000000
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                    BtnMobile(
                        func = { postFCMAlert(context = context, getFCM = getFCM) },
                        btnText = "문의사항 등록"
                    )
                }
            }
        }

        item{ Spacer(modifier = Modifier.size(60.dp)) }
        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMyPick(viewModelManavara: ViewModelManavara) {

    val context = LocalContext.current

    LaunchedEffect(viewModelManavara){
        getPickList { pickCategory, pickItemList ->
            viewModelManavara.setPickList(pickCategory = pickCategory, pickItemList = pickItemList)
        }
    }

    LazyColumn {

    }
}