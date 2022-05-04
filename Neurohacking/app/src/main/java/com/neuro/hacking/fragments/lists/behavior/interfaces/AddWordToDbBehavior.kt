package com.neuro.hacking.fragments.lists.behavior.interfaces

import android.content.Context
import com.neuro.hacking.viewmodel.WordViewModel
import android.view.View

interface AddWordToDbBehavior {

    fun addWordToDb(context: Context, category: String, wordViewModel: WordViewModel, view: View)

}