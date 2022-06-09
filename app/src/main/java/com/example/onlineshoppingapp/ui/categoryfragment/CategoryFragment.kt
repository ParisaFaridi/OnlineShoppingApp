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

    private val categoryViewModel : CategoryViewModel by viewModels()
    private lateinit var binding : FragmentCategoryBinding
    private val args : CategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvProducts.layoutManager = GridLayoutManager(requireContext(),3)
        } else {
            binding.rvProducts.layoutManager = GridLayoutManager(requireContext(),2)
        }
        if (categoryViewModel.products.value == null)
            categoryViewModel.getProductsByCategoryId(args.categoryId)

        val productsAdapter = ProductAdapter{ product ->
            val action = product.id?.let { id ->
                CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = productsAdapter
        categoryViewModel.products.observe(viewLifecycleOwner){ response ->
            when(response){
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success ->{
                    response.data?.let { data ->
                        productsAdapter.submitList(data)
                    }
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.lottie.visibility = View.GONE
                }
                is Resource.Error -> { response.message?.let { showSnack(it) } }
            }
        }
    }
    private fun showProgressBar() {
        binding.lottie.setAnimation(R.raw.loading)
        binding.lottie.visibility = View.VISIBLE
        binding.lottie.playAnimation()

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