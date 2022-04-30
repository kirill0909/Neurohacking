package com.neuro.hacking.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.neuro.hacking.data.AppDatabase
import com.neuro.hacking.model.Category
import com.neuro.hacking.model.Word
import com.neuro.hacking.repository.TrainingRepository
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class TrainingViewModel(application: Application) : AndroidViewModel(application) {


    private lateinit var _categories: List<Category>
    val categories: List<Category>
        get() = _categories

    private lateinit var _words: List<Word>
    val words: List<Word>
        get() = _words

    private var _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    private var _wordCount = MutableLiveData<Int>(0)
    val wordCount: LiveData<Int>
        get() = _wordCount

    private var _incorrect = MutableLiveData<Int>(0)
    val incorrect: LiveData<Int>
        get() = _incorrect

    private var _correct = MutableLiveData<Int>(0)
    val correct: LiveData<Int>
        get() = _correct

    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    private val repository: TrainingRepository
    val title: String = "Training"

    init {
        val categoryDao = AppDatabase.getDatabase(application).categoryDao()
        val wordDao = AppDatabase.getDatabase(application).wordDao()
        repository = TrainingRepository(categoryDao, wordDao)
        CoroutineScope(IO).launch { setDefaultTraining() }
        Log.d("TrainingViewModel", " has been created")
    }

    /*
    *This method set default data on the training page use coroutine
     */
    private suspend fun setDefaultTraining() {
        getDataFromDb()
        withContext(Main) {
            _selectedCategory.value = categories[0].category
            _wordCount.value = getNumberWords(words)
            _incorrect.value = 0
            _correct.value = 0
            _word.value = getRandomWord(_words, _selectedCategory.value!!)
            //Log.d("TrainingViewModel", getRandomWord(_words, _selectedCategory.value!!))
        }
    }

    /*
    *This method get data from db use coroutine
     */
    private suspend fun getDataFromDb() {
        withContext(Main) {
            _categories = repository.getCategories()
            _words = repository.getWords()
        }
    }

    /*
    *This method count number words in th words list with specific category
     */
    private fun getNumberWords(words: List<Word>): Int {
        var count = 0
        words.forEach { if (it.category == _selectedCategory.value) count++ }
        return count
    }

    /*
    *This method return random word(translation) by category name
     */
    private fun getRandomWord(words: List<Word>, category: String): String {
        val wordsByCategory = mutableListOf<Word>()
        words.forEach { if (it.category == _selectedCategory.value) wordsByCategory.add(it) }
        return wordsByCategory.random().translation
    }

    /*
    *This method show dialog with categories
     */
    fun selectCategoryDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Select category")
            .setSingleChoiceItems(
                categories.map { it.category }.toTypedArray(),
                0
            ) { dialog, which ->
                _selectedCategory.value = categories[which].category
                Log.d("TrainingViewModel", "You are selected ${_selectedCategory.value}")
                //start new game
                dialog.dismiss()
            }
        dialog.create().show()
    }

    /*
    fun selectCategoryDialog(context: Context) {

        val dialog = AlertDialog.Builder(context)
            .setTitle("Select category")
            .setSingleChoiceItems(categories.map { it.category }.toTypedArray(), 0) { dialog, which ->
                _selectedCategory.value = categories[which].category
                Log.d("TrainingViewModel", "You are selected ${_selectedCategory.value}")
                //start new game
                dialog.dismiss()
            }
        dialog.create().show()
    }

    /*
    *This method get data from db use coroutine
     */
    private suspend fun getDataFromDb() {
        withContext(Main) {
            _categories = repository.getCategories()
            _words = repository.getWords()
            setDefaultTraining(_categories, _words)
        }
    }

    /*
    *This method set default data on the training page use coroutine
     */
    private fun setDefaultTraining(categories: List<Category>, words: List<Word>) {
        _selectedCategory.value = categories[0].category
        _wordCount.value = getNumberWords(words)
        _incorrect.value = 0
        _correct.value = 0
        //Log.d("TrainingViewModel", _categories.toString())
        //Log.d("TrainingViewModel", _wordCount.value.toString())
    }

    /*
    *This method count number words in th words list with specific category
     */
    private fun getNumberWords(words: List<Word>): Int {
        var count = 0
        words.forEach { if(it.category == _selectedCategory.value) count++ }
        return count
    }
     */
}