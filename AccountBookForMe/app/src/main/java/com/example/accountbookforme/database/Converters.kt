package com.example.accountbookforme.database

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal {
        return BigDecimal(value)
    }

    @TypeConverter
    fun fromBigDecimal(decimal: BigDecimal?): String? {
        return decimal?.toString()
    }
}