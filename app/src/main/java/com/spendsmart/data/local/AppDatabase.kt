package com.spendsmart.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spendsmart.data.local.dao.BudgetDao
import com.spendsmart.data.local.dao.CategoryDao
import com.spendsmart.data.local.dao.TransactionDao
import com.spendsmart.data.model.Budget
import com.spendsmart.data.model.Category
import com.spendsmart.data.model.Transaction

@Database(entities = [Transaction::class, Category::class, Budget::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spendsmart_database"
                )
                .fallbackToDestructiveMigration() // Șterge și recreează DB dacă schema se schimbă (ideal pentru dezvoltare)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}