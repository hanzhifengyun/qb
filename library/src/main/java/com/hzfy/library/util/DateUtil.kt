package com.hzfy.library.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    const val FORMAT_DEFAULT: String = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DEFAULT_1: String = "yyyy/MM/dd HH:mm:ss"

    fun getCurrentDate(): Date {
        return Date()
    }

    fun getCurrentDateTime(): Long {
        return getCurrentDate().time
    }

    fun formatCurrentDate(pattern: String = FORMAT_DEFAULT): String {
        return format(getCurrentDate(), pattern)
    }

    private fun getSimpleDateFormat(pattern: String = FORMAT_DEFAULT): SimpleDateFormat {
        return SimpleDateFormat(pattern, Locale.getDefault())
    }

    fun format(date: Date, pattern: String = FORMAT_DEFAULT): String {
        return getSimpleDateFormat(pattern).format(date)
    }

    fun format(dateTime: Long, pattern: String = FORMAT_DEFAULT): String {
        return format(Date(dateTime), pattern)
    }

    fun parse(dateStr: String, pattern: String = FORMAT_DEFAULT): Date? {
        return getSimpleDateFormat(pattern).parse(dateStr)
    }

    fun safeParse(dateStr: String, pattern: String = FORMAT_DEFAULT): Date? {
        return try {
            parse(dateStr, pattern = pattern)
        } catch (e: ParseException) {
            null
        }
    }

}