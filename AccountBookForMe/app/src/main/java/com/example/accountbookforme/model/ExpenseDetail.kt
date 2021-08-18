package com.example.accountbookforme.model

import com.example.accountbookforme.util.DateUtil
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExpenseDetail(

    var id: Long? = null,

    var total: BigDecimal = BigDecimal.ZERO,

    // デフォルトは今
    var purchasedAt: String = SimpleDateFormat(DateUtil.DATE_YYYYMMDDHHMMSS, Locale.JAPAN).format(
        Date()
    ),

    // 値がnullなら手入力した店という意味
    // TODO: お気に入り店舗や店舗履歴を実装するまではnull固定
    var storeId: Long? = null,

    var storeName: String? = null,

    var note: String? = null,

    var paymentList: MutableList<Payment> = mutableListOf(),

    var itemList: MutableList<Item> = mutableListOf(),

    var deletedItemList: MutableList<Long> = mutableListOf(),

    var deletedPaymentList: MutableList<Long> = mutableListOf(),
)
