package com.hzfy.library.net.annotation

import java.lang.reflect.ParameterizedType

fun main() {

    val sss: List<String> = mutableListOf("123", "456")

    val result = sss.javaClass.genericSuperclass
    val type = (result as ParameterizedType).actualTypeArguments[0]
    println(type)
}