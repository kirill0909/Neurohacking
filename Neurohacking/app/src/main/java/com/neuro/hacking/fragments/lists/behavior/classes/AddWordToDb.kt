package com.neuro.hacking.fragments.lists.behavior.classes

import com.neuro.hacking.fragments.lists.behavior.interfaces.AddWordToDbBehavior
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
import com.neuro.hacking.databinding.AddWordToDbDialogBinding
import java.util.*
import com.neuro.hacking.model.Word
import com.neuro.hacking.viewmodel.WordViewModel

class AddWordToDb : AddWordToDbBehavior {

    private lateinit var binding: AddWordToDbDialogBinding

    override fun addWordToDb(
        context: Context,
        category: String,
        wordViewModel: WordViewModel,
        view: View
    ) {
        binding = AddWordToDbDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setTitle("Add Word to category \"$category\"")
            .setView(binding.root)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()
        dialog.setOnShowListener {
            binding.etAddWordToDbNative.requestFocus()
            showKeyBoard(binding.etAddWordToDbNative)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val wordNative = binding.etAddWordToDbNative.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                val wordTranslation = binding.etAddWordToDbTranslation.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if (checkInput(wordNative, wordTranslation) && checkDuplicate(wordNative, wordViewModel)) {
                    val word = Word(0, wordNative, wordTranslation, category)
                    wordViewModel.addWord(word)
                    showSnackBar("Word ${word.word} was add to category $category", view)
                } else {
                    binding.etAddWordToDbNative.error = "Invalid data"
                    binding.etAddWordToDbTranslation.error = "Invalid data"
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
    *This method check duplicate inside word table before add word to it
     */
    private fun checkDuplicate(wordNative: String, wordViewModel: WordViewModel): Boolean {
        return wordViewModel.wordsByCategory.value?.map { it.word }?.contains(wordNative) != true
    }

    /*
      *this method check user input
    */
    private fun checkInput(wordNative: String, wordTranslation: String): Boolean {
        return !(TextUtils.isEmpty(wordNative) || TextUtils.isEmpty(wordTranslation))
    }

    /*
        *this method show snackBar
     */
    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

}