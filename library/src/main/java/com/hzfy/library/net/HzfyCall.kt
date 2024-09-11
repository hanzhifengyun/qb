package com.hzfy.library.net

import java.io.IOException

interface HzfyCall<T> {

    @Throws(IOException::class)
    fun execute(): HzfyResponse<T>

    fun enqueue(callback: HzfyCallback<T>)


    interface Factory {

        fun newCall(request: HzfyRequest): HzfyCall<*>
    }
}