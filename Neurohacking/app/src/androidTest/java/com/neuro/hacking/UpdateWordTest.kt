package com.neuro.hacking

import org.junit.Test
import org.junit.Assert.*
import com.neuro.hacking.fragments.lists.behavior.classes.UpdateWord

class UpdateWordTest {

    private val updateWord = UpdateWord()

    @Test
    fun updateWordTest() {
        assertEquals(true, updateWord.checkInput("new word", "новое слово"))
        assertEquals(false, updateWord.checkInput("", ""))
    }
}