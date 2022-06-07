package com.example.onlineshoppingapp.ui.categoriesfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.adapters.CategoryAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoriesViewModel : CategoriesViewModel by viewModels()
    private lateinit var binding : FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesAdapter = CategoryAdapter{}
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = categoriesAdapter
        }
        categoriesViewModel.categories.observe(viewLifecycleOwner){
            categoriesAdapter.submitList(it)
        }
    }
}