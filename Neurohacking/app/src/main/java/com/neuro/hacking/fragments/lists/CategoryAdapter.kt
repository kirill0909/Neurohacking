package com.neuro.hacking.fragments.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neuro.hacking.R
import com.neuro.hacking.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private lateinit var categoriesList: MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoriesList[position]

        holder.textViewCategory.text = currentItem
    }

    override fun getItemCount() = categoriesList.size

    fun setData(categories: MutableList<String>) {
        this.categoriesList = categories
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCategoryBinding.bind(itemView)

        val textViewCategory = binding.textViewCategory
    }
}