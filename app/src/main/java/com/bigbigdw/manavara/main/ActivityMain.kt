package com.bigbigdw.manavara.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import checkMining
import com.bigbigdw.manavara.main.screen.ScreenMain
import com.bigbigdw.manavara.best.viewModels.ViewModelBest
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.util.DBDate
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.screen.BackOnPressed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()
    private val viewModelBest: ViewModelBest by viewModels()
    private var needDataUpdate: Boolean = false
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = DataStoreManager(this@ActivityMain)

        CoroutineScope(Dispatchers.IO).launch {
            dataStore.getDataStoreString(DataStoreManager.MINING).collect { value ->
                if (value != null) {
                    needDataUpdate = value.toFloat() >= DBDate.dateMMDDHHMM().toFloat()
                }
            }
        }

        viewModelMain.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        viewModelBest.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        checkMining(this@ActivityMain)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            ScreenMain(
                viewModelMain = viewModelMain,
                viewModelBest = viewModelBest,
                widthSizeClass = widthSizeClass,
                needDataUpdate = needDataUpdate
            )

            BackOnPressed()
        }
    }
}