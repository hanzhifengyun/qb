package com.hzfy.common.util

import com.hzfy.library.util.NumberUtil

object UIUtil {

    const val KB = 1024L
    const val MB = 1024L * KB
    const val GB = 1024L * MB
    const val TB = 1024L * GB

    fun getProgressText(progress: Float): String {
        if (progress <= 0) {
            return "0.0%"
        }
        if (progress >= 1) {
            return "100.0%"
        }
        val format = "%.1f".format(progress * 100)
        return "$format%"
    }

    fun getSpeedText(speed: Long): String {
        val result: Double = speed.toDouble()
        if (speed < KB) {
            return "$speed B/s"
        }
        if (speed < MB) {
            val divide = NumberUtil.divide(result, KB.toDouble(), 1).toString()
            return "$divide K/s"
        }
        if (speed < GB) {
            val divide = NumberUtil.divide(result, MB.toDouble(), 1).toString()
            return "$divide M/s"
        }
        val divide = NumberUtil.divide(result, GB.toDouble(), 1).toString()
        return "$divide G/s"
    }

    fun getSizeText(size: Long, scale: Int = 2): String {
        val result: Double = size.toDouble()
        if (size < KB) {
            return "$size B"
        }
        if (size < MB) {
            val divide = NumberUtil.divide(result, KB.toDouble(), scale).toString()
            return "$divide KB"
        }
        if (size < GB) {
            val divide = NumberUtil.divide(result, MB.toDouble(), scale).toString()
            return "$divide MB"
        }
        if (size < TB) {
            val divide = NumberUtil.divide(result, GB.toDouble(), scale).toString()
            return "$divide GB"
        }
        val divide = NumberUtil.divide(result, TB.toDouble(), scale).toString()
        return "$divide TB"
    }

}