package com.bigbigdw.manavara.util.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bigbigdw.manavara.R
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.best.screen.ScreenItemBestCount
import com.bigbigdw.manavara.main.models.MenuInfo
import com.bigbigdw.manavara.ui.theme.color000000
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.color8E8E8E
import com.bigbigdw.manavara.ui.theme.color8F8F8F
import com.bigbigdw.manavara.ui.theme.colorE9E9E9
import com.bigbigdw.manavara.ui.theme.colorEDE6FD
import com.bigbigdw.manavara.ui.theme.colorF6F6F6
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.colorList
import com.bigbigdw.manavara.util.getPlatformLogoEng
import kotlinx.coroutines.launch

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if (System.currentTimeMillis() - backPressedTime <= 400L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackOnPressedMobile(modalSheetState: ModalBottomSheetState) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var backPressedTime = 0L
    var backPressedState by remember { mutableStateOf(true) }

    BackHandler(enabled = true) {
        if (modalSheetState.isVisible) {
            coroutineScope.launch {
                modalSheetState.hide()
            }
        } else {
            if (System.currentTimeMillis() - backPressedTime <= 400L) {
                (context as Activity).finish()
            } else {
                backPressedState = true
                Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}

@Composable
fun ScreenTest() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text = "SETTING",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ScreenTabletWrap(title: String, contents: @Composable () -> Unit) {
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
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                text = title,
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )

            Spacer(modifier = Modifier.size(16.dp))

            contents()

            Spacer(modifier = Modifier.size(60.dp))
        }
    }
}

@Composable
fun ItemTabletTitle(str : String, isTopPadding : Boolean = true){

    if(isTopPadding){
        Spacer(modifier = Modifier.size(16.dp))
    }

    Text(
        modifier = Modifier.padding(16.dp, 8.dp),
        text = str,
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )
}

@Composable
fun TabletContentWrap(radius: Int = 20, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(size = radius.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp, 4.dp)
        ) {

            Spacer(modifier = Modifier.size(4.dp))

            content()

            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
fun BtnMobile(func: () -> Unit, btnText: String) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
    )
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = color20459E),
        onClick = func,
        modifier = Modifier
            .width(260.dp)
            .height(56.dp),
        shape = RoundedCornerShape(50.dp)

    ) {
        Text(
            text = btnText,
            textAlign = TextAlign.Center,
            color = colorEDE6FD,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun AlertTwoBtn(
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    btnLeft: String,
    btnRight: String,
    contents: @Composable () -> Unit,
    modifier: Modifier,
    btnLeftColor : Color = color8F8F8F,
    btnLeftTextColor : Color = Color.White,
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier,
        ) {
            Column(
                modifier = modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.size(36.dp)
                )

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ) {

                    Spacer(modifier = Modifier.size(40.dp))

                    Column(
                        modifier = Modifier
                            .semantics { contentDescription = "Overview Screen" },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        contents()

                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = btnLeftColor),

                                onClick = { onClickLeft() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 10.dp)

                            ) {
                                Text(
                                    text = btnLeft,
                                    textAlign = TextAlign.Center,
                                    color = btnLeftTextColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                                onClick = {
                                    onClickRight()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 0.dp)

                            ) {
                                Text(
                                    text = btnRight,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .height(70.dp)
                )
            }
        }
    }
}

@Composable
fun AlertOneBtn(
    isShow: () -> Unit,
    btnText: String,
    contents: @Composable () -> Unit,
    modifier: Modifier
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier,
        ) {
            Column(
                modifier = modifier.wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.size(36.dp)
                )

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ) {

                    Spacer(modifier = Modifier.size(40.dp))

                    Column(
                        modifier = Modifier
                            .semantics { contentDescription = "Overview Screen" },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        contents()

                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = color20459E),
                            onClick = {
                                isShow()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 0.dp)

                        ) {
                            Text(
                                text = btnText,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .height(70.dp)
                )
            }
        }
    }
}

@Composable
fun spannableString(textFront: String, color: Color, textEnd: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color, fontWeight = FontWeight(weight = 500))) {
            append(textFront)
        }
        append(textEnd)
    }

    return annotatedString
}

@Composable
fun MainHeader(image: Int, title: String) {

    Card(
        modifier = Modifier
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
    ) {
        Box(
            modifier = Modifier
                .height(90.dp)
                .width(90.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .height(72.dp)
                    .width(72.dp)
            )
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )
    Text(
        modifier = Modifier.padding(32.dp, 0.dp),
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = color000000
    )
}

@Composable
fun ItemMainSettingSingleTablet(
    containerColor: Color,
    image: Int,
    title: String,
    body: String,
    current: String,
    onClick: () -> Unit,
    value: String
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (current == value) {
                colorE9E9E9
            } else {
                colorF7F7F7
            }
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = {
            onClick()
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
                    colors = CardDefaults.cardColors(containerColor = containerColor),
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
fun ScreenItemKeyword(
    getter: String,
    title: String,
    getValue: String,
    onClick: () -> Unit,
) {

    Card(
        modifier = if (getter == getValue) {
            Modifier.border(2.dp, color20459E, CircleShape)
        } else {
            Modifier.border(2.dp, colorF7F7F7, CircleShape)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (getter == getValue) {
                color20459E
            } else {
                Color.White
            }
        ),
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp, 8.dp)
                .clickable {
                    onClick()
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = title,
                fontSize = 17.sp,
                textAlign = TextAlign.Left,
                color = if (getter == getValue) {
                    Color.White
                } else {
                    Color.Black
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TabletContentWrapBtn(content: @Composable () -> Unit, onClick: () -> Unit, isContinue: Boolean = true){

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            content()
        })

    if(isContinue){
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun TabletBorderLine(){
    Spacer(modifier = Modifier.size(8.dp))
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = color8E8E8E))
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun ScreenEmpty(str : String = "마나바라") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .defaultMinSize(minHeight = 300.dp)
            .background(colorF6F6F6),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Card(
                modifier = Modifier
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.logo_transparents),
                        contentDescription = null,
                        modifier = Modifier
                            .height(72.dp)
                            .width(72.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )
            Text(
                text = str,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = color000000
            )
        }
    }
}

@Composable
fun ScreenTopbar(detail: String, menu : String, onClick: () -> Unit) {

    Row(
        Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onClick() }) {
                Image(
                    painter = painterResource(id = R.drawable.icon_drawer),
                    contentDescription = null,
                    modifier = Modifier
                        .width(22.dp)
                        .height(22.dp)
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text(
                    text = detail.ifEmpty {
                        menu
                    },
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    color = color000000,
                    fontWeight = FontWeight.Bold
                )

            }

        }
    }
}

@Composable
fun ScreenMenuItem(
    item: MenuInfo,
    index: Int,
    current: String,
    onClick: () -> Unit
) {

    ItemMainSettingSingleTablet(
        containerColor = colorList[index % colorList.size],
        image = item.image,
        title = item.menu,
        body = item.body,
        current = current,
        value = item.menu,
        onClick = {
            onClick()
        },
    )

    if(item.needLine){
        TabletBorderLine()
    }
}

@Composable
fun ScreenBookCard(
    mode: String = "NUMBER",
    type: String = "WEEK",
    item: ItemBookInfo,
    index: Int,
    needIntro: Boolean = true,
    backgroundColor: Color = Color.White,
    additionalContents: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(
            start = 0.dp,
            top = 0.dp,
            end = 0.dp,
            bottom = 0.dp,
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = {
            coroutineScope.launch {
                onClick()
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(24.dp, 4.dp)
            ) {

                Spacer(modifier = Modifier.size(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {

                    ScreenBookCardItem(mode = mode, item = item, index = index)

                    if (type == "MONTH") {
                        Spacer(modifier = Modifier.size(8.dp))

                        ScreenItemBestCount(item = item)
                    }

                    additionalContents()

                    if(needIntro && item.intro.isNotEmpty()){
                        Spacer(modifier = Modifier.size(16.dp))

                        Text(
                            text = item.intro,
                            color = color8E8E8E,
                            fontSize = 16.sp,
                        )
                    } else {
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
            }
        })

    Spacer(modifier = Modifier.size(20.dp))
}

@Composable
fun ScreenBookCardItem(mode : String, item: ItemBookInfo, index: Int){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {

        Box{

            Card(
                modifier = Modifier.background(Color.White),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {

                AsyncImage(
                    model = item.bookImg,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredWidth(140.dp)
                        .requiredHeight(200.dp)
                )
            }

            if(index > -1){
                Card(
                    modifier = Modifier.padding(6.dp),
                    colors = CardDefaults.cardColors(containerColor = color20459E),
                    shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp),
                    border = BorderStroke(width = 1.dp, color = Color.White)
                ) {
                    Box(modifier = Modifier.size(35.dp), contentAlignment = Alignment.Center) {

                        if(mode == "NUMBER"){
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = "${index + 1}",
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                            )
                        } else {
                            AsyncImage(
                                model = getPlatformLogoEng(item.platform),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = Modifier.wrapContentHeight()) {
            Text(
                text = item.title,
                color = color20459E,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (item.writer.isNotEmpty()) {
                Text(
                    text = item.writer,
                    color = color000000,
                    fontSize = 16.sp,
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            if (item.genre.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "장르 : ",
                        color = color000000,
                        textEnd = item.genre
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntRecom.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "작품 추천 수 : ",
                        color = color000000,
                        textEnd = item.cntRecom
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntChapter.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "총 편수 : ",
                        color = color000000,
                        textEnd = item.cntChapter
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntFavorite.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "선호작 수 : ",
                        color = color000000,
                        textEnd = item.cntFavorite
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntPageRead.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "조회 수 : ",
                        color = color000000,
                        textEnd = item.cntPageRead
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.cntTotalComment.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "댓글 수 : ",
                        color = color000000,
                        textEnd = item.cntTotalComment
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }

            if (item.bookCode.isNotEmpty()) {
                Text(
                    text = spannableString(
                        textFront = "북코드 : ",
                        color = color000000,
                        textEnd = item.bookCode
                    ),
                    color = color8E8E8E,
                    fontSize = 16.sp,
                )
            }
        }
    }
}