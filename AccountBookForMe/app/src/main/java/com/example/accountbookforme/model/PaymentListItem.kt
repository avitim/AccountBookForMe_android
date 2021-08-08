package com.example.accountbookforme.model

import java.math.BigDecimal

data class PaymentListItem(

    val id: Long,

    val name: String = "",

    val subTotal: BigDecimal = BigDecimal.ZERO,

    // TODO: 以下は今後実装予定
//    val colorId: Long,
//
//    val iconId: Long

)
