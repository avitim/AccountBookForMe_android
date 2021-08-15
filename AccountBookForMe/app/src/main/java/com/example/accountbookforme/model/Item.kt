package com.example.accountbookforme.model

import java.math.BigDecimal

data class Item(

    var id: Long? = null,

    var name: String = "",

    var price: BigDecimal = BigDecimal.ZERO,

    var categoryId: Long? = null,

    var expenseId: Long? = null,

    var deleted: Boolean = false,
)