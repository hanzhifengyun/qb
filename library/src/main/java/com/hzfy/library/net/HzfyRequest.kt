package com.hzfy.library.net

import android.net.Uri
import java.lang.reflect.Type

open class HzfyRequest {
    var httpMethod: METHOD? = null
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String?>? = null
    var domainUrl: String? = null
    var url: String? = null
    var json: String? = null
    var relativeUrl: String? = null
    var filePathKey: String? = null
    var fileName: String? = null
    var filePathList: MutableList<Uri> = mutableListOf()
    var returnType: Type? = null
    var formPost: Boolean = true


    enum class METHOD {
        GET, POST, MULTI_POST, JSON_POST
    }

    fun getRequestUrl(): String {
        if (!url.isNullOrEmpty()) {
            return url!!
        }
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must bot be null ")
        }
        if (!relativeUrl!!.startsWith("/")) {
            return domainUrl + relativeUrl
        }

        val indexOf = domainUrl!!.indexOf("/")
        return domainUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![name] = value
    }

    fun isDownloadFile(): Boolean {
        return url != null && fileName != null
    }

}