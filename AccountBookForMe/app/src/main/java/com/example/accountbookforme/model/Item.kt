package com.example.accountbookforme.model

import java.math.BigDecimal

data class Item(

    var id: Long? = null,

    var name: String = "",

    var price: BigDecimal = BigDecimal.ZERO,

    // TODO: デフォルトで仮に1とする
    var categoryId: Long = 1,

    var expenseId: Long? = null,

    var deleted: Boolean = false,
)