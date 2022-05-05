package com.neuro.hacking.fragments.lists.behavior.classes

import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateWordBehavior
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.neuro.hacking.model.Word
import com.neuro.hacking.databinding.UpdateWordDialogBinding
import java.util.*
import com.neuro.hacking.viewmodel.WordViewModel

class UpdateWord : UpdateWordBehavior{

    private lateinit var binding: UpdateWordDialogBinding

    override fun updateWord(context: Context, oldWord: Word, wordViewModel: WordViewModel) {
        binding = UpdateWordDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setTitle("Updating the \"${oldWord.word}\"")
            .setView(binding.root)
            .setPositiveButton("Update", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            binding.etUpdateWordDialogNative.setText(oldWord.word)
            binding.etUpdateWordDialogNative.requestFocus()
            binding.etUpdateWordDialogTranslation.setText(oldWord.translation)

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val newWordNative = binding.etUpdateWordDialogNative.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                val newWordTranslation = binding.etUpdateWordDialogTranslation.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if(checkInput(newWordNative, newWordTranslation)) {
                    val word = Word(oldWord.id, newWordNative, newWordTranslation, oldWord.category)
                    wordViewModel.updateWord(word)
                }else {
                    binding.etUpdateWordDialogNative.error = "Invalid data"
                    binding.etUpdateWordDialogTranslation.error = "Invalid data"
                    return@setOnClickListener
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    /*
      *this method check user input
    */
    private fun checkInput(wordNative: String, wordTranslation: String): Boolean {
        return !(TextUtils.isEmpty(wordNative) || TextUtils.isEmpty(wordTranslation))
    }
}