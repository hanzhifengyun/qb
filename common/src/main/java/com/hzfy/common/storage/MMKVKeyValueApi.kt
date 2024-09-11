package com.hzfy.common.storage

import com.tencent.mmkv.MMKV

class MMKVKeyValueApi: IKeyValueApi {
    private val mmkv: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    override fun putString(key: String, content: String?): Boolean {
        return mmkv.encode(key, content)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        if (defaultValue == null) {
            return mmkv.decodeString(key)
        }
        return mmkv.decodeString(key, defaultValue)
    }

    override fun putBoolean(key: String, content: Boolean?): Boolean {
        return mmkv.encode(key, content ?: false)
    }

    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean {
        if (defaultValue == null) {
            return mmkv.decodeBool(key)
        }
        return mmkv.decodeBool(key, defaultValue)
    }

    override fun putInt(key: String, content: Int?): Boolean {
        return mmkv.encode(key, content ?: 0)
    }

    override fun getInt(key: String, defaultValue: Int?): Int {
        if (defaultValue == null) {
            return mmkv.decodeInt(key)
        }
        return mmkv.decodeInt(key, defaultValue)
    }

    override fun putFloat(key: String, content: Float?): Boolean {
        return mmkv.encode(key, content ?: 0.0f)
    }

    override fun getFloat(key: String, defaultValue: Float?): Float {
        if (defaultValue == null) {
            return mmkv.decodeFloat(key)
        }
        return mmkv.decodeFloat(key, defaultValue)
    }

    override fun putDouble(key: String, content: Double?): Boolean {
        return mmkv.encode(key, content ?: 0.0)
    }

    override fun getDouble(key: String, defaultValue: Double?): Double {
        if (defaultValue == null) {
            return mmkv.decodeDouble(key)
        }
        return mmkv.decodeDouble(key, defaultValue)
    }

    override fun removeValueForKey(key: String) {
        mmkv.removeValueForKey(key)
    }


}