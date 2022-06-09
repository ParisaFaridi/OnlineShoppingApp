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
        setRecyclerViews()
        if (viewModelHome.bestProducts.value == null) {
            getLists()
        }
        viewModelHome.bestProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success -> {
                    response.data?.let { data ->
                        showData(bestProductsAdapter, data)
                    }
                }
                is Resource.Error -> { response.message?.let { showSnack(it) } }
            }
        }
        viewModelHome.newProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success -> {
                    response.data?.let { data ->
                        binding.layout.visibility = View.VISIBLE
                        binding.lottie.visibility = View.GONE
                        newProductsAdapter.submitList(data)
                    }
                }
                is Resource.Error -> { response.message?.let { showSnack(it) } }
            }
        }
        viewModelHome.mostViewedProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    response.data?.let { data ->
                        mostViewedProductsAdapter.submitList(data)
                        binding.layout.visibility = View.VISIBLE
                        binding.lottie.visibility = View.GONE
                    }
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
            getLists()
            binding.lottie.playAnimation()
        }
        snackBar.show()
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