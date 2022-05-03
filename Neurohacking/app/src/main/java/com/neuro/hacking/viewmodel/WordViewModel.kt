package com.neuro.hacking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.neuro.hacking.data.AppDatabase
import com.neuro.hacking.model.Category
import com.neuro.hacking.model.Word
import com.neuro.hacking.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application, category: String) : AndroidViewModel(application) {

    val allWords: LiveData<List<Word>>
    val wordsByCategory: LiveData<List<Word>>
    private val repository: WordRepository
    lateinit var title: String

    init {
        val wordDao = AppDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao, category)
        allWords = repository.allWords
        wordsByCategory = repository.wordsByCategory

    }

    fun addWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWord(word)
        }
    }

    fun updateWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWord(word)
        }
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(word)
        }
    }

    fun searchWord(word: String, category: String): LiveData<List<Word>> {
        return repository.searchWord(word, category).asLiveData()
    }

}