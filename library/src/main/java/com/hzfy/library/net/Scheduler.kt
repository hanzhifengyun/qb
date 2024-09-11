package com.hzfy.library.net

/**
 * 代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(
    private val callFactory: HzfyCall.Factory,
    private val interceptors: MutableList<HzfyInterceptor>
) {
    fun newCall(request: HzfyRequest): HzfyCall<*> {
        val newCall: HzfyCall<*> = callFactory.newCall(request)
        return ProxyCall(newCall, request)
    }


    internal inner class ProxyCall<T>(
        private val delegate: HzfyCall<T>,
        private val request: HzfyRequest
    ) : HzfyCall<T> {
        override fun execute(): HzfyResponse<T> {
            dispatchInterceptor(request, null)

            val response = delegate.execute()

            dispatchInterceptor(request, response)

            return response
        }

        override fun enqueue(callback: HzfyCallback<T>) {
            dispatchInterceptor(request, null)

            delegate.enqueue(object : HzfyCallback<T> {
                override fun onSuccess(response: HzfyResponse<T>) {
                    dispatchInterceptor(request, response)

                    callback.onSuccess(response)
                }

                override fun onFailure(throwable: Throwable) {
                    callback.onFailure(throwable)
                }

            })
        }

        private fun dispatchInterceptor(request: HzfyRequest, response: HzfyResponse<T>?) {
            if (interceptors.size <= 0)
                return
            InterceptorChain(request, response).dispatch()
        }


        internal inner class InterceptorChain(
            private val request: HzfyRequest,
            private val response: HzfyResponse<T>?
        ) : HzfyInterceptor.Chain {
            //代表的是 分发的第几个拦截器
            var callIndex: Int = 0

            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HzfyRequest {
                return request
            }

            override fun response(): HzfyResponse<*>? {
                return response
            }


            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }

        }
    }

}

