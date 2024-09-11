package com.hzfy.common.http

import android.content.Context
import com.hzfy.library.log.HzfyLog
import com.hzfy.library.net.HzfyApiConfig
import com.hzfy.library.net.HzfyCall
import com.hzfy.library.net.HzfyCallback
import com.hzfy.library.net.HzfyConvert
import com.hzfy.library.net.HzfyRequest
import com.hzfy.library.net.HzfyResponse
import com.hzfy.library.util.FileUtils
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

class RetrofitCallFactory(
    val context: Context,
    config: HzfyApiConfig,
    val convert: HzfyConvert<Response<ResponseBody>>
) : HzfyCall.Factory {
    companion object {
        const val TAG = "RetrofitCall"
    }

    private var apiService: ApiService

    init {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
            .readTimeout(config.readTimeout, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            HzfyLog.i(config.logTag, message)
        }
        if (config.isDebug) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        builder.addInterceptor(loggingInterceptor)

        val retrofit = Retrofit.Builder()
            .client(builder.build())
            .baseUrl(config.baseUrl)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    override fun newCall(request: HzfyRequest): HzfyCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(private val request: HzfyRequest) : HzfyCall<T> {
        override fun execute(): HzfyResponse<T> {
            val realCall: Call<ResponseBody> = createRealCall(request)
            val response: Response<ResponseBody> = realCall.execute()
            return parseResponse(response)
        }


        override fun enqueue(callback: HzfyCallback<T>) {
            val realCall: Call<ResponseBody> = createRealCall(request)

            realCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailure(throwable = t)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val response: HzfyResponse<T> = parseResponse(response)
                    callback.onSuccess(response)
                }

            })
        }

        private fun parseResponse(response: Response<ResponseBody>): HzfyResponse<T> {
            return convert.convert(response, request)
        }

        private fun createRealCall(request: HzfyRequest): Call<ResponseBody> {
            when (request.httpMethod) {
                HzfyRequest.METHOD.GET -> {
                    return apiService.get(
                        request.headers,
                        request.getRequestUrl(),
                        request.parameters
                    )
                }

                HzfyRequest.METHOD.POST -> {
                    val parameters: MutableMap<String, String?>? = request.parameters
                    val builder = FormBody.Builder()
                    val requestBody: RequestBody
                    val jsonObject = JSONObject()
                    if (parameters != null) {
                        for ((key, value) in parameters) {
                            if (request.formPost) {
                                value?.let { builder.add(key, it) }
                            } else {
                                jsonObject.put(key, value)
                            }
                        }
                    }
                    requestBody = if (request.formPost) {
                        builder.build()
                    } else {
                        jsonObject.toString()
                            .toRequestBody("application/json;utf-8".toMediaTypeOrNull())
                    }
                    return apiService.post(request.headers, request.getRequestUrl(), requestBody)
                }

                HzfyRequest.METHOD.JSON_POST -> {
                    val json = request.json ?: ""
                    val requestBody =
                        json.toRequestBody("application/json;utf-8".toMediaTypeOrNull())
                    return apiService.post(request.headers, request.getRequestUrl(), requestBody)
                }

                HzfyRequest.METHOD.MULTI_POST -> {
                    var filePathKey = request.filePathKey
                        ?: throw NullPointerException("filePathKey cannot be null")
                    if (!filePathKey.endsWith("[]")) {
                        filePathKey += "[]"
                    }
                    val uploadListener = object : UploadListener {
                        override fun onStartUpload(length: Long) {

                        }

                        override fun onProgress(progress: Long) {

                        }

                        override fun onComplete() {

                        }
                    }

                    val parameters: MutableMap<String, String?>? = request.parameters
                    val builder = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)

                    if (parameters != null) {
                        for ((key, value) in parameters) {
                            value?.let {
                                builder.addFormDataPart(key, it)
                            }
                        }
                    }
                    request.filePathList.forEach { uri ->
                        val file = FileUtils.uriToFileApiQ(context, uri)

                        file?.let {
                            if (file.exists()) {
                                HzfyLog.i(TAG, "upload file is exists")
                            } else {
                                HzfyLog.i(TAG, "upload file is not exists")
                            }

//                            val uploadBody = UploadFileRequestBody(file, uploadListener)
                            val uploadBody =
                                file.asRequestBody("application/octet-stream".toMediaType())
                            builder.addFormDataPart(
                                filePathKey,
                                file.name,
                                uploadBody
                            )
                        }

                    }


                    val requestBody = builder.build()

                    return apiService.multiPost(
                        request.headers,
                        request.getRequestUrl(),
                        requestBody
                    )
                }

                else -> {

                    throw IllegalStateException("restful only support GET POST for now ,url=" + request.getRequestUrl())
                }
            }

        }
    }


    interface ApiService {

        @GET
        @Streaming
        fun get(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String?>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>


        @POST
        fun multiPost(
            @HeaderMap headers: MutableMap<String, String>?, @Url url: String,
            @Body body: MultipartBody
        ): Call<ResponseBody>
    }

}