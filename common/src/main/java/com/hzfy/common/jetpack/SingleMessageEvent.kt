package com.hzfy.common.jetpack

class SingleMessageEvent private constructor(private val code: Int, private val content: String): SingleEvent<String>(content = content) {

    val needShowMessage: Boolean
        get() = code == CODE_NEW && !hasBeenHandled

    fun getContentIfNotHandledWithDefault(): String {
        return getContentIfNotHandled() ?: ""
    }

    companion object {
        private const val CODE_NONE = 0
        private const val CODE_NEW = 1

        val NONE = SingleMessageEvent(code = CODE_NONE, content = "")

        fun newEvent(message: String): SingleMessageEvent {
            return SingleMessageEvent(code = CODE_NEW, content = message)
        }
    }
}