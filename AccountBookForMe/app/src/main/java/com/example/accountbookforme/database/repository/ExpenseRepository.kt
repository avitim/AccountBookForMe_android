package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.ExpenseDao
import com.example.accountbookforme.database.entity.ExpenseDetailEntity
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.Total
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val expenseList: Flow<List<Expense>> = expenseDao.findAll().map { list ->
        list.sortedByDescending { it.purchasedAt }
    }

    @WorkerThread
    suspend fun getDetailById(id: Long) = expenseDao.findById(id)

    @WorkerThread
    suspend fun findByStoreId(storeId: Long) = expenseDao.findByStoreId(storeId)

    // TODO: 実装
    @WorkerThread
    suspend fun getTotalPaymentList(): List<Total> = arrayListOf()

    // TODO: 実装
    @WorkerThread
    suspend fun getTotalStoreList(): List<Total> = arrayListOf()

    @WorkerThread
    suspend fun create(expenseDetailEntity: ExpenseDetailEntity): Long = expenseDao.create(expenseDetailEntity)

    @WorkerThread
    suspend fun update(expenseDetailEntity: ExpenseDetailEntity) = expenseDao.update(expenseDetailEntity)

    @WorkerThread
    suspend fun deleteById(id: Long) = expenseDao.deleteById(id)
}