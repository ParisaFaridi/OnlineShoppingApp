package com.example.onlineshoppingapp.ui.home.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.DetailedItemAdapter
import com.example.onlineshoppingapp.databinding.FragmentProductListBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : BaseFragment() {

    lateinit var binding : FragmentProductListBinding
    val viewModel : ProductListViewModel by viewModels()
    lateinit var adapter:DetailedItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         viewModel.getProducts()
        setAdapter()
        viewModel.productList.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(binding.rvProducts,binding.lottie)
                }
                is Resource.Success -> {
                    hideProgressBar(binding.rvProducts,binding.lottie)
                    response.data?.let { adapter.submitList(response.data) }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        response.code?.let { code -> showErrorSnack(message, code) }
                    }
                }
            }
        }
    }
    private fun setAdapter() {
        adapter = DetailedItemAdapter{
                product ->
            val action = product.id?.let { id ->
                ProductListFragmentDirections.actionProductListFragmentToDetailFragment(id)
            }
            if (action != null)
                findNavController().navigate(action)
        }
        binding.rvProducts.adapter = adapter

    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout,
            getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.try_again)) {
            viewModel.getProducts()
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}