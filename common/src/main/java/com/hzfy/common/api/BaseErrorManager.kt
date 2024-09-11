package com.hzfy.common.api

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.hzfy.library.net.exception.HttpStatusCodeException
import com.hzfy.library.net.exception.IErrorManager
import com.hzfy.library.net.exception.RemoteServiceException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseErrorManager(val context: Context) : IErrorManager {

    override fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is UnknownHostException -> { //网络异常
                "当前无网络，请检查你的网络设置"
            }

            is SocketTimeoutException, is TimeoutCancellationException -> {
                "连接超时,请稍后再试"
            }

            is ConnectException -> {
                "网络不给力，请稍候重试！"
            }

            is HttpStatusCodeException -> {               //请求失败异常
                getHttpStatusCodeExceptionMessage(e)
            }

            is JsonSyntaxException -> {  //请求成功，但Json语法异常,导致解析失败
                "数据解析失败,请检查数据是否正确"
            }

            is RemoteServiceException -> {       // 表明请求成功，但是数据不正确
                getRemoteServiceExceptionMessage(e)
            }

            else -> {
                "请求失败，请稍后再试"
            }
        }
    }

    protected open fun getHttpStatusCodeExceptionMessage(e: HttpStatusCodeException) = "Http状态码异常: ${e.statusCode}"
    protected open fun getRemoteServiceExceptionMessage(e: RemoteServiceException) = e.message ?: e.code.toString()
    protected open fun getUnknownExceptionMessage(e: Exception) = "请求失败，请稍后再试"


}