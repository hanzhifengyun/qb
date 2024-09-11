package com.hzfy.library.net.exception

class HttpStatusCodeException(val statusCode: Int, message: String?) : Exception(message) {
}