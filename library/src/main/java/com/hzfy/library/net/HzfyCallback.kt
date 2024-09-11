package com.hzfy.library.net

interface HzfyCallback<T> {
    fun onSuccess(response: HzfyResponse<T>)
    fun onFailure(throwable: Throwable)
}