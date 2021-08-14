package com.example.accountbookforme.model

import java.math.BigDecimal

data class Payment(

    var id: Long,

    var total: BigDecimal = BigDecimal.ZERO,

    var expenseId: Long,

    var paymentId: Long,

    var deleted: Boolean,

    )
