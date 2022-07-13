package com.example.onlineshoppingapp.ui.category

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.CategoryAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoriesBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.category.viewmodels.CategoriesViewModel
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment() {

    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOrientation()
        setAdapter()

        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_searchFragment)
        }
        if (categoriesViewModel.categories.value == null)
            categoriesViewModel.getCategories()

        categoriesViewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(binding.rvCategories,binding.lottie)
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        hideProgressBar(binding.rvCategories,binding.lottie)
                        categoriesAdapter.submitList(data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        categoriesAdapter = CategoryAdapter { category ->
            val action = category.id?.let { id ->
                category.name?.let { name ->
                    CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment(id, name)
                }
            }
            if (action != null)
                findNavController().navigate(action)
        }
        binding.rvCategories.adapter = categoriesAdapter
    }
    private fun setOrientation() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 3)
        } else {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            categoriesViewModel.getCategories()
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}