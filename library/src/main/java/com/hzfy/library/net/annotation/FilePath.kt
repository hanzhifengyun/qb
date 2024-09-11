package com.hzfy.library.net.annotation


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FilePath(val value: String)