package com.hzfy.qb.model.detail

import com.hzfy.ui.model.INameItem


data class CategoryModel(
    val name: String? = null,
    var count: Int = 0,
    val savePath: String = ""
) : INameItem {
    fun addCount(changeCount: Int) {
        count += changeCount
    }

    companion object {

        const val ALL: String = "CategoryModel_All"
        const val NONE: String = "CategoryModel_NONE"
    }

    override fun getItemName() = name ?: ""
}