package com.neuro.hacking.fragments.lists.categories

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.EditText
import com.neuro.hacking.R
import com.neuro.hacking.databinding.FragmentCategoriesBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.CategoryViewModel
import java.util.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import com.neuro.hacking.fragments.lists.ItemWorker
import com.neuro.hacking.fragments.lists.behavior.classes.AddCategoryToDb
import com.neuro.hacking.fragments.lists.behavior.classes.UpdateCategory
import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.AddToDbBehavior

class CategoriesFragment : ItemWorker(), CategoryClickListener, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentCategoriesBinding
    lateinit var mCategoryViewModel: CategoryViewModel
    private val adapter by lazy { CategoryAdapter(this) }
    override var addToDbBehavior: AddToDbBehavior = AddCategoryToDb()
    override var updateBehavior: UpdateBehavior = UpdateCategory()
    private val TAG = "CategoriesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        binding = FragmentCategoriesBinding.bind(view)

        mCategoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mCategoryViewModel.allCategories.observe(viewLifecycleOwner) { category ->
            adapter.setData(category)
        }

        setHasOptionsMenu(true)

        fabAddButtonListener(binding)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /*
        *this method listen fab add button
     */
    private fun fabAddButtonListener(binding: FragmentCategoriesBinding) {
        binding.fabAddCategories.setOnClickListener {
            performAdd(requireContext(), mCategoryViewModel, requireView())
        }
    }

    /*
    *implements category click listener.
    * Navigate to list of words
     */
    override fun onItemClick(category: Category) {
        val directions =
            CategoriesFragmentDirections.actionListFragmentToWordsFragment(category.category)
        findNavController().navigate(directions)
    }

    /*
    *Show popup menu and
    * processing click on the item inside popup menu
     */
    override fun onMoreButtonClick(category: Category, v: View) {
        val popupMenu = PopupMenu(requireContext(), v, Gravity.END)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.context_delete -> {
                    deleteCategoryDialog(category)
                    true
                }
                R.id.context_update -> {
                    //updateCategoryDialog(category)
                    performUpdate(requireContext(), category, mCategoryViewModel, requireView())
                    true
                }
                else -> false
            }

        }
        popupMenu.inflate(R.menu.category_context_menu)
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.d(TAG, "Error showing menu icons", e)
        } finally {
            popupMenu.show()
        }
    }

    /*
    *this method to rename category
     */
    private fun updateCategoryDialog(oldCategory: Category) {
        val dialogView = layoutInflater.inflate(R.layout.update_category_dialog, null)
        val editText = dialogView.findViewById(R.id.et_update_category_dialog) as EditText
        editText.setText(oldCategory.category)
        val updateCategoryDialog = AlertDialog.Builder(requireContext())
        updateCategoryDialog.setTitle("Update Category \"${oldCategory.category}\"")
        updateCategoryDialog.setView(dialogView)

        updateCategoryDialog.setPositiveButton("Update") { _, _ ->
            val categoryName = editText.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if (checkInput(categoryName)) {
                val category = Category(oldCategory.id, categoryName)
                mCategoryViewModel.updateCategory(category)
                mCategoryViewModel.updateWordByCategoryName(categoryName, oldCategory.category)
                showSnackBar("The Category \"${category.category}\" has been updated", requireView())
            } else {
                toast("The category name cannot be empty")
            }
        }
        updateCategoryDialog.setNegativeButton("Cancel") { _, _ -> }

        updateCategoryDialog.show()
    }
    /*
    *This method show alert dialog before category removing
     */
    private fun deleteCategoryDialog(category: Category) {
        val dialogView = layoutInflater.inflate(R.layout.delete_category_dialog, null)
        val deleteCategoryDialog = AlertDialog.Builder(requireContext())
        deleteCategoryDialog.setTitle("Are you sure you want to delete category \"${category.category}\"?")
        deleteCategoryDialog.setView(dialogView)

        deleteCategoryDialog.setPositiveButton("Delete") { _, _ ->
            mCategoryViewModel.deleteCategory(category)
            mCategoryViewModel.deleteWordByCategoryName(category.category)
            showSnackBar("Category \"${category.category}\" was deleted", requireView())
        }
        deleteCategoryDialog.setNegativeButton("Cancel") { _, _ -> }

        deleteCategoryDialog.show()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        activity?.menuInflater?.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchCategory(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchCategory(query)
        }
        return true
    }

    private fun searchCategory(query: String) {
        val searchQuery = "%$query%"

        mCategoryViewModel.searchCategory(searchQuery).observe(this) { list ->
            list.let {
                adapter.setData(it)
            }
        }
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
    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    /*
        *this method show notification
     */
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}