package com.example.accountbookforme.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.accountbookforme.database.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    /**
     * 全件取得
     */
    @Query("SELECT * FROM stores")
    fun findAll(): Flow<List<StoreEntity>>

    /**
     * 1件取得
     */
    @Query("SELECT * FROM stores WHERE id IS (:id)")
    suspend fun findById(id: Long): StoreEntity

    /**
     * 新規作成
     */
    @Insert
    suspend fun create(storeEntity: StoreEntity)

    /**
     * 更新
     */
    @Update
    suspend fun update(storeEntity: StoreEntity)

    /**
     * 削除
     */
    @Query("DELETE FROM stores WHERE id IS (:id)")
    suspend fun deleteById(id: Long)
}