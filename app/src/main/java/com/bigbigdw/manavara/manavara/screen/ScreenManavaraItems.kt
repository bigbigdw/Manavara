package com.bigbigdw.manavara.manavara.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bigbigdw.manavara.best.getBookItemWeekTrophy
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.firebase.DataFCMBodyNotification
import com.bigbigdw.manavara.manavara.getPickList
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color898989
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.novelListEng
import com.bigbigdw.manavara.util.screen.BtnMobile
import com.bigbigdw.manavara.util.screen.ItemTabletTitle
import com.bigbigdw.manavara.util.screen.ScreenBookCard
import com.bigbigdw.manavara.util.screen.ScreenEmpty
import com.bigbigdw.manavara.util.screen.ScreenItemKeyword
import com.bigbigdw.manavara.util.screen.TabletContentWrap
import kotlinx.coroutines.launch
import postFCMAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {

    val context = LocalContext.current
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    LazyColumn {

        item { ItemTabletTitle(str = "문의 사항 전송") }

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMyPick(
    viewModelManavara: ViewModelManavara,
    modalSheetState: ModalBottomSheetState?,
    setDialogOpen: ((Boolean) -> Unit)?
) {

    val state = viewModelManavara.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModelManavara){
        getPickList(type = "NOVEL") { pickCategory, pickItemList ->
            viewModelManavara.setPickList(pickCategory = pickCategory, pickItemList = pickItemList)
        }
    }

    LazyColumn {
        item {
            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp),
            ) {
                itemsIndexed(state.pickCategory) { index, item ->
                    Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)) {
                        ScreenItemKeyword(
                            getter = item,
                            onClick = {
                                coroutineScope.launch {
                                    viewModelManavara.setScreen(
                                        platform = item,
                                        type = "NOVEL",
                                        menu = state.menu,
                                    )
                                }
                            },
                            title = changePlatformNameKor(item),
                            getValue = state.platform
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.size(4.dp))
        }

        if(state.pickItemList.isEmpty()){
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    ScreenEmpty(str = "데이터가 없습니다")
                }
            }
        } else {
            var filterdList = ArrayList<ItemBookInfo>()

            if(state.platform == "전체"){
                filterdList = state.pickItemList

            } else {
                for(item in state.pickItemList){
                    if(item.type == state.platform){
                        filterdList.add(item)
                    }
                }
            }

            itemsIndexed(filterdList) { index, item ->

                Box(modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    .wrapContentSize()){
                    ScreenBookCard(
                        mode = "PLATFORM",
                        item = item,
                        index = index,
                    ) {

                        coroutineScope.launch {
                            getBookItemWeekTrophy(
                                bookCode = item.bookCode,
                                type = "NOVEL",
                                platform = item.type
                            ) { itemBestInfoTrophyList ->

                                viewModelManavara.setItemBestInfoTrophyList(
                                    itemBookInfo = item,
                                    itemBestInfoTrophyList = itemBestInfoTrophyList
                                )
                            }

                            modalSheetState?.show()

                            if (setDialogOpen != null) {
                                setDialogOpen(true)
                            }
                        }
                    }
                }
            }
        }

    }
}