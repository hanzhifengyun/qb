package com.hzfy.library.net.exception

interface IErrorManager {
    fun getErrorMessage(e: Throwable): String
}