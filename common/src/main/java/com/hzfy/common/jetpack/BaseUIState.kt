package com.hzfy.common.jetpack

data class BaseUIState(
    val showLoadingEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val closeCurrentPageEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val showMessageEvent: SingleMessageEvent = SingleMessageEvent.NONE,
    val showNetworkErrorEvent: SingleMessageEvent = SingleMessageEvent.NONE,
) {
    val needShowMessage: Boolean
        get() = showMessageEvent.needShowMessage
    val message: String
        get() = showMessageEvent.getContentIfNotHandledWithDefault()
    val needCloseCurrentPage: Boolean
        get() = closeCurrentPageEvent.getContentIfNotHandledWithDefault()
    val showLoading: Boolean?
        get() = showLoadingEvent.getContentIfNotHandled()
    val needShowNetworkError: Boolean
        get() = showNetworkErrorEvent.needShowMessage
    val networkErrorMessage: String
        get() = showNetworkErrorEvent.getContentIfNotHandledWithDefault()
}