package com.example.accountbookforme.database.repository

import androidx.annotation.WorkerThread
import com.example.accountbookforme.database.dao.StoreDao
import com.example.accountbookforme.database.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

class StoreRepository(private val storeDao: StoreDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val storeList: Flow<List<StoreEntity>> = storeDao.findAll()

    @WorkerThread
    suspend fun create(storeEntity: StoreEntity) = storeDao.create(storeEntity)

    @WorkerThread
    suspend fun update(storeEntity: StoreEntity) = storeDao.update(storeEntity)

    @WorkerThread
    suspend fun deleteById(id: Long) = storeDao.deleteById(id)
}