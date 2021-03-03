package com.example.accountbookforme.model

import java.time.LocalDate

data class Expense(

    var totalAmount: Float = 0F,

    var purchasedAt: String? = null,

    // 値がnullなら初めて入力する店という意味
    // TODO: お気に入り店舗や店舗履歴を実装するまではnull固定
    var storeId: Long? = null,

    var storeName: String? = null,

    var note: String? = null

)
