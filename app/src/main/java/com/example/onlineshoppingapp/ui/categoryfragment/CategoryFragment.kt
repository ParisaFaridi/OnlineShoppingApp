package com.example.onlineshoppingapp.ui.categoryfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoryBinding
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
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(),2)
        val productsAdapter = ProductAdapter{ product ->
            val action = product.id?.let { id ->
                CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = productsAdapter
        categoryViewModel.getCategoryById(args.categoryId).observe(viewLifecycleOwner){
            productsAdapter.submitList(it)
        }
    }
}