package com.hzfy.qb.api.request

import com.hzfy.database.entity.QbEntity

class ChangeSavePathTorrentRequestParam(qb: QbEntity, hashes: List<String>, val newPath: String): BaseTorrentParam(qb, hashes) {

}