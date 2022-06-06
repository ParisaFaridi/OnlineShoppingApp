package com.example.onlineshoppingapp.ui.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModelHome : HomeViewModel by viewModels()
    private lateinit var binding : FragmentHomeBinding
    private lateinit var bestProductsAdapter : ProductAdapter
    private lateinit var newProductsAdapter : ProductAdapter
    private lateinit var mostViewedProductsAdapter : ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViews()

        viewModelHome.bestProducts.observe(viewLifecycleOwner){
            it?.let {
                bestProductsAdapter.submitList(it)
            }
        }
        viewModelHome.newProducts.observe(viewLifecycleOwner){
            it?.let {
                newProductsAdapter.submitList(it)
            }
        }
        viewModelHome.mostViewedProducts.observe(viewLifecycleOwner){
            it?.let {
                mostViewedProductsAdapter.submitList(it)
            }
        }

    }

    private fun setRecyclerViews(){
        bestProductsAdapter = ProductAdapter{ product -> product.id?.let { it -> goToDetailFragment(it) } }
        newProductsAdapter = ProductAdapter{product -> product.id?.let { it -> goToDetailFragment(it) }}
        mostViewedProductsAdapter = ProductAdapter{product -> product.id?.let { it -> goToDetailFragment(it) }}

        binding.apply {
            rvBestProducts.adapter = bestProductsAdapter
            rvNewProducts.adapter = newProductsAdapter
            rvMostViewedProducts.adapter = mostViewedProductsAdapter
        }
    }
    private fun goToDetailFragment(id:Int){
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }
}