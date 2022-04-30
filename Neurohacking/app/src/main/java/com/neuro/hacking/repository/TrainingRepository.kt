package com.neuro.hacking.repository

import com.neuro.hacking.data.CategoryDao
import com.neuro.hacking.data.WordDao
import com.neuro.hacking.model.Category
import com.neuro.hacking.model.Word

class TrainingRepository(private val categoryDao: CategoryDao, private val wordDao: WordDao) {

    suspend fun getCategories(): List<Category> {
        return categoryDao.getCategories()
    }

    suspend fun getWords(): List<Word> {
        return wordDao.getWords()
    }

}