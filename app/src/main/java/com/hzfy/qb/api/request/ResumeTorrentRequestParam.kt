package com.hzfy.qb.api.request

import com.hzfy.database.entity.QbEntity

class ResumeTorrentRequestParam(qb: QbEntity, hashes: List<String>) : BaseTorrentParam(qb, hashes) {
}