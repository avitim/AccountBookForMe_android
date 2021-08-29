package com.example.accountbookforme.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var name: String

)
