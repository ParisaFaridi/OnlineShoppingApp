package com.example.onlineshoppingapp.ui.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlineshoppingapp.data.Product
import com.example.onlineshoppingapp.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    //private val viewModelHome : HomeViewModel by viewModel()
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding =FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bestProductsAdapter = ProductAdapter{}
        binding.rvBestProducts.apply {
            adapter = bestProductsAdapter
            bestProductsAdapter.submitList(listOf(Product(1),Product(2),Product(3),Product(4),
                Product(5),
            ))
        }
    }
}