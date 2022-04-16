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
import androidx.recyclerview.widget.LinearLayoutManager
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.CategoryViewModel
import java.util.*

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var mCategoryViewModel: CategoryViewModel
    private val adapter by lazy { CategoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get view
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        //get binding
        binding = FragmentCategoriesBinding.bind(view)
        //Create Category view model
        mCategoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        //set adapter and layout manager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //Observer for category list
        mCategoryViewModel.allCategories.observe(viewLifecycleOwner, Observer { category ->
            adapter.setData(category)
        })

        //call method fro listen fab button
        fabAddButtonListener(binding)
        return view
    }

    //this method listen fab add button
    private fun fabAddButtonListener(binding: FragmentCategoriesBinding) {
        //create listener
        binding.fabAddCategories.setOnClickListener {
            //call method for add category to db
            addCategoryToDb()
        }
    }

    //this method show alert dialog and add word to db
    private fun addCategoryToDb() {
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
                insertData(categoryName)
            } else {
                toast("The category name cannot be empty")
            }
        }
        addCategoryDialog.setNegativeButton("Cancel") { _, _ -> }

        addCategoryDialog.show()
    }

    //this method insert data to db
    private fun insertData(categoryName: String) {
        //create object category
        val category = Category(0, categoryName)
        //insert to db
        mCategoryViewModel.addCategory(category)
        toast("Category \"${category.category}\" was successfully created")
    }

    //this method check user input
    private fun checkInput(categoryName: String): Boolean {
        return !(TextUtils.isEmpty(categoryName))
    }

    //this method show notification
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}