package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.ExpensePaymentDao
import com.example.accountbookforme.database.entity.ExpensePaymentEntity
import java.math.BigDecimal

class ExpensePaymentRepository(private val expensePaymentDao: ExpensePaymentDao) {

    @WorkerThread
    suspend fun insert(epEntity: ExpensePaymentEntity) = expensePaymentDao.insert(epEntity)

    @WorkerThread
    suspend fun update(epEntity: ExpensePaymentEntity) = expensePaymentDao.update(epEntity)

    @WorkerThread
    suspend fun deleteById(id: Long) = expensePaymentDao.deleteById(id)

    @WorkerThread
    suspend fun findByExpenseId(expenseId: Long) = expensePaymentDao.findByExpenseId(expenseId)

    /**
     * 支出IDから総額を取得
     */
    @WorkerThread
    suspend fun calcTotalByExpenseId(expenseId: Long): BigDecimal =
        expensePaymentDao.findByExpenseId(expenseId).fold(BigDecimal.ZERO) { acc, ep ->
            acc + ep.total
        }

    /**
     * 決済方法IDから支払い額を取得
     */
    @WorkerThread
    suspend fun getPaymentTotal(paymentId: Long): BigDecimal =
        expensePaymentDao.findByPaymentId(paymentId).fold(BigDecimal.ZERO) { acc, ep ->
            acc + ep.total
        }
}