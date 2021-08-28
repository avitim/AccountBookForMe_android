package com.example.accountbookforme.application

import android.app.Application
import com.example.accountbookforme.database.MMDatabase
import com.example.accountbookforme.repository.CategoryRepository

class MMApplication : Application() {

    private val database by lazy { MMDatabase.getDatabase(this) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
}