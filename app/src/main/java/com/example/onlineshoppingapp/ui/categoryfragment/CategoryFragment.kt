package com.example.onlineshoppingapp.ui.categoryfragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoryBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var binding: FragmentCategoryBinding
    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = args.categoryName
        setOrientation()
        if (categoryViewModel.products.value == null)
            categoryViewModel.getProductsByCategoryId(args.categoryId)

        val productsAdapter = ProductAdapter { product ->
            val action = product.id?.let { id ->
                CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = productsAdapter
        categoryViewModel.products.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        hideProgressBar()
                        productsAdapter.submitList(data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        response.code?.let { code -> showError(message, code) } }
                }
            }
        }
    }

    private fun showError(message: String,code:Int) {
        when{
            code in (400..499) -> {
            //show lottie animation snack
            }
            code in (500..599) ->{
                //show lottie animation and snack
            }
            code ==0 ->{
                //show inknown error
            }
            code == 1 ->{
                //show now internet connection
            }
        }
    }

    private fun setOrientation() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 3)
        } else {
            binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun hideProgressBar() = binding.apply {
        rvProducts.visibility = View.VISIBLE
        lottie.visibility = View.GONE
    }

    private fun showProgressBar() = binding.lottie.apply {
        setAnimation(R.raw.loading)
        visibility = View.VISIBLE
        playAnimation()
    }

    private fun showSnack(message: String) {
        val snackBar = Snackbar.make(binding.layout, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            "تلاش دوباره"
        ) {
            categoryViewModel.getProductsByCategoryId(args.categoryId)
        }
        snackBar.show()
    }
}