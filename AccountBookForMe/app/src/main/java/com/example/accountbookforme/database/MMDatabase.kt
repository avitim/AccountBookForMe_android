package com.example.accountbookforme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.accountbookforme.database.dao.CategoryDao
import com.example.accountbookforme.database.dao.ExpenseDao
import com.example.accountbookforme.database.dao.PaymentDao
import com.example.accountbookforme.database.dao.StoreDao
import com.example.accountbookforme.database.entity.CategoryEntity
import com.example.accountbookforme.database.entity.ExpenseDetailEntity
import com.example.accountbookforme.database.entity.PaymentEntity
import com.example.accountbookforme.database.entity.StoreEntity

@Database(
    entities = [ExpenseDetailEntity::class, CategoryEntity::class, PaymentEntity::class, StoreEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class MMDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
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
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
            }

        // マイグレーション
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `payments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
                database.execSQL("CREATE TABLE IF NOT EXISTS `stores` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `expense_details` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `purchased_at` TEXT NOT NULL, `total` TEXT NOT NULL, `store_id` INTEGER, `store_name` TEXT, `note` TEXT NOT NULL, FOREIGN KEY(`store_id`) REFERENCES `stores`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_expense_details_store_id` ON `expense_details` (`store_id`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_stores_id` ON `stores` (`id`)")
            }
        }
    }
}