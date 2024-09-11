package com.hzfy.qb.ui.list

import androidx.lifecycle.viewModelScope
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.jetpack.SingleBooleanEvent
import com.hzfy.common.jetpack.SingleEvent
import com.hzfy.database.entity.QbEntity
import com.hzfy.qb.api.IQbRepository
import com.hzfy.qb.api.request.AddQbRequestParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class QbListViewModel @Inject constructor(private val qbRepository: IQbRepository) :
    BaseViewModel() {


    private val _uiState = MutableStateFlow(QbListUIState())
    val uiState: StateFlow<QbListUIState> = _uiState.asStateFlow()

    override fun onStart() {
        getQbList()
    }

    private fun getQbList() {
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.selectAll()
            }.flowOn(mSchedulerProvider.io())
                .collect { qbList ->
                    _uiState.update { it.copy(qbList = qbList) }
                }
        }
    }

    private fun checkQbState(qbList: List<QbEntity>) {
        viewModelScope.launch {
            qbList.asFlow()
                .flatMapConcat { qbEntity ->
                    qbRepository.login(
                        AddQbRequestParam(
                            qbEntity.url,
                            qbEntity.username,
                            qbEntity.password
                        )
                    ).map {
                        true
                    }.catch {
                        emit(false)
                    }
                }
                .flowOn(mSchedulerProvider.io())
                .collect { success ->

                }
        }
    }

    fun onQbItemClick(qb: QbEntity) {
        checkQbAvailable(qb){
            _uiState.update { it.copy(openQbDetailPageEvent = SingleEvent(qb)) }
        }
    }

    private fun checkQbAvailable(qb: QbEntity, success: () -> Unit) {
        viewModelScope.launch {
            qb.apply {
                qbRepository.checkQbAvailable(AddQbRequestParam(url, username, password))
                    .flowOn(mSchedulerProvider.io())
                    .collect { available ->
                        if (available) {
                            success.invoke()
                        } else {
                            updateTipMessage("qb is unAvailable")
                        }
                    }
            }
        }
    }

    fun onAddQbClick() {
        _uiState.update { it.copy(openAddQbPageEvent = SingleBooleanEvent(true)) }
    }
}
