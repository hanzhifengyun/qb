package com.hzfy.qb.ui.detail

import androidx.lifecycle.viewModelScope
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.jetpack.SingleBooleanEvent
import com.hzfy.common.jetpack.SingleEvent
import com.hzfy.database.entity.QbEntity
import com.hzfy.library.ext.flowInterval
import com.hzfy.qb.api.IQbRepository
import com.hzfy.qb.api.request.ChangeSavePathTorrentRequestParam
import com.hzfy.qb.api.request.DeleteTorrentRequestParam
import com.hzfy.qb.api.request.PauseTorrentRequestParam
import com.hzfy.qb.api.request.QbDetailRequestParam
import com.hzfy.qb.api.request.ResumeTorrentRequestParam
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.qb.model.detail.QbDetailModel
import com.hzfy.qb.model.detail.StateModel
import com.hzfy.qb.model.detail.TagModel
import com.hzfy.qb.model.detail.TorrentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class QbDetailViewModel @Inject constructor(private val qbRepository: IQbRepository) :
    BaseViewModel() {


    private val _uiState = MutableStateFlow(QbDetailUIState())
    val uiState: StateFlow<QbDetailUIState> = _uiState.asStateFlow()

    private val _torrentMenuDialogUiState = MutableStateFlow(TorrentMenuDialogUIState())
    val torrentMenuDialogUiState: StateFlow<TorrentMenuDialogUIState> =
        _torrentMenuDialogUiState.asStateFlow()

    private lateinit var qbDetailRequestParam: QbDetailRequestParam
    private lateinit var currentQb: QbEntity


    override fun onStart() {
        //do nothing
    }

    fun onStart(uid: Int?) {
        if (uid == null) {
            closeCurrentPageWithParamError()
            return
        }
        viewModelScope.launch {
            flowWithCatchAndBaseUIState(onCatchError = {
                updateNetworkError()
                closeCurrentPageEvent()
                true
            }) {
                qbRepository.selectQbByUid(uid)
            }.flatMapConcat { qb ->
                if (qb == null) {
                    flowOf(null)
                } else {
                    currentQb = qb
                    qbDetailRequestParam = QbDetailRequestParam(qb = qb)
                    qbRepository.getQbDetail(qbDetailRequestParam)
                }
            }.flowOn(mSchedulerProvider.io())
                .collect { qbDetail ->
                    updateDetailInfo(qbDetail)
                    if (qbDetail != null) {
                        updateDetailData()
                    }
                }
        }
    }

    private fun updateDetailData() {
        viewModelScope.launch {
            flowInterval(2L, 2L, TimeUnit.SECONDS)
                .flowOn(mSchedulerProvider.default())
                .flatMapConcat {
                    qbRepository.getQbDetail(qbDetailRequestParam)
                }
                .map { qbDetail ->
                    uiState.value.qbDetailModel?.update(qbDetail)
                }
                .flowOn(mSchedulerProvider.io())
                .collect { qbDetail ->
                    updateDetailInfo(qbDetail, true)
                }
        }
    }


    private fun updateDetailInfo(qbDetail: QbDetailModel?, isUpdate: Boolean = false) {
        if (qbDetail == null) {
            closeCurrentPageWithParamError()
        } else {
            qbDetailRequestParam.rid = qbDetail.rid
            _uiState.update { it.copy(qbDetailModel = qbDetail) }
            if (isUpdate) {
                filterTorrentList()
            }
        }
    }


    fun onAddTorrentClick() {
        _uiState.update {
            it.copy(openAddTorrentPageEvent = SingleEvent(currentQb))
        }
    }

    fun onItemTorrentClick(torrent: TorrentModel) {

    }

    fun onItemTorrentLongClick(torrent: TorrentModel) {
        showTorrentMenuDialog(torrent)
    }

    private fun showTorrentMenuDialog(torrent: TorrentModel) {
        _torrentMenuDialogUiState.update {
            it.copy(
                showTorrentMenuDialog = true,
                torrent = torrent
            )
        }
    }

    fun onStateItemClick(item: StateModel) {
        val currentSelectedState = uiState.value.currentSelectedState
        if (item.state == currentSelectedState) {
            return
        }
        _uiState.update { it.copy(currentSelectedState = item.state) }

        filterTorrentList()
    }

    fun onCategoryItemClick(item: CategoryModel) {
        val currentSelectedCategory = uiState.value.currentSelectedCategory
        if (item.name == currentSelectedCategory) {
            return
        }
        _uiState.update { it.copy(currentSelectedCategory = item.name!!) }

        filterTorrentList()
    }

    fun onTagItemClick(item: TagModel) {
        val currentSelectedTag = uiState.value.currentSelectedTag
        if (item.name == currentSelectedTag) {
            return
        }
        _uiState.update { it.copy(currentSelectedTag = item.name) }
        filterTorrentList()
    }

    private fun filterTorrentList(isRefresh: Boolean = false) {
        val currentSelectedState = uiState.value.currentSelectedState
        val currentSelectedCategory = uiState.value.currentSelectedCategory
        val currentSelectedTag = uiState.value.currentSelectedTag
        //过滤状态列表
        val torrentList = uiState.value.originTorrentList
        if (torrentList.isNullOrEmpty()) {
            return
        }
        val isStateAll = currentSelectedState == TorrentModel.STATE_ALL
        val isCategoryAll = currentSelectedCategory == CategoryModel.ALL
        val isTagAll = currentSelectedTag == TagModel.ALL
        if (isStateAll && isCategoryAll && isTagAll) {
            _uiState.update {
                it.copy(
                    filterTorrentList = null,
                    scrollToTopEvent = SingleBooleanEvent(isRefresh)
                )
            }
            return
        }
        viewModelScope.launch {
            val filterTorrentList = torrentList.asFlow()
                .filter { item ->
                    val isStateOk = item.isStateAvailable(currentSelectedState)
                    val isCategoryOk = item.isCategoryAvailable(currentSelectedCategory)
                    val isTagOk = item.isTagAvailable(currentSelectedTag)
                    isStateOk && isCategoryOk && isTagOk
                }
                .flowOn(mSchedulerProvider.default())
                .toList()
            _uiState.update {
                it.copy(
                    filterTorrentList = filterTorrentList,
                    scrollToTopEvent = SingleBooleanEvent(isRefresh)
                )
            }
        }

    }

    fun onTorrentMenuDialogPauseClick(item: TorrentModel) {
        hideTorrentMenuDialog()
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.pauseTorrent(
                    PauseTorrentRequestParam(
                        qb = currentQb,
                        hashes = listOf(item.hash)
                    )
                )
            }.flowOn(mSchedulerProvider.io())
                .collect { success ->
                    if (success) {
                        showOperationSuccess()
                    } else {
                        showOperationFailure()
                    }
                }
        }
    }

    private fun showOperationFailure() {
        _uiState.update { it.copy(showOperationSuccessEvent = SingleEvent(false)) }
    }

    private fun showOperationSuccess() {
        _uiState.update { it.copy(showOperationSuccessEvent = SingleEvent(true)) }
    }

    fun onTorrentMenuDialogResumeClick(item: TorrentModel) {
        hideTorrentMenuDialog()
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.resumeTorrent(
                    ResumeTorrentRequestParam(
                        qb = currentQb,
                        hashes = listOf(item.hash)
                    )
                )
            }.flowOn(mSchedulerProvider.io())
                .collect { success ->
                    if (success) {
                        showOperationSuccess()
                    } else {
                        showOperationFailure()
                    }
                }
        }
    }

    fun onTorrentMenuDialogDeleteClick(torrent: TorrentModel) {
        hideTorrentMenuDialog()
        showDeleteTorrentDialog(torrent)
    }

    private fun deleteTorrent(torrent: TorrentModel, deleteFiles: Boolean) {
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.deleteTorrent(
                    DeleteTorrentRequestParam(
                        qb = currentQb,
                        hash = torrent.hash,
                        deleteFiles = deleteFiles
                    )
                )
            }.flowOn(mSchedulerProvider.io())
                .onCompletion {
                    hideDeleteTorrentDialog()
                }
                .collect { success ->
                    if (success) {
                        showOperationSuccess()
                    } else {
                        showOperationFailure()
                    }
                }
        }
    }

    fun onTorrentMenuDialogChangeSavePathClick(torrent: TorrentModel) {
        hideTorrentMenuDialog()
        showChangeTorrentSavePathDialog(torrent)
    }


    private fun changeTorrentSavePath(torrent: TorrentModel, newPath: String) {
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.changeSavePathTorrent(
                    ChangeSavePathTorrentRequestParam(
                        qb = currentQb,
                        hashes = listOf(torrent.hash),
                        newPath = newPath
                    )
                )
            }.flowOn(mSchedulerProvider.io())
                .onCompletion {
                    hideChangeTorrentSavePathDialog()
                }
                .collect { success ->
                    if (success) {
                        showOperationSuccess()
                    } else {
                        showOperationFailure()
                    }
                }
        }
    }

    fun onTorrentMenuDialogDismiss() {
        hideTorrentMenuDialog()
    }

    private fun hideTorrentMenuDialog() {
        _torrentMenuDialogUiState.update { it.copy(showTorrentMenuDialog = false) }
    }

    fun onDeleteTorrentDialogDismiss() {
        hideDeleteTorrentDialog()
    }

    private fun showDeleteTorrentDialog(torrent: TorrentModel) {
        _torrentMenuDialogUiState.update {
            it.copy(
                showDeleteTorrentDialog = true,
                torrent = torrent
            )
        }
    }

    private fun hideDeleteTorrentDialog() {
        _torrentMenuDialogUiState.update { it.copy(showDeleteTorrentDialog = false) }
    }

    fun onDeleteTorrentConfirmClick(torrent: TorrentModel, deleteFiles: Boolean) {
        deleteTorrent(torrent, deleteFiles)
    }

    fun onChangeTorrentSavePathDialogDismiss() {
        hideChangeTorrentSavePathDialog()
    }

    private fun hideChangeTorrentSavePathDialog() {
        _torrentMenuDialogUiState.update { it.copy(showChangeTorrentSavePathDialog = false) }
    }

    private fun showChangeTorrentSavePathDialog(torrent: TorrentModel) {
        _torrentMenuDialogUiState.update {
            it.copy(
                showChangeTorrentSavePathDialog = true,
                torrent = torrent
            )
        }
    }

    fun onChangeTorrentSavePathConfirmClick(torrent: TorrentModel, path: String) {
        changeTorrentSavePath(torrent, path)
    }
}
