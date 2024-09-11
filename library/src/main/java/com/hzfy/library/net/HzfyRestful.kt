package com.hzfy.library.net

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap


open class HzfyRestful(private val config: HzfyApiConfig, callFactory: HzfyCall.Factory) {
    private var interceptorList: MutableList<HzfyInterceptor> = mutableListOf()
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()
    private var scheduler: Scheduler = Scheduler(callFactory, interceptorList)

    fun addInterceptor(interceptor: HzfyInterceptor) {
        interceptorList.add(interceptor)
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { _, method, args ->
            var parser: MethodParser? = methodService[method]
            if (parser == null) {
                parser = MethodParser.parse(config.baseUrl, method)
                methodService[method] = parser
            }

            val request = parser.newRequest(method, args)

            scheduler.newCall(request)
        } as T
    }

}