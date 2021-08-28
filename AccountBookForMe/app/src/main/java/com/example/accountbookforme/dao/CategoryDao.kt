package com.example.accountbookforme.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.accountbookforme.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    /**
     * 全件取得
     */
    @Query("SELECT * FROM categories")
    fun findAll(): Flow<List<CategoryEntity>>

    /**
     * 新規作成
     */
    @Insert
    suspend fun create(categoryEntity: CategoryEntity)

    /**
     * 更新
     */
    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    /**
     * 削除
     */
    @Query("DELETE FROM categories WHERE id IS (:id)")
    suspend fun deleteById(id: Long)
}