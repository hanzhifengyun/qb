package com.hzfy.common.http

import com.hzfy.library.util.Preconditions
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitFactory {
    fun createGsonRetrofit(baseUrl: String, okHttpClient: OkHttpClient?): Retrofit {
        return create(baseUrl, okHttpClient, GsonConverterFactory.create())
    }

    fun createStringRetrofit(baseUrl: String?, okHttpClient: OkHttpClient?): Retrofit {
        return create(
            baseUrl!!,
            okHttpClient,
            ScalarsConverterFactory.create()
        )
    }

    private fun create(
        baseUrl: String,
        okHttpClient: OkHttpClient?,
        converterFactory: Converter.Factory?
    ): Retrofit {
        Preconditions.checkNotNull(baseUrl, "baseUrl cannot be null")
        val builder = Retrofit.Builder()
        if (okHttpClient != null) {
            builder.client(okHttpClient)
        }
        builder.baseUrl(baseUrl)
        if (converterFactory != null) {
            builder.addConverterFactory(converterFactory)
        }
        return builder.build()
    }
}
