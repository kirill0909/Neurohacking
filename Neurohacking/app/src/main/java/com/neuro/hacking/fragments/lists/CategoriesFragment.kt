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
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.javafaker.Faker

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
        //set adapter and layout manager
        val adapter = CategoryAdapter()
        adapter.setData(getFakerList(20))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    //this method return faker list
    private fun getFakerList(num: Int): MutableList<String> {
        var fakerList = mutableListOf<String>()
        val faker = Faker.instance()
        for(i in 0..num) {
            fakerList.add(faker.name().name())
        }
        return fakerList

    }

    //this method show notification
    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}