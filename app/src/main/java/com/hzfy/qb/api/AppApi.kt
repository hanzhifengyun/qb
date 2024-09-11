package com.hzfy.qb.api

import com.hzfy.common.api.IAppApi
import com.hzfy.common.storage.IKeyValueApi
import com.hzfy.qb.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApi @Inject constructor()  : IAppApi {

    companion object{
        const val TAG = "AppApi"
    }

    @Inject
    lateinit var mKeyValueApi: IKeyValueApi

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}