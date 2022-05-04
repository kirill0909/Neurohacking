package com.neuro.hacking.fragments.lists.behavior.classes

import com.neuro.hacking.fragments.lists.behavior.interfaces.RemoveCategoryBehavior
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.neuro.hacking.model.Category
import com.neuro.hacking.databinding.DeleteCategoryDialogBinding
import com.neuro.hacking.viewmodel.CategoryViewModel
import android.view.View
import com.google.android.material.snackbar.Snackbar

class RemoveCategory : RemoveCategoryBehavior {

    private lateinit var removeCategoryDialogBinding: DeleteCategoryDialogBinding

    override fun removeCategory(
        context: Context,
        category: Category,
        categoryViewModel: CategoryViewModel,
        view: View
    ) {
        removeCategoryDialogBinding =
            DeleteCategoryDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setTitle("Are you sure you want to delete a category \"${category.category}\" ")
            .setView(removeCategoryDialogBinding.root)
            .setPositiveButton("Remove", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                categoryViewModel.deleteCategory(category)
                categoryViewModel.deleteWordByCategoryName(category.category)
                showSnackBar("Category \"${category.category}\" was deleted", view)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    /*
        *this method show snackBar
     */
    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}