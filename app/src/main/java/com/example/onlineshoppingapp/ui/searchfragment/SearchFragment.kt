package com.example.onlineshoppingapp.ui.searchfragment

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
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    lateinit var binding :FragmentSearchBinding
    val searchViewModel : SearchViewModel by viewModels()

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
        imm.showSoftInput(binding.et.rootView,InputMethodManager.SHOW_IMPLICIT)
        binding.et.requestFocus()
        binding.et.setOnEditorActionListener { textView, i, keyEvent ->
            //get editText value
            return@setOnEditorActionListener false
        }
        val productsAdapter = ProductAdapter { product ->
            val action = product.id?.let { id ->
                (id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = productsAdapter
        searchViewModel.searchResults.observe(viewLifecycleOwner){ response ->
            when (response) {
            is Resource.Success -> {
                response.data?.let { data ->
                    productsAdapter.submitList(data)
                }
            }
        }
        }
        var job : Job? = null
        binding.et.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable.let {
                    if (editable.toString().isNotEmpty())
                        searchViewModel.search(editable.toString())
                }
            }
        }
    }
}