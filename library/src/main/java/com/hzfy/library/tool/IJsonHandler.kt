package com.hzfy.library.tool

import java.lang.reflect.Type

interface IJsonHandler {
    fun toJson(obj: Any): String
    fun <T> fromJson(json: String, clazz: Class<T>): T
    fun <T> fromJson(json: String, type: Type): T
}