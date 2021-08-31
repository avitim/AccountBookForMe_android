package com.example.accountbookforme.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.accountbookforme.database.entity.ExpenseDetailEntity
import com.example.accountbookforme.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    /**
     * 支出全件取得
     */
    @Query("SELECT expense_details.id, purchased_at, 0 AS total, store_name, stores.name AS storeNameByStoreId FROM expense_details LEFT JOIN stores ON expense_details.store_id IS stores.id")
    fun findAll(): Flow<List<Expense>>

    /**
     * 店舗IDから支出リストを取得
     */
    @Query("SELECT expense_details.id, purchased_at, 0 AS total, store_name, stores.name AS storeNameByStoreId FROM expense_details LEFT JOIN stores ON expense_details.store_id IS stores.id WHERE store_id IS (:storeId)")
    suspend fun findByStoreId(storeId: Long): List<Expense>

    /**
     * IDから支出詳細を取得
     */
    @Query("SELECT * FROM expense_details WHERE id IS (:id)")
    suspend fun findById(id: Long): ExpenseDetailEntity

    /**
     * 支出詳細新規作成
     */
    @Insert
    suspend fun create(expenseDetailEntity: ExpenseDetailEntity): Long

    /**
     * 支出詳細更新
     */
    @Update
    suspend fun update(expenseDetailEntity: ExpenseDetailEntity)

    /**
     * 支出詳細削除
     */
    @Query("DELETE FROM expense_details WHERE id IS (:id)")
    suspend fun deleteById(id: Long)
}