package com.example.onlineshoppingapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.DetailedItemAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoryBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.category.viewmodels.CategoryViewModel
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : BaseFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var binding: FragmentCategoryBinding
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var productsAdapter :DetailedItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        if (categoryViewModel.products.value == null)
            categoryViewModel.getProductsByCategoryId(args.categoryId)

        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_searchFragment)
        }
        categoryViewModel.products.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(binding.rvProducts,binding.lottie)
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        hideProgressBar(binding.rvProducts,binding.lottie)
                        productsAdapter.submitList(data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        response.code?.let { code -> showErrorSnack(message, code) } }
                }
            }
        }
    }
    private fun setAdapter() {
        productsAdapter = DetailedItemAdapter { product ->
            val action = product.id?.let { id ->
                CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(id)
            }
            if (action != null)
                findNavController().navigate(action)
        }
        binding.rvProducts.adapter = productsAdapter
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            categoryViewModel.getProductsByCategoryId(args.categoryId)
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}