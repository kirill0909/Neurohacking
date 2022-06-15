package com.neuro.hacking.fragments.lists.behavior.classes

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddCategoryToDbBehavior
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.neuro.hacking.databinding.AddCategoryDialogBinding
import com.neuro.hacking.model.Category
import java.util.*
import com.neuro.hacking.viewmodel.CategoryViewModel

class AddCategoryToDb : AddCategoryToDbBehavior {

    private lateinit var addCategoryDialogBinding: AddCategoryDialogBinding

    /*
      *this method show alert dialog and add category to db
     */
    override fun addCategoryToDb(context: Context, categoryViewModel: CategoryViewModel, view: View) {
        addCategoryDialogBinding = AddCategoryDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setTitle("Creating a new category")
            .setView(addCategoryDialogBinding.root)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()
        dialog.setOnShowListener {
            addCategoryDialogBinding.edAddCategoryDialog.requestFocus()
            showKeyBoard(addCategoryDialogBinding.edAddCategoryDialog)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val categoryName =
                    addCategoryDialogBinding.edAddCategoryDialog.text.toString().trim()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if (checkInput(categoryName) && checkDuplicate(categoryName, categoryViewModel)) {
                    val category = Category(0, categoryName)
                    categoryViewModel.addCategory(category)
                    showSnackBar("Category \"${category.category}\" was successfully created", view)
                } else {
                    addCategoryDialogBinding.edAddCategoryDialog.error = "Invalid data"
                    return@setOnClickListener
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    /*
    *This method check duplicate inside category table before add category to it
     */
    private fun checkDuplicate(category: String, categoryViewModel: CategoryViewModel): Boolean {
        return categoryViewModel.allCategories.value?.map { it.category}?.contains(category) != true
    }

    private fun showKeyBoard(view: View) {
        getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    /*
    private fun hideKeyboard(view: View) {
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }
     */

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
}