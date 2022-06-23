package com.neuro.hacking.fragments.training

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.neuro.hacking.R
import com.neuro.hacking.viewmodel.TrainingViewModel
import com.neuro.hacking.databinding.FragmentTrainingBinding
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.neuro.hacking.fragments.lists.words.WordsFragmentDirections

class TrainingFragment : Fragment() {

    private lateinit var mTrainingViewModel: TrainingViewModel
    private lateinit var binding: FragmentTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_training, container, false)
        binding = FragmentTrainingBinding.bind(view)

        mTrainingViewModel = ViewModelProvider(this)[TrainingViewModel::class.java]

        mTrainingViewModel.selectedCategory.observe(viewLifecycleOwner) { category ->
            binding.tvCategory.text = getString(R.string.category, category)
        }

        mTrainingViewModel.wordCount.observe(viewLifecycleOwner) { wordCount ->
            binding.tvWordCount.text = getString(R.string.word_count, wordCount)
        }

        mTrainingViewModel.numberOfWords.observe(viewLifecycleOwner) { numberOfWords ->
            binding.tvNumberOfWords.text = getString(R.string.number_of_words, numberOfWords)
        }

        mTrainingViewModel.incorrect.observe(viewLifecycleOwner) { incorrect ->
            binding.tvIncorrect.text = getString(R.string.incorrect, incorrect)
        }

        mTrainingViewModel.currentWord.observe(viewLifecycleOwner) { word ->
            binding.tvTranslationWord.text = word.translation
        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMain.root)
        (activity as AppCompatActivity).supportActionBar?.title = mTrainingViewModel.title
        binding.toolbarMain.root.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarMain.root.setNavigationOnClickListener {
            findNavController().navigate(TrainingFragmentDirections.actionTrainingFragmentToListFragment())
        }

        binding.submitButton.setOnClickListener { onSubmitButton() }
        binding.skipButton.setOnClickListener { onSkipButton() }
        binding.tvTranslationWord.setOnClickListener { showHint() }

        setHasOptionsMenu(true)
        return view
    }

    /*
    *This method listen submit button
     */
    private fun onSubmitButton() {
        val userWord = binding.textInputEditText.text.toString().trim()
        if (mTrainingViewModel.isUserWordCorrect(userWord)) {
            setErrorTextField(false)
            if (!mTrainingViewModel.nextWord()) {
                mTrainingViewModel.finalDialog(requireContext())
            }
        } else {
            setErrorTextField(true)
        }
    }

    /*
    *This method listen skip button
     */
    private fun onSkipButton() {
        if (mTrainingViewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            mTrainingViewModel.finalDialog(requireContext())
        }
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.edNativeWord.isErrorEnabled = true
            binding.edNativeWord.error = getString(R.string.try_again)
        } else {
            binding.edNativeWord.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /*
    *This method show hint when user tap on the word
     */
    private fun showHint() {
        toast(mTrainingViewModel.currentWord.value?.word.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_category -> {
                mTrainingViewModel.selectCategoryDialog(requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.training_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
        *this method show notification
     */
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}