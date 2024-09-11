package com.hzfy.common.jetpack

import androidx.lifecycle.ViewModel
import com.hzfy.library.log.HzfyLog
import com.hzfy.library.net.exception.IErrorManager
import com.hzfy.library.util.scheduler.ISchedulerProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var mSchedulerProvider: ISchedulerProvider

    @Inject
    lateinit var mErrorManager: IErrorManager


    private val _baseUiState = MutableStateFlow(BaseUIState())
    val baseUiState: StateFlow<BaseUIState> = _baseUiState.asStateFlow()

    protected fun updateLoadingState(showLoading: Boolean) {
        _baseUiState.update { it.copy(showLoadingEvent = SingleBooleanEvent(showLoading)) }
    }

    protected fun updateTipMessage(message: String) {
        _baseUiState.update { it.copy(showMessageEvent = SingleMessageEvent.newEvent(message)) }
    }

    protected fun updateNetworkError(message: String = "") {
        _baseUiState.update { it.copy(showNetworkErrorEvent = SingleMessageEvent.newEvent(message)) }
    }

    protected fun closeCurrentPageEvent() {
        _baseUiState.update { it.copy(closeCurrentPageEvent = SingleBooleanEvent(true)) }
    }

    protected fun closeCurrentPageWithParamError() {
        updateTipMessage("param error")
        closeCurrentPageEvent()
    }

    abstract fun onStart()

    protected fun <T> flowWithCatchAndBaseUIState(
        onCatchError: ((Throwable) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        resource: () -> Flow<T>
    ): Flow<T> {
        return resource()
            .onStart { updateLoadingState(true) }
            .onCompletion {
                updateLoadingState(false)
            }
            .catch { error ->
                onError?.invoke(error)
                HzfyLog.e("HttpError", error.message)
                if (onCatchError != null) {
                    onCatchError.invoke(error)
                } else {
                    getErrorMessageAndTips(error)
                }
            }
    }

    protected fun getErrorMessageAndTips(error: Throwable) {
        val message = mErrorManager.getErrorMessage(error)
        updateTipMessage(message)
    }

}