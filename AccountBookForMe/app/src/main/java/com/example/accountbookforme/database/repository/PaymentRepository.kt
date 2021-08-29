package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.PaymentDao
import com.example.accountbookforme.database.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

class PaymentRepository(private val paymentDao: PaymentDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val paymentList: Flow<List<PaymentEntity>> = paymentDao.findAll()

    @WorkerThread
    suspend fun create(paymentEntity: PaymentEntity) = paymentDao.create(paymentEntity)

    @WorkerThread
    suspend fun update(paymentEntity: PaymentEntity) = paymentDao.update(paymentEntity)

    @WorkerThread
    suspend fun deleteById(id: Long) = paymentDao.deleteById(id)
}