package com.hzfy.library.net.exception

class RemoteServiceException(val code: Int = -1, message: String?) : Exception(message) {
}