package com.example.accountbookforme.model

import java.math.BigDecimal

data class Item(

    var id: Long,

    var name: String,

    var price: BigDecimal,

    var categoryId: Long,

    var expenseId: Long,

    var deleted: Boolean,
)