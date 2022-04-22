package com.neuro.hacking.repository

import androidx.lifecycle.LiveData
import com.neuro.hacking.data.WordDao
import com.neuro.hacking.model.Word

class WordRepository(private val wordDao: WordDao, private val category: String) {

    val allWords: LiveData<List<Word>> = wordDao.getAllWords()
    val wordsByCategory: LiveData<List<Word>> = wordDao.getWordsByCategory(category)

    suspend fun addWord(word: Word) {
        wordDao.addWord(word)
    }

    suspend fun updateWord(word: Word) {
        wordDao.updateWord(word)
    }

    suspend fun deleteWord(word: Word) {
        wordDao.deleteWord(word)
    }

}