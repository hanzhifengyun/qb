package com.hzfy.qb.ui.add

import androidx.lifecycle.viewModelScope
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.jetpack.SingleBooleanEvent
import com.hzfy.library.log.HzfyLog
import com.hzfy.qb.api.IQbRepository
import com.hzfy.qb.api.request.AddQbRequestParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQbViewModel @Inject constructor(private val qbRepository: IQbRepository) :
    BaseViewModel() {

    companion object {
        const val TAG = "AddQb"
    }

    private val _uiState = MutableStateFlow(AddQbUIState())
    val uiState: StateFlow<AddQbUIState> = _uiState.asStateFlow()


    override fun onStart() {

    }

    fun onEditUrlTextChanged(text: String) {
        if (uiState.value.urlEmptyError && text.isNotEmpty()) {
            _uiState.update { it.copy(urlEmptyErrorEvent = SingleBooleanEvent(false)) }
        }
        _uiState.update { it.copy(url = text) }
    }

    fun onEditUsernameTextChanged(text: String) {
        if (uiState.value.usernameEmptyError && text.isNotEmpty()) {
            _uiState.update { it.copy(usernameEmptyErrorEvent = SingleBooleanEvent(false)) }
        }
        _uiState.update { it.copy(username = text) }
    }


    fun onEditPasswordTextChanged(text: String) {
        _uiState.update { it.copy(password = text) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onBtnConfirmClick() {
        var url = uiState.value.url
        val username = uiState.value.username
        val password = uiState.value.password
        if (url.isEmpty()) {
            _uiState.update { it.copy(urlEmptyErrorEvent = SingleBooleanEvent(true)) }
            return
        }
        if (!url.endsWith("/")) {
            url += "/"
        }
        if (username.isEmpty()) {
            _uiState.update { it.copy(usernameEmptyErrorEvent = SingleBooleanEvent(true)) }
            return
        }
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.login(AddQbRequestParam(url, username, password))
            }.flatMapConcat {
                qbRepository.saveQb(it)
            }.flowOn(mSchedulerProvider.io())
                .collect { saveSuccess ->
                    HzfyLog.i(TAG, "saveSuccess = $saveSuccess")
                    if (saveSuccess) {
                        _uiState.update {
                            it.copy(
                                saveQbSuccessEvent = SingleBooleanEvent(true),
                            )
                        }
                        closeCurrentPageEvent()
                    } else {
                        _uiState.update {
                            it.copy(
                                saveQbErrorEvent = SingleBooleanEvent(true)
                            )
                        }
                    }
                }
        }

    }


}
