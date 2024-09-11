package com.hzfy.qb.ui.detail

import com.hzfy.qb.model.detail.TorrentModel

data class TorrentMenuDialogUIState(
    val showTorrentMenuDialog: Boolean = false,
    val showDeleteTorrentDialog: Boolean = false,
    val showChangeTorrentSavePathDialog: Boolean = false,
    val torrent: TorrentModel? = null,
)
