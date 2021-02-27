package com.example.accountbookforme.model

import java.time.LocalDateTime

data class Expense(

    val id: Long? = null,

    val totalAmount: Float? = null,

    val storeId: Long? = null,

    val purchasedAt: String? = null,

    val note: String? = null

)
