package com.example.onlineshoppingapp.ui.categoriesfragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.CategoryAdapter
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentCategoriesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoriesViewModel : CategoriesViewModel by viewModels()
    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoriesAdapter :CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesAdapter = CategoryAdapter{ category ->
            val action = category.id?.let { id ->
                CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment(id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvCategories.adapter = categoriesAdapter

        if (categoriesViewModel.categories.value == null)
            categoriesViewModel.getCategories()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(),3)
        } else {
            binding.rvCategories.layoutManager = GridLayoutManager(requireContext(),2)
        }
        categoriesViewModel.categories.observe(viewLifecycleOwner) { response ->
            when(response){
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success ->{
                    response.data?.let { data ->
                        binding.lottie.visibility = View.GONE
                        binding.rvCategories.visibility = View.VISIBLE
                        categoriesAdapter.submitList(data)
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
            categoriesViewModel.getCategories()
        }
        snackBar.show()
    }
}