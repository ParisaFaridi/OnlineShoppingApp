package com.example.onlineshoppingapp.ui.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.AttributeItemsAdapter
import com.example.onlineshoppingapp.databinding.FragmentSizeFilterBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SizeFilterFragment : Fragment() {

    private val filterVm : FilterViewModel by activityViewModels()
    lateinit var binding : FragmentSizeFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSizeFilterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AttributeItemsAdapter {
            it.isSelected = !it.isSelected
        }
        binding.rvFilterItems.adapter = adapter

        if (filterVm.sizeFilters.value == null)
            filterVm.getFilters(4,filterVm.sizeFilters)

        filterVm.sizeFilters.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> { response.data?.let {
                    hideProgressBar()
                    adapter.submitList(it)
                }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }}
                }
            }
        }
    }
    private fun hideProgressBar() {
        binding.apply {
            lottie.visibility = View.GONE
            rvFilterItems.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar() = binding.lottie.apply {
        setAnimation(R.raw.loading)
        visibility = View.VISIBLE
        playAnimation()
        binding.rvFilterItems.visibility = View.GONE
    }

    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            filterVm.getFilters(4,filterVm.sizeFilters)
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}