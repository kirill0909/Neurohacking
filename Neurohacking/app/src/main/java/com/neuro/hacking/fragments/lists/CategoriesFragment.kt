package com.neuro.hacking.fragments.lists

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.neuro.hacking.R
import com.neuro.hacking.databinding.FragmentCategoriesBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.CategoryViewModel
import java.util.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class CategoriesFragment : Fragment(), CategoryClickListener {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var mCategoryViewModel: CategoryViewModel
    private val adapter by lazy { CategoryAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        binding = FragmentCategoriesBinding.bind(view)

        mCategoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mCategoryViewModel.allCategories.observe(viewLifecycleOwner, Observer { category ->
            adapter.setData(category)
        })

        fabAddButtonListener(binding)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeToDeleteCallBack = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val category = mCategoryViewModel.allCategories.value?.get(position)!!
                mCategoryViewModel.deleteCategory(category)
                showSnackBar(category, view)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    /*
        *this method listen fab add button
     */
    private fun fabAddButtonListener(binding: FragmentCategoriesBinding) {
        binding.fabAddCategories.setOnClickListener {
            insertCategoryToDb()
        }
    }

    /*
    *implements category click listener.
    * Navigate to list of words
     */
    override fun onItemClick(position: Int) {
        //toast("${mCategoryViewModel.allCategories.value?.get(position)?.category}")
        val categoryName = mCategoryViewModel.allCategories.value?.get(position)?.category
        val directions =
            CategoriesFragmentDirections.actionListFragmentToWordsFragment(categoryName!!)
        findNavController().navigate(directions)
    }

    /*
    *implements category long click listener.
    * Show alert dialog and update category name
     */
    override fun onItemLongClickListener(position: Int) {
        val oldCategoryId = mCategoryViewModel.allCategories.value?.get(position)?.id
        val oldCategoryName = mCategoryViewModel.allCategories.value?.get(position)?.category
        //toast("Long item click: ${mCategoryViewModel.allCategories.value?.get(position)?.category}")
        val editView = layoutInflater.inflate(R.layout.update_category, null)
        val editText = editView.findViewById(R.id.edit_text_update_category_dialog) as EditText
        val updateCategoryDialog = AlertDialog.Builder(requireContext())
        updateCategoryDialog.setTitle("Update category name")
        editText.setText(oldCategoryName)
        updateCategoryDialog.setView(editView)

        updateCategoryDialog.setPositiveButton("Update") { _, _ ->
            val newCategoryName = editText.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if (checkInput(newCategoryName)) {
                val category = Category(oldCategoryId!!, newCategoryName)
                mCategoryViewModel.updateCategory(category)
                toast("Category \"${category.category}\" has been updated")
            } else {
                toast("The category name cannot be empty")
            }
        }
        updateCategoryDialog.setNegativeButton("Cancel") { _, _ -> }
        updateCategoryDialog.show()
    }

    /*
        *this method show alert dialog and add word to db
     */
    private fun insertCategoryToDb() {
        val editView = layoutInflater.inflate(R.layout.add_category_dialog, null)
        val editText = editView.findViewById(R.id.edit_text_add_category_dialog) as EditText
        val addCategoryDialog = AlertDialog.Builder(requireContext())
        addCategoryDialog.setTitle("Enter new category name.")
        //addCategoryDialog.setMessage("Enter new category name.")
        addCategoryDialog.setView(editView)

        addCategoryDialog.setPositiveButton("Add") { _, _ ->
            val categoryName = editText.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if (checkInput(categoryName)) {
                val category = Category(0, categoryName)
                mCategoryViewModel.addCategory(category)
                toast("Category \"${category.category}\" was successfully created")
            } else {
                toast("The category name cannot be empty")
            }
        }
        addCategoryDialog.setNegativeButton("Cancel") { _, _ -> }

        addCategoryDialog.show()
    }

    /*
        *this method check user input
     */
    private fun checkInput(categoryName: String): Boolean {
        return !(TextUtils.isEmpty(categoryName))
    }

    /*
        *this method show snackBar
     */
    private fun showSnackBar(category: Category, view: View) {
        Snackbar.make(view, "The Category \"${category.category}\" deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                mCategoryViewModel.addCategory(category)
                toast("The Category \"${category.category}\" has been resumed")
            }
            .show()
    }

    /*
        *this method show notification
     */
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}