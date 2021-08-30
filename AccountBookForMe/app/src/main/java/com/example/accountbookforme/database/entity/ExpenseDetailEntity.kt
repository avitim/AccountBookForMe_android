package com.example.accountbookforme.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "expense_details",
    foreignKeys = [ForeignKey(
        entity = StoreEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("store_id"),
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["store_id"])]
)
data class ExpenseDetailEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // デフォルトは今日
    @ColumnInfo(name = "purchased_at")
    var purchasedAt: LocalDateTime = LocalDateTime.now(),

    var total: BigDecimal = BigDecimal.ZERO,

    @ColumnInfo(name = "store_id")
    var storeId: Long? = null,

    @ColumnInfo(name = "store_name")
    var storeName: String? = null,

    var note: String = ""
)
