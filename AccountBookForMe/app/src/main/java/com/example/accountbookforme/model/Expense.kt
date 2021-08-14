package com.example.accountbookforme.model

import java.math.BigDecimal

data class Expense(

    val id: Long,

    val purchasedAt: String,

    val total: BigDecimal,

    val storeName: String,

    )
