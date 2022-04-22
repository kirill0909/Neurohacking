package com.neuro.hacking.fragments.lists.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neuro.hacking.R
import com.neuro.hacking.databinding.ItemCategoryBinding
import com.neuro.hacking.model.Category
import androidx.recyclerview.widget.DiffUtil

class CategoryAdapter(private val listener: CategoryClickListener,) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoriesList = emptyList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoriesList[position]

        holder.textViewCategory.text = category.category
        holder.textViewCategory.setOnClickListener {
            listener.onItemClick(category)
        }
        holder.imageViewMoreButton.setOnClickListener {
            listener.onMoreButtonClick(category, holder.itemView)
        }
    }

    override fun getItemCount() = categoriesList.size

    fun setData(newCategoriesList: List<Category>) {
        val diffUtil = CategoriesDiffUtil(categoriesList, newCategoriesList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        categoriesList = newCategoriesList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemCategoryBinding.bind(itemView)

        val textViewCategory = binding.textViewCategory
        val imageViewMoreButton = binding.imageViewMoreButton
    }


}
