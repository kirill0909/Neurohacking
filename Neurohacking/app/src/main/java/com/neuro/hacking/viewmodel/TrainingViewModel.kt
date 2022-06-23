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
import androidx.compose.ui.text.capitalize
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

    private var _numberOfWords = MutableLiveData<Int>(0)
    val numberOfWords: LiveData<Int>
        get() = _numberOfWords

    private var _wordCount = MutableLiveData<Int>(0)
    val wordCount: LiveData<Int>
        get() = _wordCount

    private var _incorrect = MutableLiveData<Int>(0)
    val incorrect: LiveData<Int>
        get() = _incorrect

    private var _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

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
            _wordCount.value = 0
            _numberOfWords.value = getNumberWords(_words)
            _incorrect.value = 0
            //_correct.value = 0
            _currentWord.value = getRandomWord(_words)


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
    private fun getRandomWord(words: List<Word>): Word {
        val wordsByCategory = mutableListOf<Word>()
        var word: Word
        words.forEach { if (it.category == _selectedCategory.value) wordsByCategory.add(it) }
        wordsByCategory.shuffle()
        try {
            word = wordsByCategory.random()
            return word
        }catch(e: NoSuchElementException) {
            Log.d("TrainingViewModel", "Collection ${_selectedCategory} is empty")
        }
        word = Word(0, "", "The category \"${_selectedCategory.value.toString()}\" is empty", "")
        return word
    }

    /*
    *This method show dialog with categories
     */
    fun selectCategoryDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Select category")
            .setSingleChoiceItems(
                categories.map { it.category }.toTypedArray(), 0
            ) { dialog, which ->
                _selectedCategory.value = categories[which].category
                reinitializeData(
                    categories[which].category, getNumberWords(_words), 0,
                    0,
                    0,
                    getRandomWord(_words)
                )

                dialog.dismiss()
            }
        dialog.create().show()
    }

    /*
    *This method show final dialog
     */
    fun finalDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Your results")
            .setMessage(
                "Category: ${_selectedCategory.value}\n" +
                        "Total words: ${_numberOfWords.value}\n" +
                        "Incorrect:${_incorrect.value}\n"
            )

            .setPositiveButton("Train again") { _, _ ->
                Log.d("TrainingViewModel", "You tap on the play again")
                selectCategoryDialog(context)
            }
            .setNegativeButton("Exit") { _, _ ->
                //stop training
            }
        dialog.create().show()
    }

    private fun getNextWord() {
        _currentWord.value = getRandomWord(_words)
    }

    /*
    *re-initializes the  data to restart.
     */
    private fun reinitializeData(
        selectedCategory: String,
        numberOfWords: Int,
        wordCount: Int,
        incorrectValue: Int,
        correctValue: Int,
        currentWord: Word
    ) {
        _selectedCategory.value = selectedCategory//categories[which].category
        _numberOfWords.value = numberOfWords//getNumberWords(_words)
        _wordCount.value = wordCount//0
        _incorrect.value = incorrectValue//0
        //_correct.value = correctValue//0
        _currentWord.value = currentWord//getRandomWord(_words)
    }

    /*
    *Returns true if the current word count is less then _numberOfWords.
    * Update the next word.
     */
    fun nextWord(): Boolean {
        return if (_wordCount.value!! < _numberOfWords.value!!) {
            getNextWord()
            true
        } else {
            return false
        }
    }

    /*
    *This method check user input
     */
    fun isUserWordCorrect(userWord: String): Boolean {
        if (userWord.equals(_currentWord.value?.word, true)) {
            inCreasePositiveIndicators()
            return true
        } else {
            inCreaseNegativeIndicators()
        }
        return false
    }

    /*
    *This method increase indicators (_wordCount, _correct)
     */
    private fun inCreasePositiveIndicators() {
        _wordCount.value = (_wordCount.value)?.plus(1)
       // _correct.value = (_correct.value)?.plus(1)
    }

    /*
    *This method increase indicators (_incorrect)
     */
    private fun inCreaseNegativeIndicators() {
        _incorrect.value = (_incorrect.value)?.plus(1)
    }


}