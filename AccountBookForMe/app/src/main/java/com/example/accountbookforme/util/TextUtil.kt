package com.example.accountbookforme.util

import java.math.BigDecimal

object TextUtil {

    /**
     * BigDecimal型を小数点第2位までの文字列に変換する
     */
    fun convertToStr(price: BigDecimal?) = String.format("%.2f", price)
}