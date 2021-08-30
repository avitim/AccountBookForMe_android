package com.example.accountbookforme

import android.app.Application
import com.example.accountbookforme.database.MMDatabase
import com.example.accountbookforme.database.repository.CategoryRepository
import com.example.accountbookforme.database.repository.ExpenseRepository
import com.example.accountbookforme.database.repository.PaymentRepository
import com.example.accountbookforme.database.repository.StoreRepository

class MMApplication : Application() {

    private val database by lazy { MMDatabase.getDatabase(this) }
    val expenseRepository by lazy { ExpenseRepository(database.expenseDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val paymentRepository by lazy { PaymentRepository(database.paymentDao()) }
    val storeRepository by lazy { StoreRepository(database.storeDao()) }
}