package com.spendsmart.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",   // "Food", "Transport", "Health"
    val iconRes: String = "",
    val colorHex: String = ""
)