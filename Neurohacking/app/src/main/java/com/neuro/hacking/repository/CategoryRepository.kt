package com.neuro.hacking.repository

import androidx.lifecycle.LiveData
import com.neuro.hacking.data.CategoryDao
import com.neuro.hacking.model.Category

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun addCategory(category: Category) {
        categoryDao.addCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}