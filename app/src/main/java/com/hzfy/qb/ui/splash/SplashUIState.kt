package com.hzfy.qb.ui.splash

import com.hzfy.common.jetpack.SingleBooleanEvent

data class SplashUIState(
    val countdownSeconds: Int = 3,
    val checkPermissionsEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val openHomePage: Boolean = false,
) {

    val checkPermissions: Boolean
        get() = checkPermissionsEvent.getContentIfNotHandledWithDefault()
}