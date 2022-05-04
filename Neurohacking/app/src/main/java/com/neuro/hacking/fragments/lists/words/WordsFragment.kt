package com.neuro.hacking.fragments.lists.words

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neuro.hacking.R
import com.neuro.hacking.viewmodel.WordViewModel
import com.neuro.hacking.model.Word
import com.neuro.hacking.databinding.FragmentWordsBinding
import java.util.*
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.neuro.hacking.fragments.lists.behavior.classes.AddWordToDb
import com.neuro.hacking.fragments.lists.behavior.interfaces.AddWordToDbBehavior
import com.neuro.hacking.model.Category
import com.neuro.hacking.viewmodel.WordViewModelFactory
import java.lang.Exception

class WordsFragment : WordItemWorker(), WordClickListener, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentWordsBinding
    private val adapter by lazy { WordAdapter(this) }
    private lateinit var mWordViewModel: WordViewModel
    private val args: WordsFragmentArgs by navArgs()
    private val TAG = "WordsFragment"
    override var addWordToDbBehavior: AddWordToDbBehavior = AddWordToDb()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_words, container, false)
        binding = FragmentWordsBinding.bind(view)

        val viewModelWordFactory = WordViewModelFactory(args.categoryName)
        mWordViewModel = ViewModelProvider(this, viewModelWordFactory)[WordViewModel::class.java]

        binding.recyclerViewWord.adapter = adapter
        binding.recyclerViewWord.layoutManager = LinearLayoutManager(requireContext())

        mWordViewModel.wordsByCategory.observe(viewLifecycleOwner) { word ->
            adapter.setData(word)
        }

        mWordViewModel.title = args.categoryName
        activity?.title = mWordViewModel.title

        setHasOptionsMenu(true)

        fabAddWordListener(binding)
        return view
    }

    /*
    *This method listen add word button
     */
    private fun fabAddWordListener(binding: FragmentWordsBinding) {
        binding.fabAddWord.setOnClickListener {
            //insertWordToDbDialog()
            performAdd(requireContext(), args.categoryName, mWordViewModel, requireView())
        }
    }

    override fun onClick(word: Word) {
        toast(word.translation)
    }

    /*
    *Show popup menu and
    * processing click on the item inside popup menu
     */
    override fun onMoreButtonClick(word: Word, v: View) {
        val popupMenu = PopupMenu(requireContext(), v, Gravity.END)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.context_delete -> {
                    mWordViewModel.deleteWord(word)
                    showSnackBar("Word: \"${word.word}\" was deleted", requireView())
                    true
                }
                R.id.context_update -> {
                    updateWord(word)
                    true
                }
                else -> false
            }

        }
        popupMenu.inflate(R.menu.word_context_menu)
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
    *This method to update word
     */
    private fun updateWord(word: Word) {
        val dialogView = layoutInflater.inflate(R.layout.update_word_dialog, null)
        val editTextNative = dialogView.findViewById(R.id.edit_text_update_word_dialog_native) as EditText
        val editTextTranslation = dialogView.findViewById(R.id.edit_text_update_word_dialog_translation) as EditText
        editTextNative.setText(word.word)
        editTextTranslation.setText(word.translation)
        val updateWordDialog = AlertDialog.Builder(requireContext())
        updateWordDialog.setTitle("Update Word \"${word.word}\"")
        updateWordDialog.setView(dialogView)

        updateWordDialog.setPositiveButton("Update") { _, _ ->
            val wordNative = editTextNative.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val wordTranslation = editTextTranslation.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if (checkInput(wordNative, wordTranslation)) {
                val word = Word(word.id, wordNative, wordTranslation, word.category)
                mWordViewModel.updateWord(word)
                showSnackBar("The Word \"${word.word}:${word.translation}\" has been updated", requireView())
            } else {
                toast("The category name cannot be empty")
            }
        }
        updateWordDialog.setNegativeButton("Cancel") { _, _ -> }

        updateWordDialog.show()
    }


    /*
      *this method show alert dialog and add word to db
    */
    private fun insertWordToDbDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_word_to_db_dialog, null)
        val editTextNative =
            dialogView.findViewById(R.id.et_add_word_to_db_native) as EditText
        val editTextTranslation =
            dialogView.findViewById(R.id.et_add_word_to_db_translation) as EditText
        val addWordDialog = AlertDialog.Builder(requireContext())
        addWordDialog.setTitle("Add word to category \"${args.categoryName}\".")
        //addCategoryDialog.setMessage("Enter new category name.")
        addWordDialog.setView(dialogView)

        addWordDialog.setPositiveButton("Add") { _, _ ->
            val wordNative = editTextNative.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val wordTranslation = editTextTranslation.text.toString().trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            if (checkInput(wordNative, wordTranslation)) {
                val word = Word(0, wordNative, wordTranslation, args.categoryName)
                mWordViewModel.addWord(word)
                showSnackBar(
                    "Word \"${word.word}:${word.translation}\" was successfully added to db",
                    requireView()
                )
            } else {
                toast("You need to fill in all the fields")
            }
        }
        addWordDialog.setNegativeButton("Cancel") { _, _ -> }

        addWordDialog.show()
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
            searchWord(query, args.categoryName)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchWord(query, args.categoryName)
        }
        return true
    }

    private fun searchWord(query: String, category: String) {
        val searchQuery = "%$query%"

        mWordViewModel.searchWord(searchQuery, category).observe(this) { list ->
            list.let {
                adapter.setData(it)
            }
        }
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

    /*
        *this method show notification
     */
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}