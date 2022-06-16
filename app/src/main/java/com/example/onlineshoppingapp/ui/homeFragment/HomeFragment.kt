package com.example.onlineshoppingapp.ui.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.databinding.FragmentHomeBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModelHome: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bestProductsAdapter: ProductAdapter
    private lateinit var newProductsAdapter: ProductAdapter
    private lateinit var mostViewedProductsAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "صفحه اصلی"
        setRecyclerViews()
        if (viewModelHome.bestProducts.value == null) {
            getLists()
        }
        viewModelHome.bestProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        showData(bestProductsAdapter, data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {  message -> response.code?.let { code -> showErrorSnack(message, code) }
                    }

                }
            }
        }
        viewModelHome.newProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data -> showData(newProductsAdapter, data) }
                }
                is Resource.Error -> {
                    response.message?.let {  message -> response.code?.let { code -> showErrorSnack(message, code) }
                    }
                }
            }
        }
        viewModelHome.mostViewedProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data -> showData(mostViewedProductsAdapter, data) }
                }
                is Resource.Error -> {
                    response.message?.let {  message -> response.code?.let { code -> showErrorSnack(message, code) }
                    }
                }
            }
        }
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            getLists()
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }

    private fun showProgressBar() = binding.lottie.apply {
        setAnimation(R.raw.loading)
        visibility = View.VISIBLE
        playAnimation()
        binding.layout.visibility = View.GONE
    }
    private fun getLists() {
        viewModelHome.apply {
            getNewProducts()
            getMostViewedProducts()
            getBestProducts()
        }
    }
    private fun showData(adapter: ProductAdapter, data: List<Product>) {
        binding.layout.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
        adapter.submitList(data)
    }
    private fun setRecyclerViews() {
        bestProductsAdapter =
            ProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }
        newProductsAdapter =
            ProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }
        mostViewedProductsAdapter =
            ProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }

        binding.apply {
            rvBestProducts.adapter = bestProductsAdapter
            rvNewProducts.adapter = newProductsAdapter
            rvMostViewedProducts.adapter = mostViewedProductsAdapter
        }
    }
    private fun goToDetailFragment(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }
}