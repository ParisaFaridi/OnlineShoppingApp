package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.AttributeTerm
import com.example.onlineshoppingapp.databinding.FilterItemViewBinding



class AttributeItemsAdapter() : ListAdapter<AttributeTerm, AttributeItemsAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack: DiffUtil.ItemCallback<AttributeTerm>() {
        override fun areItemsTheSame(oldItem: AttributeTerm, newItem: AttributeTerm): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: AttributeTerm, newItem: AttributeTerm): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class ItemHolder(val binding: FilterItemViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: FilterItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.filter_item_view,
            parent,
            false
        )
        return ItemHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.term = getItem(position)
        holder.binding.radioBtn.setOnClickListener {
            getItem(position).isSelected = !getItem(position).isSelected
            holder.binding.radioBtn.isChecked = getItem(position).isSelected
        }
    }
}