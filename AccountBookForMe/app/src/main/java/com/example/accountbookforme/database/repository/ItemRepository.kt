package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.ItemDao
import com.example.accountbookforme.database.entity.ItemEntity
import java.math.BigDecimal

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

    /**
     * カテゴリIDの品物の合計額を取得
     */
    @WorkerThread
    suspend fun getCategoryTotal(categoryId: Long): BigDecimal =
        itemDao.findByCategoryId(categoryId).fold(BigDecimal.ZERO) { acc, item ->
            acc + item.price
        }
}