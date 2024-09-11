package com.hzfy.common.storage

interface IKeyValueApi {

    fun putString(key: String, content: String?): Boolean
    fun getString(key: String, defaultValue: String? = null): String?

    fun putBoolean(key: String, content: Boolean?): Boolean
    fun getBoolean(key: String, defaultValue: Boolean? = null): Boolean?

    fun putInt(key: String, content: Int?): Boolean
    fun getInt(key: String, defaultValue: Int? = null): Int?

    fun putFloat(key: String, content: Float?): Boolean
    fun getFloat(key: String, defaultValue: Float? = null): Float?

    fun putDouble(key: String, content: Double?): Boolean
    fun getDouble(key: String, defaultValue: Double? = null): Double?

    fun removeValueForKey(key: String)
}