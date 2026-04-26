package com.spendsmart.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val amount: Double = 0.0,
    val type: String = "",        // "income" sau "expense"
    val category: String = "",    // "Food", "Transport", "Health"
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val recurring: String = "",   // "daily", "weekly", "monthly" sau ""
    val isSynced: Boolean = false
)