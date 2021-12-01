package com.example.accountbookforme.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "items",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = ExpenseDetailEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("expense_id"),
            onDelete = androidx.room.ForeignKey.CASCADE
        ),
        androidx.room.ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = androidx.room.ForeignKey.RESTRICT
        )],
    indices = [Index(value = ["expense_id"]), Index(value = ["category_id"])]
)
data class ItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var name: String = "",

    var price: BigDecimal = BigDecimal.ZERO,

    @ColumnInfo(name = "expense_id")
    var expenseId: Long = 0,

    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0
)
