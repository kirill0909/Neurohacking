package com.neuro.hacking

import org.junit.Test
import org.junit.Assert.*
import com.neuro.hacking.fragments.lists.behavior.classes.AddCategoryToDb

class AddCategoryToDbTest {

    private val addCategoryToDb = AddCategoryToDb()

    @Test
    fun userInputCheckTest() {
        assertEquals(true, addCategoryToDb.checkInput("Body"))
        assertEquals(false, addCategoryToDb.checkInput(""))
    }
}