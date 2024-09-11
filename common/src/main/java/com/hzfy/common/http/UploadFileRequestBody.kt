package com.hzfy.common.http

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer
import java.io.File

class UploadFileRequestBody(val file: File, val uploadListener: UploadListener? = null) :
    RequestBody() {
    private val requestBody = file.asRequestBody("application/octet-stream".toMediaType())

    init {
        uploadListener?.onStartUpload(contentLength())
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    override fun writeTo(sink: BufferedSink) {
        val bufferedSink = object : ForwardingSink(sink) {
            var totalBytesWritten: Long = 0
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                totalBytesWritten += byteCount
                uploadListener?.onProgress(totalBytesWritten)
                if (totalBytesWritten == contentLength()) {
                    uploadListener?.onComplete()
                }
            }
        }.buffer()
// 写入
        requestBody.writeTo(bufferedSink)
// 刷新
// 必须调用flush，否则最后一部分数据可能不会被写入
// bufferedSink.flush() - Removed redundant flush call
    }
}


//提供一个监听下载进度的接口
interface UploadListener {
    fun onStartUpload(length: Long)
    fun onProgress(progress: Long)
    fun onComplete()
}