package com.example.accountbookforme.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "expense_payments",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = ExpenseDetailEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("expense_id"),
            onDelete = androidx.room.ForeignKey.CASCADE
        ),
        androidx.room.ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("payment_id"),
            onDelete = androidx.room.ForeignKey.RESTRICT
        )],
    indices = [Index(value = ["expense_id"]), Index(value = ["payment_id"])]
)
data class ExpensePaymentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var total: BigDecimal = BigDecimal.ZERO,

    @ColumnInfo(name = "expense_id")
    var expenseId: Long = 0,

    @ColumnInfo(name = "payment_id")
    var paymentId: Long = 0
)
