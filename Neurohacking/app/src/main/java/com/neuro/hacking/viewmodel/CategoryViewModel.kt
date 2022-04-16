package com.neuro.hacking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.neuro.hacking.data.CategoryDatabase
import com.neuro.hacking.model.Category
import com.neuro.hacking.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    val allCategories: LiveData<List<Category>>
    private val repository: CategoryRepository

    init {
        val categoryDao = CategoryDatabase.getDatabase(application).categoryDao()
        repository = CategoryRepository(categoryDao)
        allCategories = repository.allCategories
    }

    fun addCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCategory(category)
        }
    }
}