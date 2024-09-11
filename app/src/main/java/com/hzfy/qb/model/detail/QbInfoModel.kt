package com.hzfy.qb.model.detail

data class QbInfoModel(
    val url: String,
    val username: String,
    val password: String,
    val appVersion: String,

) {
    var totalDownloadSize: Long? = null//总下载量
    var totalDownloadSpeed: Long? = null//总下载速度
    var totalUploadSize: Long? = null//总上传量
    var totalUploadSpeed: Long? = null//总上传速度
    var freeDiskSpace: Long? = null//剩余磁盘空间
    fun update(qbInfo: QbInfoModel) {
        qbInfo.totalDownloadSize?.let { totalDownloadSize = it }
        qbInfo.totalDownloadSpeed?.let { totalDownloadSpeed = it }
        qbInfo.totalUploadSize?.let { totalUploadSize = it }
        qbInfo.totalUploadSpeed?.let { totalUploadSpeed = it }
        qbInfo.freeDiskSpace?.let { freeDiskSpace = it }
    }
}