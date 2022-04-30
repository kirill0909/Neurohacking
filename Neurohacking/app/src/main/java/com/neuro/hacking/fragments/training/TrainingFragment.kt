package com.neuro.hacking.fragments.training

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.neuro.hacking.R
import com.neuro.hacking.viewmodel.TrainingViewModel
import com.neuro.hacking.databinding.FragmentTrainingBinding

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

        //Log.d("TrainingFragment", mTrainingViewModel.categories.toString())

        mTrainingViewModel.selectedCategory.observe(viewLifecycleOwner) { category ->
            binding.tvCategory.text = getString(R.string.category, category)
        }

        mTrainingViewModel.wordCount.observe(viewLifecycleOwner) { wordCount ->
            binding.tvWordCount.text = getString(R.string.word_count, 0, wordCount)
        }

        mTrainingViewModel.incorrect.observe(viewLifecycleOwner) { incorrect ->
            binding.tvIncorrect.text = getString(R.string.incorrect, incorrect)
        }

        mTrainingViewModel.correct.observe(viewLifecycleOwner) { correct ->
            binding.tvCorrect.text = getString(R.string.correct, correct)
        }

        mTrainingViewModel.word.observe(viewLifecycleOwner) { word ->
            binding.tvTranslationWord.text = word
        }

        activity?.title = mTrainingViewModel.title

        setHasOptionsMenu(true)
        return view
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