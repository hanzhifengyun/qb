package com.hzfy.ui.model

open class MenuItem<T>(private val name: String, open val value: T) : INameItem {
    override fun getItemName(): String {
        return name
    }
}
