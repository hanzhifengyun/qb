package com.hzfy.library.ext

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import java.util.concurrent.TimeUnit


fun flowInterval(delay: Long = 0L, period: Long, unit: TimeUnit): Flow<Long> = flow {

    delay(unit.toMillis(delay))
    var count = 0L
    while (true) {
        emit(count++)
        delay(unit.toMillis(period))
    }
}

/**
 * flow 倒计时
 *
 * @param seconds 秒数
 * @return Flow<Long>  if seconds = 3, collect 0, 1, 2
 */
fun flowCountdownSeconds(seconds: Int): Flow<Long> = flowInterval(period = 1L, unit = TimeUnit.SECONDS).take(seconds)