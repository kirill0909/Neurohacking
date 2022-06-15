package com.neuro.hacking

import org.junit.Test
import org.junit.Assert.*
import com.neuro.hacking.fragments.lists.behavior.classes.UpdateCategory

class UpdateCategoryTest {

    private val updateCategory = UpdateCategory()

    @Test
    fun updateCategoryTest() {
        assertEquals(true, updateCategory.checkInput("New Category"))
        assertEquals(false, updateCategory.checkInput(""))
    }

}