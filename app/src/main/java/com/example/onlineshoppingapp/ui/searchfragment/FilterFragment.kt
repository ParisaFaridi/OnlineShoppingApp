package com.example.onlineshoppingapp.ui.searchfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.adapters.FilterTabLayoutAdapter
import com.example.onlineshoppingapp.databinding.FragmentFilterBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : Fragment() {

    lateinit var binding : FragmentFilterBinding
    lateinit var adapter :FilterTabLayoutAdapter
    private val filterVm : FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = activity?.let { FilterTabLayoutAdapter(it) }!!
        binding.viewPager.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.btnFilterResults.setOnClickListener {
            filterVm.getFilteredList()
            val action = FilterFragmentDirections.actionFilterFragmentToSearchResultsFragment(filterVm.searchQuery)
            findNavController().navigate(action)
        }
    }
}