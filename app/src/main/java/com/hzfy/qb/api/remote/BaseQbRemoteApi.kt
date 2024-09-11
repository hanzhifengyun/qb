package com.hzfy.qb.api.remote

import com.hzfy.common.api.BaseRemoteApi
import com.hzfy.library.net.HzfyCall
import com.hzfy.library.net.exception.RemoteServiceException
import com.hzfy.qb.api.result.QbResult

open class BaseQbRemoteApi : BaseRemoteApi() {

    override fun <T> getResult(resource: () -> HzfyCall<T>): T? {
        val result = super.getResult(resource)
        if (result is String) {
            if (result == QbResult.QB_ERROR) {
                throw RemoteServiceException(code = QbResult.CODE_QB_ERROR, message = result)
            }
        }
        return result

    }
}