package com.example.onlineshoppingapp.ui.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    val args:ProductListFragmentArgs by navArgs()
    lateinit var binding : FragmentProductListBinding
    val viewModel :HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts(args.orderBy)

        val adapter = ProductAdapter{}
        binding.rvProducts.adapter = adapter

        viewModel.productList.observe(viewLifecycleOwner){ response ->
            when (response) {

                is Resource.Success -> {
                    response.data?.let { data ->
                        adapter.submitList(response.data)
                    }
                }
                is Resource.Error -> {
                }
            }

        }

    }

}