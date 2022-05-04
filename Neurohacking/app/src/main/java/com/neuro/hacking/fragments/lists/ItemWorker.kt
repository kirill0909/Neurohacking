package com.neuro.hacking.fragments.lists

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddCategoryToDbBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateCategoryBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.RemoveCategoryBehavior
import android.content.Context
import androidx.fragment.app.Fragment
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View
import com.neuro.hacking.model.Category

open class ItemWorker : Fragment() {

    open lateinit var addCategoryToDbBehavior: AddCategoryToDbBehavior
    open lateinit var updateCategoryBehavior: UpdateCategoryBehavior
    open lateinit var removeCategoryBehavior: RemoveCategoryBehavior

    fun performAdd(context: Context, categoryViewModel: CategoryViewModel, view: View) {
        addCategoryToDbBehavior.addCategoryToDb(context, categoryViewModel, view)
    }

    fun performUpdate(
        context: Context,
        oldCategory: Category,
        categoryViewModel: CategoryViewModel,
        view: View
    ) {
        updateCategoryBehavior.updateCategory(context, oldCategory, categoryViewModel, view)
    }

    fun performRemove(
        context: Context,
        category: Category,
        categoryViewModel: CategoryViewModel,
        view: View
    ) {
        removeCategoryBehavior.removeCategory(context, category, categoryViewModel, view)
    }
}