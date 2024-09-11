package com.hzfy.common.http

import com.hzfy.library.log.HzfyLog
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * OkHttp创建
 */
object OkHttpFactory {
    private const val TAG = "OkHttpFactory"
    private const val CONNECT_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 10L
    private const val READ_TIMEOUT = 10L

    private val MEDIA_TYPE_JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    fun create(isDebug: Boolean = false): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor { message -> HzfyLog.i(TAG, message) }
        if (isDebug) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        builder.addInterceptor(loggingInterceptor)
        val okHttpClient = builder.build()
        ignoreSSLCheck(okHttpClient)
        return okHttpClient
    }

    fun getJsonRequestBody(content: String?): RequestBody {
        var json = content
        if (json == null) {
            json = ""
        }
        HzfyLog.i(TAG, json)
        return json.toRequestBody(MEDIA_TYPE_JSON)
    }

    fun getMultipartRequestBody(params: Map<String, String>?): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder()
        builder.setType((MultipartBody.FORM))
        HzfyLog.i(TAG, "getMultipartBody: params = ")
        if (params != null) {
            for ((key, value) in params) {
                builder.addFormDataPart(key, value)
                HzfyLog.i(TAG, "key= $key and value= $value")
            }
        }
        return builder.build()
    }


    fun getFormRequestBody(params: Map<String, String>?): RequestBody {
        val builder: FormBody.Builder = FormBody.Builder()
        HzfyLog.i(TAG, "getFormBody : params = ")
        if (params != null) {
            for ((key, value) in params) {
                builder.add(key, value)
                HzfyLog.i(TAG, "key= $key and value= $value")
            }
        }
        return builder.build()
    }


    private fun ignoreSSLCheck(okHttpClient: OkHttpClient) {
        var sc: SSLContext? = null
        try {
            sc = SSLContext.getInstance("SSL")
            sc.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }
            }), SecureRandom())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val hv1 = HostnameVerifier { _, _ -> true }

        val workerClassName = "okhttp3.OkHttpClient"
        try {
            val workerClass = Class.forName(workerClassName)
            val hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier")
            hostnameVerifier.isAccessible = true
            hostnameVerifier[okHttpClient] = hv1

            val sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory")
            sslSocketFactory.isAccessible = true
            sslSocketFactory[okHttpClient] = sc!!.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
