package com.neuro.hacking.fragments.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neuro.hacking.R
import com.neuro.hacking.databinding.ItemCategoryBinding
import com.neuro.hacking.model.Category
import androidx.recyclerview.widget.DiffUtil

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoriesList = emptyList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoriesList[position]

        holder.textViewCategory.text = currentItem.category
    }

    override fun getItemCount() = categoriesList.size

    /*
    fun setData(categories: List<Category>) {
        this.categoriesList = categories
        notifyDataSetChanged()
    }
     */
    fun setData(newCategoriesList: List<Category>) {
        val diffUtil = MyDiffUtil(categoriesList, newCategoriesList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        categoriesList = newCategoriesList
        diffResults.dispatchUpdatesTo(this)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCategoryBinding.bind(itemView)

        val textViewCategory = binding.textViewCategory
    }
}