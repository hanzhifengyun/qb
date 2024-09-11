package com.hzfy.qb.model.detail

data class StateModel(
    val state: String,
    var count: Int = 0
) {
    fun addCount() {
        count++
    }
}
