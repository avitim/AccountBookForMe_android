package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.ItemDao
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.model.Total

class ItemRepository(private val itemDao: ItemDao) {

    @WorkerThread
    suspend fun insert(itemEntity: ItemEntity) = itemDao.insert(itemEntity)

    @WorkerThread
    suspend fun update(itemEntity: ItemEntity) = itemDao.update(itemEntity)

    @WorkerThread
    suspend fun deleteById(id: Long) = itemDao.deleteById(id)

    @WorkerThread
    suspend fun findByExpenseId(expenseId: Long) = itemDao.findByExpenseId(expenseId)

    @WorkerThread
    suspend fun findByCategoryId(categoryId: Long) = itemDao.findByCategoryId(categoryId)

    // TODO: 要実装
    suspend fun getTotalCategoryList(): List<Total> = arrayListOf()
}