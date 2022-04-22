package com.neuro.hacking.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WordViewModelFactory(private val category: String) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java))
            return WordViewModel(application = Application(), category) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}