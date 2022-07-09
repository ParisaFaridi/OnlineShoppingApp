package com.example.onlineshoppingapp.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.DetailedItemAdapter
import com.example.onlineshoppingapp.databinding.FragmentSearchBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.example.onlineshoppingapp.ui.search.viewmodels.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    lateinit var binding :FragmentSearchBinding
    private val searchViewModel : SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearch.rootView,InputMethodManager.SHOW_IMPLICIT)
        binding.etSearch.requestFocus()
        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            return@setOnEditorActionListener false
        }
        val productsAdapter = DetailedItemAdapter { product ->
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(product.id!!)
            findNavController().navigate(action)
        }
        binding.rvProducts.adapter = productsAdapter
        searchViewModel.searchResults.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { data -> productsAdapter.submitList(data) }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
        }
        }
        var job : Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable.let {
                    if (editable.toString().isNotEmpty())
                        searchViewModel.search(editable.toString(),4)
                }
            }
        }
        binding.btnSearch.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(binding.etSearch.text.toString())
            findNavController().navigate(action)
        }
    }
    private fun hideProgressBar() {
        binding.rvProducts.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.rvProducts.visibility = View.GONE
        binding.lottie.visibility = View.VISIBLE
        binding.lottie.playAnimation()
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            searchViewModel.search(binding.etSearch.toString(),4)
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}