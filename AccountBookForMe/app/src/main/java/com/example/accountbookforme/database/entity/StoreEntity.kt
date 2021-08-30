package com.example.accountbookforme.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "stores", indices = [Index(value = ["id"])])
data class StoreEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var name: String

)
