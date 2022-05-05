package com.neuro.hacking.fragments.lists.categories

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddCategoryToDbBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateCategoryBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.RemoveCategoryBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.AddWordToDbBehavior
import android.content.Context
import androidx.fragment.app.Fragment
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.WordViewModel

open class CategoryItemWorker : Fragment() {

    open lateinit var addCategoryToDbBehavior: AddCategoryToDbBehavior
    open lateinit var updateCategoryBehavior: UpdateCategoryBehavior
    open lateinit var removeCategoryBehavior: RemoveCategoryBehavior
    open lateinit var addWordToDbBehavior: AddWordToDbBehavior

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

    fun performAddWord(context: Context, category: String, wordViewModel: WordViewModel, view: View) {
        addWordToDbBehavior.addWordToDb(context, category, wordViewModel, view)
    }
}