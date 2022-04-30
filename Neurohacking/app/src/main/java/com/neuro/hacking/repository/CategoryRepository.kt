package com.neuro.hacking.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.neuro.hacking.data.CategoryDao
import com.neuro.hacking.model.Category
import com.neuro.hacking.data.WordDao
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao, private val wordDao: WordDao) {

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

    suspend fun deleteWordByCategoryName(category: String) {
        categoryDao.deleteWordByCategoryName(category)
    }

    suspend fun updateWordByCategoryName(oldCategory: String, newCategory: String) {
        categoryDao.updateWordByCategoryName(oldCategory, newCategory)
    }

    fun searchCategory(category: String): Flow<List<Category>> {
        return categoryDao.searchCategory(category)
    }
}