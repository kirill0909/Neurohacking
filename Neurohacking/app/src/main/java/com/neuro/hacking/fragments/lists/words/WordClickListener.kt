package com.neuro.hacking.fragments.lists.words

import com.neuro.hacking.model.Word
import android.view.View

interface WordClickListener {
    fun onClick(word: Word)
    fun onMoreButtonClick(word: Word, v: View)
}