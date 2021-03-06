package com.neuro.hacking.fragments.lists.words

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neuro.hacking.R
import com.neuro.hacking.viewmodel.WordViewModel
import com.neuro.hacking.model.Word
import com.neuro.hacking.databinding.FragmentWordsBinding
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.neuro.hacking.fragments.lists.behavior.classes.AddWordToDb
import com.neuro.hacking.fragments.lists.behavior.classes.UpdateWord
import com.neuro.hacking.fragments.lists.behavior.interfaces.AddWordToDbBehavior
import com.neuro.hacking.fragments.lists.behavior.interfaces.UpdateWordBehavior
import com.neuro.hacking.viewmodel.WordViewModelFactory
import java.lang.Exception

class WordsFragment : WordItemWorker(), WordClickListener, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentWordsBinding
    private val adapter by lazy { WordAdapter(this) }
    private lateinit var mWordViewModel: WordViewModel
    private val args: WordsFragmentArgs by navArgs()
    private val TAG = "WordsFragment"
    override var addWordToDbBehavior: AddWordToDbBehavior = AddWordToDb()
    override var updateWordBehavior: UpdateWordBehavior = UpdateWord()

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
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMain.root)
        (activity as AppCompatActivity).supportActionBar?.title = mWordViewModel.title
        binding.toolbarMain.root.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarMain.root.setNavigationOnClickListener {
            findNavController().navigate(WordsFragmentDirections.actionWordsFragmentToListFragment())
        }

        setHasOptionsMenu(true)

        fabAddWordListener(binding)
        return view
    }

    /*
    *This method listen add word button
     */
    private fun fabAddWordListener(binding: FragmentWordsBinding) {
        binding.fabAddWord.setOnClickListener {
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
                    performUpdate(requireContext(), word, mWordViewModel)
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