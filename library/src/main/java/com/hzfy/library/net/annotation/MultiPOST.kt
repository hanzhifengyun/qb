package com.hzfy.library.net.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MultiPOST(val value: String)