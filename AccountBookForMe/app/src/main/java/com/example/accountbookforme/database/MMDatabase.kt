package com.example.accountbookforme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.accountbookforme.dao.CategoryDao
import com.example.accountbookforme.entity.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class MMDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object {

        // シングルトンで使うため
        @Volatile
        private var instance: MMDatabase? = null

        fun getDatabase(context: Context): MMDatabase =
            instance ?: synchronized(this) {
                // インスタンス生成
                Room.databaseBuilder(context.applicationContext, MMDatabase::class.java, "db")
                    .build()
            }
    }
}