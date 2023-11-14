package com.bigbigdw.manavara.best

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.bigbigdw.manavara.best.screen.ScreenBestDetail
import com.bigbigdw.manavara.best.viewModels.ViewModelBestDetail
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityBestDetail : ComponentActivity() {

    private var bookCode : String = ""
    private var platform : String = ""
    private var type : String = ""
    private val viewModelBestDetail: ViewModelBestDetail by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelBestDetail.sideEffects
            .onEach { Toast.makeText(this@ActivityBestDetail, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        bookCode = intent.getStringExtra("BOOKCODE") ?: ""
        platform = intent.getStringExtra("PLATFORM") ?: ""
        type = intent.getStringExtra("TYPE") ?: ""

        setContent {

            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            ScreenBestDetail(
                viewModelBestDetail = viewModelBestDetail,
                widthSizeClass = widthSizeClass,
                bookCode = bookCode,
                platform = platform,
                type = type
            )
        }
    }
}