package com.neuro.hacking.fragments.lists.behavior.interfaces

import android.content.Context
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View

interface RemoveCategoryBehavior {

    fun removeCategory(
        context: Context,
        category: Category,
        categoryViewModel: CategoryViewModel,
        view: View
    )
}