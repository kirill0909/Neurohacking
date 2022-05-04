package com.neuro.hacking.fragments.lists.behavior.interfaces

import android.content.Context
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View

interface AddCategoryToDbBehavior {

    fun addCategoryToDb(context: Context, categoryViewModel: CategoryViewModel, view: View)
}