package com.hzfy.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "qb", indices = [Index(value = ["url"], unique = true)])
data class QbEntity(
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "password")
    var password: String,
){
    // 主键，自增长
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    // 应用版本
    @ColumnInfo(name = "appVersion")
    var appVersion: String = ""
    /**
     * 默认下载地址
     */
    @ColumnInfo(name = "savePath")
    var savePath: String? = null

    /**
     * 全局下载限速 b/s  -1为不限速
     */
    @ColumnInfo(name = "downloadLimit")
    var downloadLimit: Long = SPEED_LIMIT_NO
    /**
     * 全局上传限速 b/s  -1为不限速
     */
    @ColumnInfo(name = "uploadLimit")
    var uploadLimit: Long = SPEED_LIMIT_NO


    companion object {
        // 不限速
        const val SPEED_LIMIT_NO = 0L
    }
}