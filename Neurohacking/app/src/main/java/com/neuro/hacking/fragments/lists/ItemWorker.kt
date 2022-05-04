package com.neuro.hacking.fragments.lists

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddToDbBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateBehavior
import android.content.Context
import androidx.fragment.app.Fragment
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View
import com.neuro.hacking.model.Category

open class ItemWorker : Fragment() {

    open lateinit var addToDbBehavior: AddToDbBehavior
    open lateinit var updateBehavior: UpdateBehavior

    fun performAdd(context: Context, categoryViewModel: CategoryViewModel, view: View) {
        addToDbBehavior.addToDb(context, categoryViewModel, view)
    }

    fun performUpdate(context: Context, oldCategory: Category, categoryViewModel: CategoryViewModel, view: View) {
        updateBehavior.update(context, oldCategory, categoryViewModel, view)
    }
}