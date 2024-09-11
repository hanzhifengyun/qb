package com.hzfy.common.jetpack

class SingleEmptyEvent private constructor(content: Boolean?) : SingleEvent<Boolean?>(content) {

    fun isNewEvent(): Boolean {
        return getContentIfNotHandled() ?: false
    }

    companion object {
        val INIT = SingleEmptyEvent(null)
        val NEW = SingleEmptyEvent(true)
    }
}