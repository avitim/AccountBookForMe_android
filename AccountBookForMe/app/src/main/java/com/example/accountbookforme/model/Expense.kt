package com.example.accountbookforme.model

import androidx.room.ColumnInfo
import java.math.BigDecimal

data class Expense(

    val id: Long,

    @ColumnInfo(name = "purchased_at")
    val purchasedAt: String,

    val total: BigDecimal,

    // テーブル上の店舗名（storeId が null のときのみ non null）
    @ColumnInfo(name = "store_name")
    val storeName: String?,

    // storeIdをもとにstoresテーブルから取得した店舗名（storeId が null なら当然 null）
    var storeNameByStoreId: String?

)
