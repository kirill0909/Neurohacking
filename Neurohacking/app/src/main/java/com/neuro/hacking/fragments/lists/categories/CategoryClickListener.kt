package com.neuro.hacking.fragments.lists.categories

import android.view.View
import com.neuro.hacking.model.Category

interface CategoryClickListener {
    fun onItemClick(category: Category)
    fun onMoreButtonClick(category: Category, v: View)
}