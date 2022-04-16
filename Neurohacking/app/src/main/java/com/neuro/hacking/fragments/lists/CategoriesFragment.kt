package com.neuro.hacking.fragments.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neuro.hacking.R
import com.neuro.hacking.databinding.FragmentCategoriesBinding
import android.widget.Toast
import androidx.navigation.fragment.findNavController

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get view
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        //get binding
        binding = FragmentCategoriesBinding.bind(view)
        //call method for listen to words button
        toWordsButtonListener(binding)
        return view
    }

    //this method listen to words button
    private fun toWordsButtonListener(binding: FragmentCategoriesBinding) {
        //create listener
        binding.toListWordsButton.setOnClickListener {
            //navigate to words list
            findNavController().navigate(R.id.action_listFragment_to_wordsFragment)
        }
    }

    //this method show notification
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}