package com.hzfy.library.net

open class HzfyResponse<T> {
    companion object {
        const val CODE_SUCCESS: Int = 200
    }

    var headers: MutableMap<String, String>? = null
    var rawData: String? = null//原始数据
    var code: Int? = null//状态码
    var data: T? = null//业务数据

    var msg: String? = null//错误信息


    open fun successful(): Boolean {
        return code in 200..299
    }

}