package com.hzfy.qb.ui.detail

import com.hzfy.common.jetpack.SingleBooleanEvent
import com.hzfy.common.jetpack.SingleEvent
import com.hzfy.database.entity.QbEntity
import com.hzfy.qb.model.detail.CategoryModel
import com.hzfy.qb.model.detail.QbDetailModel
import com.hzfy.qb.model.detail.TagModel
import com.hzfy.qb.model.detail.TorrentModel

data class QbDetailUIState(

    val currentSelectedState: String = TorrentModel.STATE_ALL,
    val currentSelectedCategory: String = CategoryModel.ALL,
    val currentSelectedTag: String = TagModel.ALL,
    val qbDetailModel: QbDetailModel? = null,

    val filterTorrentList: List<TorrentModel>? = null,
    val scrollToTopEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val openAddTorrentPageEvent: SingleEvent<QbEntity?> = SingleEvent(null),

    val showOperationSuccessEvent: SingleEvent<Boolean?> = SingleEvent(null),

) {
    val needScrollToTop: Boolean
        get() = scrollToTopEvent.getContentIfNotHandledWithDefault()

    val originTorrentList: List<TorrentModel>?
        get() = qbDetailModel?.torrentList

    fun getDisplayTorrentList(): List<TorrentModel>? {
        if (filterTorrentList != null) {
            return filterTorrentList
        }
        if (qbDetailModel != null) {
            return qbDetailModel.torrentList
        }
        return null
    }
}
