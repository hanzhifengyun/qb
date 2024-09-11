package com.hzfy.qb

import com.hzfy.common.base.HzfyBaseApplication
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : HzfyBaseApplication() {
    override fun onCreate() {
        super.onCreate()


        initMMKV()
    }

    private fun initMMKV() {
        MMKV.initialize(this);
    }

}