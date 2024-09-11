package com.hzfy.qb.di

import android.content.Context
import com.hzfy.common.api.IAppApi
import com.hzfy.common.http.JsonResponseConvert
import com.hzfy.common.http.RetrofitCallFactory
import com.hzfy.common.storage.IKeyValueApi
import com.hzfy.library.net.HzfyApiConfig
import com.hzfy.library.net.HzfyRestful
import com.hzfy.library.tool.IJsonHandler
import com.hzfy.qb.QbConfig
import com.hzfy.qb.api.IQbRepository
import com.hzfy.qb.api.QbRepository
import com.hzfy.qb.api.local.IQbLocalApi
import com.hzfy.qb.api.local.QbLocalApi
import com.hzfy.qb.api.remote.IQbRemoteApi
import com.hzfy.qb.api.remote.QbRemoteApi
import com.hzfy.qb.api.remote.QbService
import com.hzfy.qb.http.QbInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QbApiModule {

    @QbRestful
    @Singleton
    @Provides
    fun provideQbRestful(
        @ApplicationContext context: Context,
        appApi: IAppApi,
        jsonHandler: IJsonHandler,
        keyValueApi: IKeyValueApi
    ): HzfyRestful {
        val baseUrl = QbConfig.BASE_URL
        val config = HzfyApiConfig(baseUrl, appApi.isDebug())
        val qbInterceptor = QbInterceptor(keyValueApi)
        val restful =
            HzfyRestful(config, RetrofitCallFactory(context, config, JsonResponseConvert(jsonHandler)))
        restful.addInterceptor(qbInterceptor)
        return restful
    }


    @Singleton
    @Provides
    fun provideQBService(@QbRestful restful: HzfyRestful): QbService {
        return restful.create(QbService::class.java)
    }

    @Singleton
    @Provides
    fun provideQBRemoteApi(remoteApi: QbRemoteApi): IQbRemoteApi {
        return remoteApi
    }

    @Singleton
    @Provides
    fun provideQbLocalApi(localApi: QbLocalApi): IQbLocalApi {
        return localApi
    }

    @Singleton
    @Provides
    fun provideQbRepository(qbRepository: QbRepository): IQbRepository {
        return qbRepository
    }


}