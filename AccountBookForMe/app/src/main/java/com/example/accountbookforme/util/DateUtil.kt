package com.example.accountbookforme.util

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateUtil {

    const val DATE_EDMMMYYYY = "E, d MMM. yyyy"
    const val DATE_YYYYMMDD = "yyyy-MM-dd"

    /**
     * 年、月、日のInt型からLocalDateTimeフォーマットの文字列へ変換
     */
    fun parseLocalDateTimeFromInt(year: Int, month: Int, dayOfMonth: Int): String {

        val now = LocalTime.now()
        return "${year}-${zeroPaddingStr(month)}-${zeroPaddingStr(dayOfMonth)}T$now"
    }

    private fun parseLocalDateTime(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }

    /**
     * 画面表示用の日付フォーマッタ
     * フォーマットした日付を文字列で返す
     */
    fun formatDate(dateTime: String, pattern: String): String {

        // 日付フォーマッタ
        val dtfToShow = DateTimeFormatter.ofPattern(pattern)
        return dtfToShow.format(parseLocalDateTime(dateTime))
    }

    /**
     * 数字を0埋めした2桁に変換して文字列として返す
     */
    private fun zeroPaddingStr(num: Int): String {
        return num.toString().padStart(2, '0')
    }

}