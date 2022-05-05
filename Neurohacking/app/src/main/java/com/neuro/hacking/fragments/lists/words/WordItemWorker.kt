package com.neuro.hacking.fragments.lists.words

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddWordToDbBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateWordBehavior
import android.content.Context
import androidx.fragment.app.Fragment
import com.neuro.hacking.viewmodel.WordViewModel
import android.view.View
import com.neuro.hacking.model.Word

open class WordItemWorker : Fragment() {

    open lateinit var addWordToDbBehavior: AddWordToDbBehavior
    open lateinit var updateWordBehavior: UpdateWordBehavior

    fun performAdd(context: Context, category: String, wordViewModel: WordViewModel, view: View) {
        addWordToDbBehavior.addWordToDb(context, category, wordViewModel, view)
    }

    fun performUpdate(context: Context, word: Word, wordViewModel: WordViewModel) {
        updateWordBehavior.updateWord(context, word, wordViewModel)
    }
}