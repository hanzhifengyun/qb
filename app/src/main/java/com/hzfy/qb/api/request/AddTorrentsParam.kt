package com.hzfy.qb.api.request

import android.net.Uri
import com.hzfy.common.model.ParamMenuItem
import com.hzfy.database.entity.QbEntity

class AddTorrentsParam(
    qb: QbEntity,
    val filepathList: List<Uri>,
    val autoTMM: Boolean,
    val savePath: String,
    val rename: String,
    val category: String,
    val paused: Boolean,
    val stopCondition: ParamMenuItem, //None, MetadataReceived(已收到元数据)，FilesChecked（已检查的文件）
    val contentLayout: ParamMenuItem, //Original, Subfolder（创建子文件夹），NoSubfolder（不创建子文件夹）
    val downloadSpeedLimit: Long, //NaN, 1024
    val uploadSpeedLimit: Long, //NaN, 1024
) : BaseQbParam(qb) {

    fun getStopConditionParam(): String {
        return stopCondition.param
    }

    fun getContentLayoutParam(): String {
        return contentLayout.param
    }

    fun getDownloadSpeedLimitParam(): String {
        return if (downloadSpeedLimit > 0) {
            (downloadSpeedLimit * KB).toString()
        } else {
            SPEED_LIMIT_NONE
        }
    }

    fun getUploadSpeedLimitParam(): String {
        return if (uploadSpeedLimit > 0) {
            (uploadSpeedLimit * KB).toString()
        } else {
            SPEED_LIMIT_NONE
        }
    }


    companion object {
        private const val SPEED_LIMIT_NONE = "NaN"
        private const val KB = 1024
    }
}