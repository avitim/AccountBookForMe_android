package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.CategoryDao
import com.example.accountbookforme.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val categoryList: Flow<List<CategoryEntity>> = categoryDao.findAll()

    @WorkerThread
    suspend fun create(categoryEntity: CategoryEntity) = categoryDao.create(categoryEntity)

    @WorkerThread
    suspend fun update(categoryEntity: CategoryEntity) = categoryDao.update(categoryEntity)

    @WorkerThread
    suspend fun deleteById(id: Long) = categoryDao.deleteById(id)
}