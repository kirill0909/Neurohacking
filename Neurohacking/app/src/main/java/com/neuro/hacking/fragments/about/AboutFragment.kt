package com.neuro.hacking.fragments.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.neuro.hacking.R
import com.neuro.hacking.fragments.about.AboutFragmentDirections
import com.neuro.hacking.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        binding = FragmentAboutBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarAbout)
        (activity as AppCompatActivity).supportActionBar?.title = "About"
        binding.toolbarAbout.setNavigationIcon(R.drawable.ic_back)
        binding.toolbarAbout.setNavigationOnClickListener {
            findNavController().navigate(AboutFragmentDirections.actionAboutFragmentToListFragment())
        }

        return view
    }
}