package com.bigbigdw.manavara.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.login.viewModels.ViewModelLogin
import com.bigbigdw.manavara.main.screen.ScreenMain
import com.bigbigdw.manavara.main.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.util.screen.BackOnPressed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()
    private val viewModelBest: ViewModelBest by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelMain.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        viewModelBest.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            ScreenMain(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                widthSizeClass = widthSizeClass
            )

            BackOnPressed()
        }
    }
}