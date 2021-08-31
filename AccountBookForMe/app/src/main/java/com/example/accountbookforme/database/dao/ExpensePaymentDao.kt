package com.example.accountbookforme.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.accountbookforme.database.entity.ExpensePaymentEntity

@Dao
interface ExpensePaymentDao {

    /**
     * 新規作成
     */
    @Insert
    suspend fun insert(epEntity: ExpensePaymentEntity)

    /**
     * 更新
     */
    @Update
    suspend fun update(epEntity: ExpensePaymentEntity)

    /**
     * 削除
     */
    @Query("DELETE FROM expense_payments WHERE id IS (:id)")
    suspend fun deleteById(id: Long)

    /**
     * 支出IDが一致するリスト取得
     */
    @Query("SELECT * FROM expense_payments WHERE expense_id IS (:expenseId)")
    suspend fun findByExpenseId(expenseId: Long): List<ExpensePaymentEntity>

    /**
     * 決済方法IDが一致するリスト取得
     */
    @Query("SELECT * FROM expense_payments WHERE payment_id IS (:paymentId)")
    suspend fun findByPaymentId(paymentId: Long): List<ExpensePaymentEntity>
}