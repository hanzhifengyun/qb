package com.hzfy.qb.di

import com.hzfy.common.api.IAppApi
import com.hzfy.qb.api.AppApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppApi(api: AppApi): IAppApi {
        return api
    }
}