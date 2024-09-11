package com.hzfy.qb.ui.torrent.add

import android.net.Uri
import com.hzfy.common.model.ParamMenuItem
import com.hzfy.qb.model.detail.CategoryModel

data class AddTorrentUIState(
    val filePathList: List<Uri> = listOf(),

    val showFilePicker: Boolean = false,
    val category: String = "",
    val savePath: String = "",
    val rename: String = "",
    val stopCondition: ParamMenuItem? = null,
    val contentLayout: ParamMenuItem? = null,
    val downloadSpeedLimit: String = "",
    val uploadSpeedLimit: String = "",
    val categoryList: List<CategoryModel> = listOf(),
    val isTorrentManagerModeManualChecked: Boolean = true,
    val isTorrentStartChecked: Boolean = true,
    val isTorrentSkipHashVerificationChecked: Boolean = false,
    val isTorrentDownloadInOrderChecked: Boolean = false,
    val isTorrentDownloadFirstFileBlocksChecked: Boolean = false,
)
