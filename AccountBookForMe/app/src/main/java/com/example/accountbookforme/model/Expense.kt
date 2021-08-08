package com.example.accountbookforme.model

import java.math.BigDecimal

data class Expense(

    val expenseId: Long,

    val purchasedAt: String,

    val total: BigDecimal,

    val storeName: String,

    )
