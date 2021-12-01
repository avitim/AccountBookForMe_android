package com.example.accountbookforme.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.accountbookforme.database.entity.ItemEntity

@Dao
interface ItemDao {

    /**
     * 新規作成
     */
    @Insert
    suspend fun insert(itemEntity: ItemEntity)

    /**
     * 更新
     */
    @Update
    suspend fun update(itemEntity: ItemEntity)

    /**
     * 削除
     */
    @Query("DELETE FROM items WHERE id IS (:id)")
    suspend fun deleteById(id: Long)

    /**
     * 支出IDが一致するリスト取得
     */
    @Query("SELECT * FROM items WHERE expense_id IS (:expenseId)")
    suspend fun findByExpenseId(expenseId: Long): List<ItemEntity>

    /**
     * カテゴリIDが一致するリスト取得
     */
    @Query("SELECT * FROM items WHERE category_id IS (:categoryId)")
    suspend fun findByCategoryId(categoryId: Long): List<ItemEntity>
}