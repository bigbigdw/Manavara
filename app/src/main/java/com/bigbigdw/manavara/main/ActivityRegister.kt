package com.bigbigdw.manavara.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.main.screen.ScreenRegisterMobile
import com.bigbigdw.manavara.main.viewModels.ViewModelLogin
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

        val UID = intent?.getStringExtra("UID") ?: return
        val EMAIL = intent?.getStringExtra("EMAIL") ?: return

        viewModelLogin.setUserInfo(UID = UID, EMAIL = EMAIL)

        setContent {
            val (getUserInfo, setUserInfo) = remember { mutableStateOf(state.userInfo) }

            val (getRange, setRange) = remember { mutableStateOf(state.platformRange) }

            ScreenRegisterMobile(
                viewModelLogin = viewModelLogin,
                setRange = setRange,
                getRange = getRange,
                setUserInfo = setUserInfo,
                getUserInfo = getUserInfo,
                activity = this@ActivityRegister
            )
        }
    }
}