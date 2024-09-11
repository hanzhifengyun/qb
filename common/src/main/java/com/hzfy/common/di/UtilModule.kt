package com.hzfy.common.di

import com.hzfy.common.storage.IKeyValueApi
import com.hzfy.common.storage.MMKVKeyValueApi
import com.hzfy.common.tools.GsonHandler
import com.hzfy.library.tool.IJsonHandler
import com.hzfy.library.util.scheduler.ISchedulerProvider
import com.hzfy.library.util.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {

    @Singleton
    @Provides
    fun provideKeyValueApi(): IKeyValueApi {
        return MMKVKeyValueApi()
    }

    @Singleton
    @Provides
    fun provideJsonHandler(): IJsonHandler {
        return GsonHandler()
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): ISchedulerProvider {
        return SchedulerProvider()
    }
}