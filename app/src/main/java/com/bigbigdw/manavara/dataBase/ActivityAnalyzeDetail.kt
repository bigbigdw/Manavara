package com.bigbigdw.manavara.dataBase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.dataBase.screen.ScreenAnalyzeDetail
import com.bigbigdw.manavara.dataBase.viewModels.ViewModelAnalyzeDetail
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityAnalyzeDetail : ComponentActivity() {

    private var title: String = ""
    private var json: String = ""
    private var platform: String = ""
    private var type: String = ""
    private var mode: String = ""
    private val viewModelAnalyzeDetail: ViewModelAnalyzeDetail by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelAnalyzeDetail.sideEffects
            .onEach { Toast.makeText(this@ActivityAnalyzeDetail, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        title = intent.getStringExtra("TITLE") ?: ""
        json = intent.getStringExtra("JSON") ?: ""
        platform = intent.getStringExtra("PLATFORM") ?: ""
        type = intent.getStringExtra("TYPE") ?: ""
        mode = intent.getStringExtra("MODE") ?: ""

        setContent {

            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            viewModelAnalyzeDetail.setInit(
                platform = platform,
                type = type,
                json = json,
                title = title,
                mode = mode
            )

            ScreenAnalyzeDetail(
                viewModelAnalyzeDetail = viewModelAnalyzeDetail,
                widthSizeClass = widthSizeClass,
            )
        }
    }
}