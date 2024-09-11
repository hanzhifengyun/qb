package com.hzfy.qb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.hzfy.common.compose.BaseComposeActivity
import com.hzfy.qb.ui.splash.SplashPage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseComposeActivity() {

    @Composable
    override fun CurrentScreen() {
        var showSplash by rememberSaveable {
            mutableStateOf(true)
        }

        if (showSplash) {
            SplashPage {
                showSplash = false
            }
        } else {
            HzfyPage()
        }
    }

}