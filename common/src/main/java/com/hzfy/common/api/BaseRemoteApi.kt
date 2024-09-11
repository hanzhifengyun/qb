package com.hzfy.common.api

import com.hzfy.library.log.HzfyLog
import com.hzfy.library.net.HzfyCall
import com.hzfy.library.net.HzfyResponse
import com.hzfy.library.net.exception.UnknownErrorException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class BaseRemoteApi {

    protected open fun <T> getResult(resource: () -> HzfyCall<T>): T? {
        val response: HzfyResponse<T>?

        response = resource().execute()


        if (response.successful()) {
            HzfyLog.i("responseData: " + response.rawData)
            return response.data
        } else {
            HzfyLog.e("UnknownErrorException")
            throw UnknownErrorException("UnknownErrorException")
        }

    }


    protected open fun <T> getFlowResult(resource: () -> HzfyCall<T>): Flow<T?> {
        return flow {
            emit(getResult(resource))
        }
    }

}