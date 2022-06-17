package com.example.onlineshoppingapp.ui.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.DetailedItemAdapter
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentProductListBinding
import com.example.onlineshoppingapp.ui.categoryfragment.CategoryFragmentDirections
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private val args:ProductListFragmentArgs by navArgs()
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
         getProducts()

        val adapter = DetailedItemAdapter{
                product ->
            val action = product.id?.let { id ->
                ProductListFragmentDirections.actionProductListFragmentToDetailFragment(id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = adapter

        viewModel.productList.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Loading -> { showProgressBar()}
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { adapter.submitList(response.data) }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }
                    }
                }
            }
        }
    }

    private fun getProducts() {
        if (args.orderBy == getString(R.string.on_sale))
            viewModel.getProducts(getString(R.string.date),true)
        else
            viewModel.getProducts(args.orderBy)
    }

    private fun showProgressBar(){
        binding.rvProducts.visibility = View.GONE
        binding.lottie.visibility - View.VISIBLE
    }
    private fun hideProgressBar(){
        binding.rvProducts.visibility = View.VISIBLE
        binding.lottie.visibility - View.GONE
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.try_again)) {
            getProducts()
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}