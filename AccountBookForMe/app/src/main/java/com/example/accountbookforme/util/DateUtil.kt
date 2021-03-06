package com.example.accountbookforme.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {

    val DATE_EDMMMYYYY = "E, d MMM. yyyy"
    val DATE_YYYYMMDD = "yyyy-MM-dd"

    /**
     * 年、月、日のInt型からLocalDate型へ変換
     */
    fun parseLocalDateFromInt(year: Int, month: Int, dayOfMonth: Int): LocalDate {

        val dateStr = "${year}-${zeroPaddingStr(month)}-${zeroPaddingStr(dayOfMonth)}"
        return LocalDate.parse(dateStr)
    }

    /**
     * 画面表示用の日付フォーマッタ
     * フォーマットした日付を文字列で返す
     */
    fun formatDate(localDate: LocalDate, pattern: String): String {

        // 日付フォーマッタ
        val dtfToShow = DateTimeFormatter.ofPattern(pattern)
        return dtfToShow.format(localDate)
    }

    /**
     * 数字を0埋めした2桁に変換して文字列として返す
     */
    private fun zeroPaddingStr(num: Int): String {
        return num.toString().padStart(2, '0')
    }

}