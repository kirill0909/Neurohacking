package com.neuro.hacking.fragments.lists.behavior.classes

import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateBehavior
import android.util.Log
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.neuro.hacking.databinding.UpdateCategoryDialogBinding
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.CategoryViewModel
import java.util.*

class UpdateCategory : UpdateBehavior {

    private lateinit var updateCategoryDialogBinding: UpdateCategoryDialogBinding

    override fun update(
        context: Context,
        oldCategory: Category,
        categoryViewModel: CategoryViewModel,
        view: View
    ) {
        //realization
        Log.d("UpdateCategory", "update() from UpdateCategory")
        updateCategoryDialogBinding =
            UpdateCategoryDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setTitle("Updating the \"${oldCategory.category}\" category")
            .setView(updateCategoryDialogBinding.root)
            .setPositiveButton("Update", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            updateCategoryDialogBinding.etUpdateCategoryDialog.setText(oldCategory.category)
            updateCategoryDialogBinding.etUpdateCategoryDialog.requestFocus()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val newCategory =
                    updateCategoryDialogBinding.etUpdateCategoryDialog.text.toString().trim()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if (checkInput(newCategory)) {
                    val category = Category(oldCategory.id, newCategory)
                    categoryViewModel.updateCategory(category)
                    categoryViewModel.updateWordByCategoryName(category.category, oldCategory.category)
                    showSnackBar("Category \"${category.category}\" was successfully updated",view)
                } else {
                    updateCategoryDialogBinding.etUpdateCategoryDialog.error = "Value is empty"
                    return@setOnClickListener
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    private fun showKeyBoard(view: View) {
        getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
}

/*
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
                if (checkInput(categoryName)) {
                    val category = Category(0, categoryName)
                    categoryViewModel.addCategory(category)
                    showSnackBar("Category \"${category.category}\" was successfully created", view)
                } else {
                    addCategoryDialogBinding.edAddCategoryDialog.error = "Value is empty"
                    return@setOnClickListener
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
 */