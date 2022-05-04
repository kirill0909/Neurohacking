package com.neuro.hacking.fragments.lists.behavior.interfaces

import android.content.Context
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View

interface AddToDbBehavior {

    fun addToDb(context: Context, categoryViewModel: CategoryViewModel, view: View)
}