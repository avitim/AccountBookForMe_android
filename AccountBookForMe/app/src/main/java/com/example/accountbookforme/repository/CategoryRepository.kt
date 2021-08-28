package com.example.accountbookforme.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.dao.CategoryDao
import com.example.accountbookforme.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val categoryList: Flow<List<CategoryEntity>> = categoryDao.findAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun create(categoryEntity: CategoryEntity) = categoryDao.create(categoryEntity)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(categoryEntity: CategoryEntity) = categoryDao.update(categoryEntity)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id: Long) = categoryDao.deleteById(id)
}