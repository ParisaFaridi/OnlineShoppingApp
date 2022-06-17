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
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.CategoryAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoriesBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOrientation()
        activity?.title = getString(R.string.categories)
        categoriesAdapter = CategoryAdapter { category ->
            val action = category.id?.let { id ->
                category.name?.let { name ->
                    CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment(id, name)
                }
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvCategories.adapter = categoriesAdapter

        if (categoriesViewModel.categories.value == null)
            categoriesViewModel.getCategories()

        categoriesViewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        hideProgressBar()
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
    private fun setOrientation() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 3)
        } else {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
    private fun hideProgressBar() = binding.apply {
        lottie.visibility = View.GONE
        rvCategories.visibility = View.VISIBLE
    }
    private fun showProgressBar() = binding.lottie.apply {
        setAnimation(R.raw.loading)
        visibility = View.VISIBLE
        playAnimation()
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