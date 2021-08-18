package com.example.accountbookforme.model

import java.math.BigDecimal

data class Payment(

    var id: Long? = null,

    var total: BigDecimal = BigDecimal.ZERO,

    // TODO: デフォルトで仮に1とする
    var paymentId: Long = 1,

    var expenseId: Long? = null,
)
