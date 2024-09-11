package com.hzfy.common.http

import android.os.Environment
import com.hzfy.library.log.HzfyLog
import com.hzfy.library.net.HzfyConvert
import com.hzfy.library.net.HzfyRequest
import com.hzfy.library.net.HzfyResponse
import com.hzfy.library.net.exception.HttpStatusCodeException
import com.hzfy.library.tool.IJsonHandler
import okhttp3.Headers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class JsonResponseConvert(private val jsonHandler: IJsonHandler) : HzfyConvert<Response<ResponseBody>> {

    override fun <R> convert(data: Response<ResponseBody>, request: HzfyRequest): HzfyResponse<R> {
        val result = HzfyResponse<R>()
        parseMainInfo(data, result, request)
        parseHeaders(data.headers(), result)
        return result
    }

    private fun <R> parseData(result: HzfyResponse<R>, request: HzfyRequest) {
        val rawData = result.rawData
        val dataType = request.returnType!!
        if (dataType == String::class.java) {
            result.data = rawData as R
        } else {
            val jsonObject = rawData?.let { JSONObject(it) }
            result.data = jsonHandler.fromJson(jsonObject.toString(), dataType)
        }
    }

    private fun <R> parseMainInfo(
        response: Response<ResponseBody>,
        result: HzfyResponse<R>, request: HzfyRequest
    ) {
        result.code = response.code()
        if (response.isSuccessful) {
            val body = response.body()
            if (request.isDownloadFile()) {
                download(result, response, request)
            } else {
                val rawData = body?.string()
                result.rawData = rawData
                parseData(result, request)
            }
        } else {
            val body = response.errorBody()
            val rawData = body?.string()
            result.rawData = rawData
            throw HttpStatusCodeException(statusCode = response.code(), message = rawData)
        }
    }

    private fun <R> parseHeaders(
        headers: Headers,
        result: HzfyResponse<R>
    ) {
        val mapHeaders = mutableMapOf<String, String>()
        HzfyLog.i("headers start-------")
        for (i in 0 until headers.size) {
            val name = headers.name(i)
            val value = headers.value(i)
            mapHeaders[name] = value
            HzfyLog.i("headers ：name = $name   value = $value")
        }
        HzfyLog.i("headers end-------")
        result.headers = mapHeaders
    }



    private fun <R> download(
        result: HzfyResponse<R>,
        response: Response<ResponseBody>,
        request: HzfyRequest
    ) {
        response.body()?.let { body ->
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, request.fileName!!)

            body.byteStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            result.data = file.absolutePath as R
        }
    }

}