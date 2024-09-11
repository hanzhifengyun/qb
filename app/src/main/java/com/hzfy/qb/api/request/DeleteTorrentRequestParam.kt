package com.hzfy.qb.api.request

import com.hzfy.database.entity.QbEntity

class DeleteTorrentRequestParam(qb: QbEntity, hashes: List<String>, val deleteFiles: Boolean) :
    BaseTorrentParam(qb, hashes) {
    constructor(qb: QbEntity, hash: String, deleteFiles: Boolean) : this(
        qb = qb,
        hashes = listOf(hash),
        deleteFiles = deleteFiles
    )
}