package com.neuro.hacking

import org.junit.Test
import org.junit.Assert.*
import com.neuro.hacking.fragments.lists.behavior.classes.AddWordToDb

class AddWordToDbTest {

    private val addWordToDb = AddWordToDb()

    @Test
    fun userInputCheckTest() {
        assertEquals(true, addWordToDb.checkInput("Body", "Тело"))
        assertEquals(false, addWordToDb.checkInput("", ""))
    }
}