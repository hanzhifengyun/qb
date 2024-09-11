package com.hzfy.library.net

import android.net.Uri
import com.hzfy.library.net.annotation.BaseUrl
import com.hzfy.library.net.annotation.FileName
import com.hzfy.library.net.annotation.FilePath
import com.hzfy.library.net.annotation.Filed
import com.hzfy.library.net.annotation.GET
import com.hzfy.library.net.annotation.Headers
import com.hzfy.library.net.annotation.JsonFiled
import com.hzfy.library.net.annotation.JsonPOST
import com.hzfy.library.net.annotation.MultiPOST
import com.hzfy.library.net.annotation.POST
import com.hzfy.library.net.annotation.Path
import com.hzfy.library.net.annotation.Url
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType

class MethodParser(
    private val baseUrl: String,
    method: Method
) {

    init {
        //parse method annotations such as get,headers,post baseUrl
        parseMethodAnnotations(method)

        //parse method generic return type
        parseMethodReturnType(method)

        //parse method parameters such as path,filed
        //parseMethodParameters(method, args)
    }

    private var replaceRelativeUrl: String? = null
    private var domainUrl: String? = null
    private var url: String? = null
    private var fileName: String? = null
    private var json: String? = null
    private var formPost: Boolean = true
    private var filePathKey: String? = null
    private var filePathList: MutableList<Uri> = mutableListOf()
    private var httpMethod: HzfyRequest.METHOD? = null
    private lateinit var relativeUrl: String
    private lateinit var returnType: Type
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, String?> = mutableMapOf()


    companion object {
        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }


    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HzfyCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method %s must be type of IHzfyCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) { "method %s can only has one generic return type" }
            val argument = actualTypeArguments[0]
            require(validateGenericType(argument)) {
                String.format("method %s generic return type must not be an unknown type. " + method.name)
            }
            returnType = argument
        } else {
            throw IllegalStateException(
                String.format(
                    "method %s must has one gerneric return type",
                    method.name
                )
            )
        }
    }

    private fun validateGenericType(type: Type): Boolean {
        /**
         *wrong
         *  fun test():HiCall<Any>
         *  fun test():HiCall<List<*>>
         *  fun test():HiCall<ApiInterface>
         *expect
         *  fun test():HiCall<User>
         */
        //如果指定的泛型是集合类型的，那还检查集合的泛型参数
        if (type is GenericArrayType) {
            return validateGenericType(type.genericComponentType)
        }
        //如果指定的泛型是一个接口 也不行
        if (type is TypeVariable<*>) {
            return false
        }
        //如果指定的泛型是一个通配符 ？extends Number 也不行
        if (type is WildcardType) {
            return false
        }

        return true
    }

    fun newRequest(method: Method, args: Array<out Any>?): HzfyRequest {
        val arguments: Array<Any?> = args as Array<Any?>? ?: arrayOf()
        parseMethodParameters(method, arguments)

        val request = HzfyRequest()
        request.url = url
        request.fileName = fileName
        request.domainUrl = domainUrl
        request.returnType = returnType
        request.relativeUrl = replaceRelativeUrl ?: relativeUrl
        request.parameters = parameters
        request.headers = headers
        request.filePathKey = filePathKey
        request.filePathList = filePathList
        request.httpMethod = httpMethod
        request.formPost = formPost
        request.json = json
        return request
    }

    private fun parseMethodParameters(method: Method, args: Array<Any?>) {
        //每次调用api接口时  应该吧上一次解析到的参数清理掉，因为methodParser存在复用
        parameters.clear()
        filePathList.clear()

        //@Path("province") province: Int,@Filed("page") page: Int
        val parameterAnnotations = method.parameterAnnotations
        method.parameterTypes
        val equals = parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s dont match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        //args
        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) { "filed can only has one annotation :index =$index" }

            val value: Any? = args[index]

            require(isSupportValue(value)) { "8 basic types are supported for now,index=$index" }

            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                parameters[key] = value?.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName.isNotEmpty()) {
                    //relativeUrl = home/{categroyId}
                    replaceRelativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                }
            } else if (annotation is Url) {
                url = value.toString()
            }  else if (annotation is FileName) {
                fileName = value.toString()
            } else if (annotation is FilePath) {
                filePathKey = annotation.value
                if (value == null) {
                    throwFilepathCannotBeNullOrEmptyException()
                }
                if (value is Uri) {
                    val filePath = value.path
                    if (filePath.isNullOrEmpty()) {
                        throwFilepathCannotBeNullOrEmptyException()
                    }
                    filePathList.add(value)
                } else {
                    val valueList = value as? List<*>
                    if (valueList == null) {
                        throw IllegalStateException("unSupport types for FilePath param: " + value!!.javaClass.name)
                    }
                    if (valueList.isEmpty()) {
                        throwFilepathCannotBeNullOrEmptyException()
                    }
                    valueList.forEach { filePath ->
                        if (filePath == null) {
                            throwFilepathCannotBeNullOrEmptyException()
                        }
                        if (filePath is Uri) {
                            filePathList.add(filePath)
                        } else {
                            throw IllegalStateException("unSupport types for FilePath param: " + filePath!!.javaClass.name)
                        }
                    }
                }

            } else if (annotation is BaseUrl) {
                domainUrl = value.toString()
            } else if (annotation is JsonFiled) {
                json = value.toString()
            } else {
                throw IllegalStateException("cannot handle parameter annotation :" + annotation.javaClass.toString())
            }
        }


    }

    private fun throwFilepathCannotBeNullOrEmptyException() {
        throw NullPointerException("filepath value can not be null or empty")
    }

    private fun parseMethodAnnotations(method: Method) {

        val annotations = method.annotations
        for (annotation in annotations) {
            when (annotation) {
                is GET -> {
                    relativeUrl = annotation.value
                    httpMethod = HzfyRequest.METHOD.GET
                }

                is POST -> {
                    relativeUrl = annotation.value
                    httpMethod = HzfyRequest.METHOD.POST
                    formPost = annotation.formPost
                }

                is MultiPOST -> {
                    relativeUrl = annotation.value
                    httpMethod = HzfyRequest.METHOD.MULTI_POST
                }


                is JsonPOST -> {
                    relativeUrl = annotation.value
                    httpMethod = HzfyRequest.METHOD.JSON_POST
                }

                is Headers -> {
                    val headersArray = annotation.value
                    //@Headers("auth-token:token", "accountId:123456")
                    for (header in headersArray) {
                        val colon = header.indexOf(":")
                        check(!(colon == 0 || colon == -1)) {
                            String.format(
                                "@headers value must be in the form [name:value] ,but found [%s]",
                                header
                            )
                        }
                        val name = header.substring(0, colon)
                        val value = header.substring(colon + 1).trim()
                        headers[name] = value
                    }
                }

                is BaseUrl -> {
                    domainUrl = annotation.value
                }

                else -> {
                    throw IllegalStateException("cannot handle method annotation:" + annotation.javaClass.toString())
                }
            }
        }

        require(
            (httpMethod == HzfyRequest.METHOD.GET)
                    || (httpMethod == HzfyRequest.METHOD.POST)
                    || (httpMethod == HzfyRequest.METHOD.JSON_POST)
                    || (httpMethod == HzfyRequest.METHOD.MULTI_POST)
        ) {
            String.format("method %s must has one of GET,POST ", method.name)
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }

    }


    private fun isSupportValue(value: Any?): Boolean {
        if (value == null) {
            return true
        }
        //String
        if (value is String) {
            return true
        }
        if (value is Boolean) {
            return true
        }
        if (value is Double) {
            return true
        }
        if (value is Long) {
            return true
        }
        if (value is Int) {
            return true
        }
        if (value.javaClass.isPrimitive) {
            return true
        }
        val list = value as? List<*>
        if (!list.isNullOrEmpty()) {
            return true
        }
        return false
    }

}