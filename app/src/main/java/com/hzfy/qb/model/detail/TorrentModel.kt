package com.hzfy.qb.model.detail


data class TorrentModel(
    val hash: String = "", //种子唯一标识hash
    var name: String? = null,//名称
    var state: String? = null,//状态
    var category: String? = null,//分类
    var tags: String? = null,//标签
    var savePath: String? = null,//保存路径
    var totalSize: Long? = null,//文件大小
    var lastSize: Long? = null,//剩余文件大小
    var selectedSize: Long? = null,//选定文件大小
    var progress: Float? = null,//下载进度
    var ratio: Double? = null,//比率
    var createTime: Long? = null,//创建时间
    var downloadSpeed: Long? = null,//下载速度 B/s
    var uploadSpeed: Long? = null,//上传速度 B/s
    var completeNumber: Long? = null,//完成人数
    var incompleteNumber: Long? = null,//未完成人数
    var tracker: String? = null,//tracker
) {

    fun getRealCategory(): String {
        return if (category.isNullOrEmpty()) {
            CategoryModel.NONE
        } else {
            category!!
        }
    }


    fun update(item: TorrentModel) {
        item.name?.let { name = it }
        item.state?.let { state = it }
        item.category?.let { category = it }
        item.tags?.let {
            tags = it
            tagList = item.tagList
        }
        item.savePath?.let { savePath = it }
        item.totalSize?.let { totalSize = it }
        item.lastSize?.let { lastSize = it }
        item.selectedSize?.let { selectedSize = it }
        item.progress?.let { progress = it }
        item.ratio?.let { ratio = it }
        item.createTime?.let { createTime = it }
        item.downloadSpeed?.let { downloadSpeed = it }
        item.uploadSpeed?.let { uploadSpeed = it }
        item.completeNumber?.let { completeNumber = it }
        item.incompleteNumber?.let { incompleteNumber = it }
        item.tracker?.let { tracker = it }
    }


    var tagList: MutableList<String> = mutableListOf()//标签


    fun getTagText(): String {
        if (tagList.isEmpty()) {
            return ""
        } else {
            var text = ""
            tagList.forEachIndexed { index, tag ->
                if (index > 0) {
                    text += "，"
                }
                text += tag
            }
            return text
        }
    }


    private fun containsTag(tag: String): Boolean {
        tagList.forEach {
            if (it == tag) {
                return true
            }
        }
        return false
    }

    fun isStateAvailable(currentSelectedState: String): Boolean {
        if (currentSelectedState == STATE_ALL) {
            return true
        }
        return currentSelectedState == state
    }

    fun isCategoryAvailable(currentSelectedCategory: String): Boolean {
        if (currentSelectedCategory == CategoryModel.ALL) {
            return true
        }
        if (currentSelectedCategory == CategoryModel.NONE) {
            return category.isNullOrEmpty()
        }
        return currentSelectedCategory == category
    }

    fun isTagAvailable(currentSelectedTag: String): Boolean {
        if (currentSelectedTag == TagModel.ALL) {
            return true
        }
        if (currentSelectedTag == TagModel.NONE) {
            return tagList.isEmpty()
        }
        return containsTag(currentSelectedTag)
    }


    val lastTime: Long //剩余下载时间 s
        get() {
            if (downloadSpeed == null || lastSize == null) {
                return 0L
            }
            return if (downloadSpeed!! > 0 && lastSize!! > 0) {
                lastSize!! / downloadSpeed!!
            } else {
                0L
            }
        }

    companion object {
        const val STATE_ALL = "all"//全部
        const val STATE_ERROR = "error"//错误种子 Some error occurred, applies to paused torrents
        const val STATE_MISSING_FILES = "missingFiles"//数据文件丢失 Torrent data files is missing
        const val STATE_PAUSED_UP = "pausedUP"//暂停上传 Torrent is paused and has finished downloading
        const val STATE_QUEUED_UP =
            "queuedUP"//排队等待上传 Queuing is enabled and torrent is queued for upload
        const val STATE_UPLOADING =
            "uploading"//正在上传中  Torrent is being seeded and data is being transferred
        const val STATE_STALLED_UP =
            "stalledUP"//做种中，但未建立连接 Torrent is being seeded, but no connection were made
        const val STATE_CHECKING_UP =
            "checkingUP"//种子已完成下载，检查中 Torrent has finished downloading and is being checked
        const val STATE_FORCED_UP =
            "forcedUP"//强制上传并忽略队列限制 Torrent is forced to uploading and ignore queue limit
        const val STATE_ALLOCATING =
            "allocating"//正在分配磁盘空间以供下载 Torrent is allocating disk space for download
        const val STATE_DOWNLOADING =
            "downloading"//下载中 Torrent is being downloaded and data is being transferred
        const val STATE_META_DL =
            "metaDL"//刚刚开始下载并正在获取元数据 Torrent has just started downloading and is fetching metadata
        const val STATE_PAUSED_DL =
            "pausedDL"//已暂停且尚未完成下载 Torrent is paused and has NOT finished downloading
        const val STATE_QUEUED_DL =
            "queuedDL"//排队等待下载 Queuing is enabled and torrent is queued for download
        const val STATE_STALLED_DL =
            "stalledDL"//下载中，但未建立连接 Torrent is being downloaded, but no connection were made
        const val STATE_CHECKING_DL =
            "checkingDL"//检查中，但尚未完成下载 Same as checkingUP, but torrent has NOT finished downloading
        const val STATE_FORCED_DL =
            "forcedDL"//强制下载并忽略队列限制 Torrent is forced to downloading to ignore queue limit
        const val STATE_CHECKING_RESUME_DATA =
            "checkingResumeData"//qB 启动时的恢复数据 Checking resume data on qBt startup
        const val STATE_MOVING = "moving"//文件正在移动到另一个位置 Torrent is moving to another location
        const val STATE_UNKNOWN = "unknown"//未知状态 Unknown status
    }
}