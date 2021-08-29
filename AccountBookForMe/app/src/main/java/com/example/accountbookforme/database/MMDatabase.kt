package com.example.accountbookforme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.accountbookforme.database.dao.CategoryDao
import com.example.accountbookforme.database.dao.PaymentDao
import com.example.accountbookforme.database.dao.StoreDao
import com.example.accountbookforme.database.entity.CategoryEntity
import com.example.accountbookforme.database.entity.PaymentEntity
import com.example.accountbookforme.database.entity.StoreEntity

@Database(entities = [CategoryEntity::class, PaymentEntity::class, StoreEntity::class], version = 2)
abstract class MMDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun paymentDao(): PaymentDao
    abstract fun storeDao(): StoreDao

    companion object {

        // シングルトンで使うため
        @Volatile
        private var instance: MMDatabase? = null

        fun getDatabase(context: Context): MMDatabase =
            instance ?: synchronized(this) {
                // インスタンス生成
                Room.databaseBuilder(context.applicationContext, MMDatabase::class.java, "db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }

        // マイグレーション
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `payments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS `stores` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
            }
        }
    }
}