package com.example.account_book_for_me.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "expenses")
data class Expense(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column(name = "total_amount")
        var totalAmount: Float? = null,

        @Column(name = "store_id")
        var storeId: Long? = null,

        @Column(name = "purchased_at")
        var purchasedAt: LocalDateTime? = null,

        @Column(name = "note", length = 140)
        var note: String? = null

)
