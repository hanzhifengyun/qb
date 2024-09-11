package com.hzfy.qb.util

import com.hzfy.qb.R
import com.hzfy.qb.model.detail.TorrentModel

object QbUtil {

    fun getTorrentStateResId(state: String): Int {
        return when (state) {
            TorrentModel.STATE_ALL -> {
                R.string.qb_torrent_state_all
            }

            TorrentModel.STATE_ERROR -> {
                R.string.qb_torrent_state_error
            }

            TorrentModel.STATE_MISSING_FILES -> {
                R.string.qb_torrent_state_missing_files
            }

            TorrentModel.STATE_UPLOADING -> {
                R.string.qb_torrent_state_uploading
            }

            TorrentModel.STATE_PAUSED_UP -> {
                R.string.qb_torrent_state_paused_up
            }

            TorrentModel.STATE_QUEUED_UP -> {
                R.string.qb_torrent_state_queued_up
            }

            TorrentModel.STATE_STALLED_UP -> {
                R.string.qb_torrent_state_stalled_up
            }

            TorrentModel.STATE_CHECKING_UP -> {
                R.string.qb_torrent_state_checking_up
            }

            TorrentModel.STATE_FORCED_UP -> {
                R.string.qb_torrent_state_forced_up
            }

            TorrentModel.STATE_ALLOCATING -> {
                R.string.qb_torrent_state_allocating
            }

            TorrentModel.STATE_DOWNLOADING -> {
                R.string.qb_torrent_state_downloading
            }

            TorrentModel.STATE_META_DL -> {
                R.string.qb_torrent_state_meta_dl
            }

            TorrentModel.STATE_PAUSED_DL -> {
                R.string.qb_torrent_state_paused_dl
            }

            TorrentModel.STATE_QUEUED_DL -> {
                R.string.qb_torrent_state_queued_dl
            }

            TorrentModel.STATE_STALLED_DL -> {
                R.string.qb_torrent_state_stalled_dl
            }

            TorrentModel.STATE_CHECKING_DL -> {
                R.string.qb_torrent_state_checking_dl
            }

            TorrentModel.STATE_FORCED_DL -> {
                R.string.qb_torrent_state_forced_dl
            }

            TorrentModel.STATE_CHECKING_RESUME_DATA -> {
                R.string.qb_torrent_state_checking_resume_data
            }

            TorrentModel.STATE_MOVING -> {
                R.string.qb_torrent_state_moving
            }

            else -> {
                R.string.qb_torrent_state_unknown
            }
        }
    }

}