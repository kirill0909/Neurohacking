package com.neuro.hacking.fragments.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neuro.hacking.R
import com.neuro.hacking.databinding.ItemCategoryBinding
import com.neuro.hacking.model.Category
import com.neuro.hacking.fragments.lists.CategoryClickListener
import androidx.recyclerview.widget.DiffUtil

class CategoryAdapter(private val listener: CategoryClickListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    /*
    interface CategoryClickListener {
        fun onItemClick(position: Int)
    }
     */

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

    fun setData(newCategoriesList: List<Category>) {
        val diffUtil = MyDiffUtil(categoriesList, newCategoriesList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        categoriesList = newCategoriesList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        private val binding = ItemCategoryBinding.bind(itemView)

        val textViewCategory = binding.textViewCategory

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            val position: Int = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position: Int = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemLongClickListener(position)
                return true
            }
            return false
        }
    }
}