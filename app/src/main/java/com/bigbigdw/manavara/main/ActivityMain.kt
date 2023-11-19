package com.bigbigdw.manavara.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import checkMining
import checkUpdateTime
import com.bigbigdw.manavara.main.screen.ScreenMain
import com.bigbigdw.manavara.main.viewModels.ViewModelMain
import com.bigbigdw.manavara.manavara.viewModels.ViewModelManavara
import com.bigbigdw.manavara.util.DataStoreManager
import com.bigbigdw.manavara.util.novelListEng
import deleteJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()
    private val viewModelManavara: ViewModelManavara by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = DataStoreManager(this@ActivityMain)

        checkMining {
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.getDataStoreString(DataStoreManager.MINING).collect { value ->
                    if (value?.isNotEmpty() == true) {

                        dataStore.setDataStoreBoolean(DataStoreManager.NEED_UPDATE, checkUpdateTime(updateTime = it, storeTime = value))

                        if(checkUpdateTime(updateTime = it, storeTime = value)){
                            dataStore.setDataStoreString(DataStoreManager.MINING, it)

                            for (platform in novelListEng()) {
                                deleteJson(
                                    context = this@ActivityMain,
                                    platform = platform,
                                    type = "NOVEL"
                                )
                            }
                        }
                    } else {
                        dataStore.setDataStoreBoolean(DataStoreManager.NEED_UPDATE, true)

                        for (platform in novelListEng()) {
                            deleteJson(
                                context = this@ActivityMain,
                                platform = platform,
                                type = "NOVEL"
                            )
                        }

                        dataStore.setDataStoreString(DataStoreManager.MINING, it)
                    }
                }
            }
        }

        viewModelMain.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        viewModelManavara.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded


            ScreenMain(
                viewModelMain = viewModelMain,
                viewModelManavara = viewModelManavara,
                isExpandedScreen = isExpandedScreen
            )
        }
    }
}