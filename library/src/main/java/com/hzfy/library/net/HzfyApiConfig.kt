package com.hzfy.library.net

data class HzfyApiConfig(
    val baseUrl: String,
    val isDebug: Boolean = false,
    val logTag: String? = TAG,
    val connectTimeout: Long = CONNECT_TIMEOUT,
    val writeTimeout: Long = WRITE_TIMEOUT,
    val readTimeout: Long = READ_TIMEOUT,
) {
    companion object {
        const val TAG: String = "hzfy"
        const val CONNECT_TIMEOUT: Long = 30
        const val WRITE_TIMEOUT: Long = 30
        const val READ_TIMEOUT: Long = 30
    }
}