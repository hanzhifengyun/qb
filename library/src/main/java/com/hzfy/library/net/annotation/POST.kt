package com.hzfy.library.net.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class POST(val value: String, val formPost: Boolean = true)