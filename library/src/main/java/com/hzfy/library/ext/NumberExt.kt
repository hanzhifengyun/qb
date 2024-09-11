package com.hzfy.library.ext

fun String.parseToLong(defaultValue: Long = 0): Long {
    return try {
        java.lang.Long.parseLong(this)
    } catch (e: NumberFormatException) {
        defaultValue
    }
}