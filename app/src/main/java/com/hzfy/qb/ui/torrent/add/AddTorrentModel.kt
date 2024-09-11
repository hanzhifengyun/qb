package com.hzfy.qb.ui.torrent.add

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.hzfy.common.jetpack.BaseViewModel
import com.hzfy.common.model.ParamMenuItem
import com.hzfy.database.entity.QbEntity
import com.hzfy.library.ext.parseToLong
import com.hzfy.library.log.HzfyLog
import com.hzfy.qb.api.IQbRepository
import com.hzfy.qb.api.request.AddTorrentsParam
import com.hzfy.qb.api.request.BaseQbParam
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.ui.compose.PlatformFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTorrentModel @Inject constructor(private val qbRepository: IQbRepository) :
    BaseViewModel() {

    companion object {
        const val TAG = "AddTorrent"
    }

    private val _uiState = MutableStateFlow(AddTorrentUIState())
    val uiState: StateFlow<AddTorrentUIState> = _uiState.asStateFlow()
    private lateinit var currentQb: QbEntity

    override fun onStart() {
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
            }) {
                qbRepository.selectQbByUid(uid)
            }.flowOn(mSchedulerProvider.io())
                .collect { qb ->
                    if (qb == null) {
                        closeCurrentPageWithParamError()
                    } else {
                        currentQb = qb
                        setPageInfo()

                        getCategoryList()
                    }
                }
        }
    }

    private fun setPageInfo() {
        currentQb.savePath?.let {
            updateSavePath(it)
        }
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.getCategoryList(BaseQbParam(currentQb))
            }.flowOn(mSchedulerProvider.io())
                .collect { categoryList ->
                    _uiState.update { it.copy(categoryList = categoryList) }
                }
        }
    }

    fun onBtnConfirmClick() {
        uiState.value.apply {

            if (filePathList.isEmpty()) {
                updateTipMessage("please choose torrent file")
                return
            }
            if (savePath.isEmpty()) {
                updateTipMessage("please input torrent savePath")
                return
            }
            if (stopCondition == null) {
                updateTipMessage("please choose stopCondition")
                return
            }
            if (contentLayout == null) {
                updateTipMessage("please choose contentLayout")
                return
            }
            val param = AddTorrentsParam(
                currentQb,
                filePathList,
                autoTMM = !isTorrentManagerModeManualChecked,
                savePath = savePath,
                rename = rename,
                stopCondition = stopCondition,
                contentLayout = contentLayout,
                category = category,
                paused = !isTorrentStartChecked,
                downloadSpeedLimit = downloadSpeedLimit.parseToLong(),
                uploadSpeedLimit = uploadSpeedLimit.parseToLong()
            )
            addTorrent(param)
        }


    }

    private fun addTorrent(param: AddTorrentsParam) {
        viewModelScope.launch {
            flowWithCatchAndBaseUIState {
                qbRepository.addTorrents(param)
            }.flowOn(mSchedulerProvider.io())
                .collect{
                    updateTipMessage("add success")
                    closeCurrentPageEvent()
                }
        }
    }

    fun onFilesPick(files: List<PlatformFile>?) {
        closeFilePicker()
        if (files == null) {
            return
        }
        val filePathList = mutableListOf<Uri>()
        files.forEach { file->
            val path = file.uri.path
            HzfyLog.i(TAG, "filePick: $path")
            path?.let { filePathList.add(file.uri) }
        }
        if (filePathList.isNotEmpty()) {
            _uiState.update { it.copy(filePathList = filePathList) }
        }
    }

    private fun closeFilePicker() {
        _uiState.update { it.copy(showFilePicker = false) }
    }

    private fun showFilePicker() {
        _uiState.update { it.copy(showFilePicker = true) }
    }

    fun onCategoryItemClick(category: CategoryModel) {
        _uiState.update { it.copy(category = category.name ?: "") }
    }

    fun onCategoryTextChanged(text: String) {
        _uiState.update { it.copy(category = text) }
    }

    fun onTorrentManagerModeManualCheckedChanged(checked: Boolean) {
        _uiState.update { it.copy(isTorrentManagerModeManualChecked = checked) }
    }
    fun onTorrentStartCheckedChanged(checked: Boolean) {
        _uiState.update { it.copy(isTorrentStartChecked = checked) }
    }

    fun onTorrentSkipHashVerificationCheckedChanged(checked: Boolean) {
        _uiState.update { it.copy(isTorrentSkipHashVerificationChecked = checked) }
    }

    fun onTorrentDownloadInOrderCheckedChanged(checked: Boolean) {
        _uiState.update { it.copy(isTorrentDownloadInOrderChecked = checked) }
    }

    fun onTorrentDownloadFirstFileBlocksCheckedChanged(checked: Boolean) {
        _uiState.update { it.copy(isTorrentDownloadFirstFileBlocksChecked = checked) }
    }

    fun onSavePathValueChange(text: String) {
        updateSavePath(text)
    }

    private fun updateSavePath(text: String) {
        _uiState.update { it.copy(savePath = text) }
    }

    fun onRenameValueChange(text: String) {
        _uiState.update { it.copy(rename = text) }
    }

    fun onDownloadSpeedLimitValueChange(text: String) {
        _uiState.update { it.copy(downloadSpeedLimit = text) }
    }

    fun onUploadSpeedLimitValueChange(text: String) {
        _uiState.update { it.copy(uploadSpeedLimit = text) }
    }

    fun onStopConditionItemClick(item: ParamMenuItem) {
        _uiState.update { it.copy(stopCondition = item) }
    }

    fun onContentLayoutItemClick(item: ParamMenuItem) {
        _uiState.update { it.copy(contentLayout = item) }
    }

    fun onBtnChooseFileClick() {
        showFilePicker()
    }

}
