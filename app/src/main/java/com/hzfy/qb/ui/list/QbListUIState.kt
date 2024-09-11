package com.hzfy.qb.ui.list

import com.hzfy.common.jetpack.SingleBooleanEvent
import com.hzfy.common.jetpack.SingleEvent
import com.hzfy.database.entity.QbEntity

data class QbListUIState(
    val qbList: List<QbEntity> = listOf(),
    val openAddQbPageEvent: SingleBooleanEvent = SingleBooleanEvent(false),
    val openQbDetailPageEvent: SingleEvent<QbEntity?> = SingleEvent<QbEntity?>(null)
) {
    val needOpenAddQbPage: Boolean
        get() = openAddQbPageEvent.getContentIfNotHandledWithDefault()
    val needOpenQbDetailPage: Boolean
        get() = openQbDetailPageEvent.getContentIfNotHandled() != null
}
