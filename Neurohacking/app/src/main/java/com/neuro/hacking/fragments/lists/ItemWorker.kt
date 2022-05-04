package com.neuro.hacking.fragments.lists

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddToDbBehavior
import android.content.Context
import androidx.fragment.app.Fragment
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View

open class ItemWorker : Fragment() {

    open lateinit var addToDbBehavior: AddToDbBehavior

    fun performAdd(context: Context, categoryViewModel: CategoryViewModel, view: View) {
        addToDbBehavior.addToDb(context, categoryViewModel, view)
    }
}