package com.hzfy.qb.ui.add

import com.hzfy.common.jetpack.SingleBooleanEvent

/**
 * Data class representing the UI state for adding a QB.
 *
 * @property url The URL input by the user.
 * @property username The username input by the user.
 * @property password The password input by the user.
 * @property urlEmptyErrorEvent Event indicating if the URL field is empty.
 * @property usernameEmptyErrorEvent Event indicating if the username field is empty.
 * @property saveQbSuccessEvent Event indicating if saving the QB was successful.
 * @property saveQbErrorEvent Event indicating if there was an error saving the QB.
 */
data class AddQbUIState(
    val url: String = "",
    val username: String = "",
    val password: String = "",
    val urlEmptyErrorEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val usernameEmptyErrorEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val saveQbSuccessEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val saveQbErrorEvent: SingleBooleanEvent = SingleBooleanEvent(false),
) {
    /**
     * Indicates if the URL field is empty.
     */
    val urlEmptyError: Boolean
        get() = urlEmptyErrorEvent.getContent()

    /**
     * Indicates if the username field is empty.
     */
    val usernameEmptyError: Boolean
        get() = usernameEmptyErrorEvent.getContent()

    /**
     * Indicates if saving the QB was successful.
     */
    val saveQbSuccess: Boolean
        get() = saveQbSuccessEvent.getContentIfNotHandledWithDefault()

    /**
     * Indicates if there was an error saving the QB.
     */
    val saveQbError: Boolean
        get() = saveQbErrorEvent.getContentIfNotHandledWithDefault()
}
