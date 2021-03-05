package com.example.accountbookforme.model

data class PaymentListItem(

    val id: Long,

    val name: String = "",

    val subTotal: Float = 0F,

    // TODO: 以下は今後実装予定
//    val colorId: Long,
//
//    val iconId: Long

)
