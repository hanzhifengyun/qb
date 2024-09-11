package com.hzfy.qb.di

import android.content.Context
import com.hzfy.library.net.exception.IErrorManager
import com.hzfy.qb.http.QbErrorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QbUtilModule {

    @Singleton
    @Provides
    fun provideQbErrorManager(@ApplicationContext context: Context): IErrorManager {
        return QbErrorManager(context)
    }


}