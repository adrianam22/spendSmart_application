package com.spendsmart.data.repository

import com.spendsmart.data.local.dao.CategoryDao
import com.spendsmart.data.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insert(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun delete(category: Category) {
        categoryDao.deleteCategory(category)
    }
}