package com.example.accountbookforme.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.accountbookforme.database.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    /**
     * 全件取得
     */
    @Query("SELECT * FROM payments")
    fun findAll(): Flow<List<PaymentEntity>>

    /**
     * 新規作成
     */
    @Insert
    suspend fun create(paymentEntity: PaymentEntity)

    /**
     * 更新
     */
    @Update
    suspend fun update(paymentEntity: PaymentEntity)

    /**
     * 削除
     */
    @Query("DELETE FROM payments WHERE id IS (:id)")
    suspend fun deleteById(id: Long)
}