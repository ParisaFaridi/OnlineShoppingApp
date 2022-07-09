package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.onlineshoppingapp.databinding.HeaderItemBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    class HeaderViewHolder(binding :ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding  =
            HeaderItemBinding.inflate((LayoutInflater.from(parent.context)), parent, false)
        return HeaderViewHolder(binding)

    }
    override fun getItemCount() = 1
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
    }
}