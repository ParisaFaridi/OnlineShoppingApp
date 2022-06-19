package com.example.onlineshoppingapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.onlineshoppingapp.ColorFilterFragment
import com.example.onlineshoppingapp.SizeFilterFragment

class FilterTabLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SizeFilterFragment()
            else -> ColorFilterFragment()
        }
    }

}