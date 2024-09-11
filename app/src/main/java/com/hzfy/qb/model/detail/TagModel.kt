package com.hzfy.qb.model.detail


data class TagModel(
    val name: String,
    var count: Int = 0,
) {
    fun addCount(changeCount: Int) {
        count += changeCount
    }

    companion object {
        const val ALL: String = "TagModel_ALL"
        const val NONE: String = "TagModel_NONE"
    }
}