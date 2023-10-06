package com.bigbigdw.manavara.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.login.screen.ScreenRegisterMobile
import com.bigbigdw.manavara.login.viewModels.ViewModelLogin
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ActivityRegister : ComponentActivity() {

    private val viewModelLogin: ViewModelLogin by viewModels()
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelLogin.sideEffects
            .onEach { Toast.makeText(this@ActivityRegister, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        val state = viewModelLogin.state.value

        val uid = intent?.getStringExtra("UID") ?: return
        val email = intent?.getStringExtra("EMAIL") ?: return

        viewModelLogin.setUserInfo(uid = uid, email = email)

        setContent {
            val (getUserInfo, setUserInfo) = remember { mutableStateOf(state.userInfo) }

            val (getRangeNovel, setRangeNovel) = remember { mutableStateOf(state.platformRangeNovel) }

            val (getRangeComic, setRangeComic) = remember { mutableStateOf(state.platformRangeComic) }

            ScreenRegisterMobile(
                viewModelLogin = viewModelLogin,
                setRangeNovel = setRangeNovel,
                getRangeNovel = getRangeNovel,
                setRangeComic = setRangeComic,
                getRangeComic = getRangeComic,
                setUserInfo = setUserInfo,
                getUserInfo = getUserInfo,
                activity = this@ActivityRegister
            )
        }
    }
}