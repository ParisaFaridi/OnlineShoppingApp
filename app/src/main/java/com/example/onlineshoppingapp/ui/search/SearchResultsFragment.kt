package com.example.onlineshoppingapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.DetailedItemAdapter
import com.example.onlineshoppingapp.databinding.FragmentSearchResultsBinding
import com.example.onlineshoppingapp.ui.search.viewmodels.FilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {

    private val args : SearchResultsFragmentArgs by navArgs()
    lateinit var binding : FragmentSearchResultsBinding
    val searchViewModel : FilterViewModel by activityViewModels()
    private lateinit var productAdapter: DetailedItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.searchQuery = args.searchQuery
        binding.etSearch.setText(args.searchQuery)
        setAdapter()
        binding.sortSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView : AdapterView<*>?, view: View?, position : Int, id: Long) {
                when (position) {
                    0 -> searchViewModel.search(query = args.searchQuery,orderBy = getString(R.string.title))
                    1 -> searchViewModel.search(orderBy = getString(R.string.date))
                    2 -> searchViewModel.search(orderBy = getString(R.string.popularity))
                    3 -> searchViewModel.search(orderBy = getString(R.string.rating))
                    4 -> searchViewModel.search(orderBy = getString(R.string.price))
                    else -> searchViewModel.search(orderBy = getString(R.string.price), order =  getString(R.string.asc))
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchResultsFragment_to_filterFragment)
        }
        binding.btnSearch.setOnClickListener {
            searchViewModel.search(query = binding.etSearch.text.toString(), orderBy = getString(R.string.title))
        }
        searchViewModel.searchResults.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { data ->
                        productAdapter.submitList(data)
                    }
                }
            }
        }
    }
    private fun setAdapter() {
        productAdapter = DetailedItemAdapter { product ->
            val action = product.id?.let {
                SearchResultsFragmentDirections.actionSearchResultsFragmentToDetailFragment(it)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        binding.rvProducts.adapter = productAdapter

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
}