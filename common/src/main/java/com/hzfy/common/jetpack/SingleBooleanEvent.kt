package com.hzfy.common.jetpack

class SingleBooleanEvent(content: Boolean): SingleEvent<Boolean>(content) {

    fun getContentIfNotHandledWithDefault(): Boolean {
        return getContentIfNotHandled() ?: false
    }
}