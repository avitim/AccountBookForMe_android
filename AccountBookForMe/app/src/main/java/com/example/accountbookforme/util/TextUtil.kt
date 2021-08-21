package com.example.accountbookforme.util

import java.math.BigDecimal

object TextUtil {

    /**
     * BigDecimal型に"¥"をつけて、小数点第2位までの文字列に変換する
     */
    fun convertToStrWithCurrency(price: BigDecimal?) = String.format("¥%.2f", price)
}