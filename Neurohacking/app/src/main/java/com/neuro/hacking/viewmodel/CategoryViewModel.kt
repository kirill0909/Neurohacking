package com.neuro.hacking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.neuro.hacking.data.AppDatabase
import com.neuro.hacking.model.Category
import com.neuro.hacking.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    val allCategories: LiveData<List<Category>>
    private val repository: CategoryRepository

    init {
        val categoryDao = AppDatabase.getDatabase(application).categoryDao()
        val wordDao = AppDatabase.getDatabase(application).wordDao()
        repository = CategoryRepository(categoryDao, wordDao)
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

    fun deleteWordByCategoryName(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWordByCategoryName(category)
        }
    }

    fun updateWordByCategoryName(oldCategory: String, newCategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWordByCategoryName(oldCategory, newCategory)
        }
    }

    fun searchCategory(category: String): LiveData<List<Category>> {
        return repository.searchCategory(category).asLiveData()
    }
}