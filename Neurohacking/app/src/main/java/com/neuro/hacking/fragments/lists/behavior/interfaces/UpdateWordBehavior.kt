package com.neuro.hacking.fragments.lists.behavior.interfaces

import android.content.Context
import com.neuro.hacking.model.Word
import com.neuro.hacking.viewmodel.WordViewModel

interface UpdateWordBehavior {

    fun updateWord(context: Context, word: Word, wordViewModel: WordViewModel)

}