package com.spendsmart.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "",
    val totalLimit: Double = 0.0,
    val foodLimit: Double = 0.0,
    val transportLimit: Double = 0.0,
    val healthLimit: Double = 0.0,
    val funLimit: Double = 0.0,
    val shoppingLimit: Double = 0.0,
    val billsLimit: Double = 0.0,
    val educationLimit: Double = 0.0,
    val month: Int = 0,
    val year: Int = 0,
    val isSynced: Boolean = false
)