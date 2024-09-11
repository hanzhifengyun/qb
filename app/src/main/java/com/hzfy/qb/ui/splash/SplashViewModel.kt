package com.hzfy.qb.ui.splash

import androidx.lifecycle.viewModelScope
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.jetpack.SingleBooleanEvent
import com.hzfy.library.ext.flowCountdownSeconds
import com.hzfy.library.log.HzfyLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {


    private val _uiState = MutableStateFlow(SplashUIState())
    val uiState: StateFlow<SplashUIState> = _uiState.asStateFlow()

    private var job: Job? = null

    private var onPermissionAllGranted: Boolean = false

    companion object {
        const val TAG = "Splash"
        private const val COUNTDOWN_SECONDS = 4
    }

    override fun onStart() {
        checkPermissions()
    }

    private fun checkPermissions() {
        _uiState.update { it.copy(checkPermissionsEvent = SingleBooleanEvent(true)) }
    }

    @Synchronized
    private fun openHomePage() {
        _uiState.update { it.copy(openHomePage = true) }
    }

    fun onJumpClick() {
        job?.cancel()
        openHomePage()
    }

    fun onPermissionAllGranted() {
        if (onPermissionAllGranted) {
            return
        }
        onPermissionAllGranted = true
        HzfyLog.i(TAG, "onPermissionAllGranted")
        job = viewModelScope.launch {
            flowCountdownSeconds(COUNTDOWN_SECONDS)
                .flowOn(mSchedulerProvider.default())
                .onCompletion {
                    openHomePage()
                }
                .collect { count ->
                    HzfyLog.i(TAG, "count = $count")
                    _uiState.update { it.copy(countdownSeconds = (COUNTDOWN_SECONDS - count.toInt() - 1)) }
                }
        }

    }
}
