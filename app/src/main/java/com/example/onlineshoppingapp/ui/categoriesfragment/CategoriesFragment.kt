package com.example.onlineshoppingapp.ui.categoriesfragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(),3)
        } else {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(),2)
        }

        val categoriesAdapter = CategoryAdapter{ category ->
            val action = category.id?.let { id ->
                CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment(id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvCategories.adapter = categoriesAdapter

        categoriesViewModel.categories.observe(viewLifecycleOwner){
            categoriesAdapter.submitList(it)
        }
    }
}