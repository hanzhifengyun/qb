package com.hzfy.common.tools

import com.google.gson.Gson
import com.hzfy.library.tool.IJsonHandler
import java.lang.reflect.Type

class GsonHandler : IJsonHandler {
    private val mGson: Gson = Gson()

    override fun toJson(obj: Any): String {
        return mGson.toJson(obj)
    }

    override fun <T> fromJson(json: String, clazz: Class<T>): T {
        return mGson.fromJson(json, clazz)
    }

    override fun <T> fromJson(json: String, type: Type): T {
        return mGson.fromJson(json, type)
    }
}