package com.hzfy.library.util.scheduler

import kotlin.coroutines.CoroutineContext

interface ISchedulerProvider {
    fun io(): CoroutineContext
    fun main(): CoroutineContext
    fun default(): CoroutineContext
}