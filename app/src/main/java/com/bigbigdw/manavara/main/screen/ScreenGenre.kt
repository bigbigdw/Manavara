package com.bigbigdw.manavara.main.screen

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavara.main.models.ItemKeyword
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.ui.theme.color1CE3EE
import com.bigbigdw.manavara.ui.theme.color20459E
import com.bigbigdw.manavara.ui.theme.colorF7F7F7
import com.bigbigdw.manavara.util.changePlatformNameKor
import com.bigbigdw.manavara.util.genreListEng
import com.bigbigdw.manavara.util.getPlatformLogo

@Composable
fun GenreDetailJson(
    viewModelBest: ViewModelBest,
    getDetailType: String,
    menuType: String
) {

    val (getPlatform, setPlatform) = remember { mutableStateOf("JOARA") }

    LaunchedEffect(menuType, getPlatform) {
        when (menuType) {
            "투데이" -> {
                viewModelBest.getJsonGenreList(platform = getPlatform, type = getDetailType)
            }
            "주간" -> {
                viewModelBest.getJsonGenreWeekList(
                    platform = getPlatform,
                    type = getDetailType
                )
            }
            else -> {
                viewModelBest.getJsonGenreMonthList(
                    platform = getPlatform,
                    type = getDetailType
                )
            }
        }
    }

    val state = viewModelBest.state.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        LazyRow {
            itemsIndexed(genreListEng()) { index, item ->
                Box {
                    ScreenItemKeyword(
                        getter = getPlatform,
                        setter = setPlatform,
                        title =  changePlatformNameKor(item),
                        getValue = item
                    )
                }
            }
        }

        LazyColumn(modifier = Modifier.padding(0.dp, 16.dp)) {
            itemsIndexed(state.genreDay) { index, item ->
                ListGenreToday(
                    itemBestKeyword = item,
                    index = index
                )
            }
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ScreenItemKeyword(
    getter: String,
    setter: (String) -> Unit,
    title: String,
    getValue: String
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
                    setter(getValue)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = getPlatformLogo(title)),
                contentDescription = null,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

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
fun ListGenreToday(
    itemBestKeyword: ItemKeyword,
    index: Int,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = {},
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 0.dp,
            ),
            content = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${index + 1} ",
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(16.dp, 0.dp, 0.dp, 0.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Left,
                        color = color20459E,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        maxLines = 1,
                        text = itemBestKeyword.title,
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = itemBestKeyword.value,
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        color = color1CE3EE,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            })
    }
}