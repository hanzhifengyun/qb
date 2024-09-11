package com.hzfy.common.base

import android.app.Application
import com.google.gson.Gson
import com.hzfy.library.log.HzfyConsolePrinter
import com.hzfy.library.log.HzfyLogConfig
import com.hzfy.library.log.HzfyLogManager

open class HzfyBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLog()
    }

    private fun initLog() {


        HzfyLogManager.init(object : HzfyLogConfig() {
            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }

            override fun includeThread(): Boolean {
                return true
            }
        }, HzfyConsolePrinter())
    }


}