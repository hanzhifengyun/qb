package com.hzfy.library.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.hzfy.library.log.HzfyLog
import java.io.File


object FileUtils {
    fun uriToFileApiQ(context: Context, uri: Uri): File? {
        var file: File? = null
        //android10以上转换
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            file = uri.path?.let { File(it) }
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件复制到沙盒目录
            val contentResolver = context.contentResolver
            val displayName = (System.currentTimeMillis() + Math.round((Math.random() + 1) * 1000)
                    ).toString() + "." + MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(uri))

            HzfyLog.i("FileUtils", "displayName = $displayName")
            val path = uri.path
            val filename = path?.substringAfterLast("/") ?: displayName

            file = File(context.cacheDir.absolutePath, filename)

            uri.let { contentResolver.openInputStream(it) }.use { input ->
                file.outputStream().use { output ->

                    input?.copyTo(output)
                    HzfyLog.i("FileUtils", "fileSize = ${output.channel.size()}")

                }
            }
        }
        return file
    }

    fun getFileName(uri: Uri): String? {
        val path = uri.path
        val filename = path?.substringAfterLast("/") ?: path
        return filename
    }
}