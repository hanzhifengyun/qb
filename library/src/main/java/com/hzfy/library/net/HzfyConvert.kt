package com.hzfy.library.net

interface HzfyConvert<T> {
    fun <R> convert(data: T, request: HzfyRequest): HzfyResponse<R>
}