package com.bigbigdw.manavara.analyze

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.analyze.screen.ScreenAnalyzeDetail
import com.bigbigdw.manavara.analyze.viewModels.ViewModelAnalyzeDetail
import com.bigbigdw.manavara.best.getBookMap
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityGenreDetail : ComponentActivity() {

    private var title: String = ""
    private var json: String = ""
    private var platform: String = ""
    private var type: String = ""
    private val viewModelAnalyzeDetail: ViewModelAnalyzeDetail by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelAnalyzeDetail.sideEffects
            .onEach { Toast.makeText(this@ActivityGenreDetail, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        title = intent.getStringExtra("TITLE") ?: ""
        json = intent.getStringExtra("JSON") ?: ""
        platform = intent.getStringExtra("PLATFORM") ?: ""
        type = intent.getStringExtra("TYPE") ?: ""

        setContent {

            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            viewModelAnalyzeDetail.setInit(
                platform = platform,
                type = type,
                json = json,
                title = title
            )

            ScreenAnalyzeDetail(
                viewModelAnalyzeDetail = viewModelAnalyzeDetail,
                widthSizeClass = widthSizeClass,
            )
        }
    }
}