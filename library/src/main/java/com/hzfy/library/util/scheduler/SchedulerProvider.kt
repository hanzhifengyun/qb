package com.hzfy.library.util.scheduler

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class SchedulerProvider : ISchedulerProvider {
    override fun io(): CoroutineContext = Dispatchers.IO
    override fun main(): CoroutineContext = Dispatchers.Main
    override fun default(): CoroutineContext = Dispatchers.Default
}