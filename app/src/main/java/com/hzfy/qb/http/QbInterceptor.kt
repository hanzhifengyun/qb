package com.hzfy.qb.http

import com.hzfy.common.storage.IKeyValueApi
import com.hzfy.library.log.HzfyLog
import com.hzfy.library.net.HzfyInterceptor
import com.hzfy.library.net.HzfyRequest
import javax.inject.Inject

class QbInterceptor @Inject constructor(private val keyValueApi: IKeyValueApi) : HzfyInterceptor {

    private fun saveCookies(key: String, headers: MutableMap<String, String>) {
        for ((name, value) in headers) {
            if ("set-cookie" == name) {
                val cookie = getCookie(value)
                HzfyLog.i("cookie = $cookie")
                cookie.let { keyValueApi.putString(key, cookie) }
                break
            }
        }
    }


    private fun getCookie(value: String): String? {
        val split = value.split(";")
        for (item in split) {
            if (item.contains("SID=")) {
                return item
            }
        }
        return null
    }

    private fun setCookies(request: HzfyRequest) {
        val baseUrl = request.domainUrl
        val cookie = baseUrl?.let { keyValueApi.getString(it) }
        if (!cookie.isNullOrEmpty()) {
            request.addHeader("Cookie", cookie)
        }
    }

    override fun intercept(chain: HzfyInterceptor.Chain): Boolean {

        val request = chain.request()

        setCookies(request)

        val response = chain.response()
        val responseHeaders = response?.headers
        responseHeaders?.let { saveCookies(request.domainUrl!!, it) }

        return false
    }
}