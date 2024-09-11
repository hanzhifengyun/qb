package com.hzfy.qb.api.request

import com.hzfy.database.entity.QbEntity

open class BaseTorrentParam(qb: QbEntity, val hashes: List<String>) : BaseQbParam(qb) {


    fun getHashesParam(): String {
        return hashes.joinToString("|")
    }
}