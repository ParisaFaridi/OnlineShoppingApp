package com.example.onlineshoppingapp.ui.searchfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.databinding.FragmentSearchResultsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {

    private val args : SearchResultsFragmentArgs by navArgs()
    lateinit var binding : FragmentSearchResultsBinding
    val searchViewModel : FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.search(args.searchQuery,50,"مرتبط ترین")
        binding.etSearch.setText(args.searchQuery)

        binding.sortSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView : AdapterView<*>?, view: View?, position : Int, id: Long) {
                when (position) {
                    0 -> searchViewModel.search(args.searchQuery, 50, "title")
                    1 -> searchViewModel.search(args.searchQuery, 50, "date")
                    2 -> searchViewModel.search(args.searchQuery, 50, "popularity")
                    3 -> searchViewModel.search(args.searchQuery, 50, "rating")
                    4 -> searchViewModel.search(args.searchQuery, 50, "price")
                    else -> searchViewModel.search(args.searchQuery, 50, "price", "asc")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchResultsFragment_to_filterFragment)
        }
        val productsAdapter = ProductAdapter { product ->
            val action = product.id?.let {
                SearchResultsFragmentDirections.actionSearchResultsFragmentToDetailFragment(
                    it
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = productsAdapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(),2)

        searchViewModel.searchResults.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        productsAdapter.submitList(data)
                    }
                }
            }
        }
    }
}