package com.neuro.hacking.fragments.lists.words

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddWordToDbBehavior
import android.content.Context
import androidx.fragment.app.Fragment
import com.neuro.hacking.viewmodel.WordViewModel
import android.view.View

open class WordItemWorker : Fragment() {

    open lateinit var addWordToDbBehavior: AddWordToDbBehavior

    fun performAdd(context: Context, category: String, wordViewModel: WordViewModel, view: View) {
        addWordToDbBehavior.addWordToDb(context, category, wordViewModel, view)
    }
}