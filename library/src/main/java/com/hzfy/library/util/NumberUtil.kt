package com.hzfy.library.util

import java.math.BigDecimal
import java.math.RoundingMode

object NumberUtil {
    /**
     * @return 两数相除保留newScale小数
     */
    fun divide(a: BigDecimal?, b: BigDecimal?, newScale: Int = 2): BigDecimal {
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO
        }
        val left = a ?: BigDecimal.ZERO

        return left.divide(b, newScale, RoundingMode.HALF_UP)
    }

    /**
     * @return 两数相除保留newScale小数
     */
    fun divide(a: Double, b: Double, newScale: Int = 2): BigDecimal {
        return divide(BigDecimal(a), BigDecimal(b), newScale)
    }
}
