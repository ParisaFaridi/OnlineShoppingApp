package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.Category
import com.example.onlineshoppingapp.databinding.CategoryItemBinding

typealias CategoryClickHandler = (Category) -> Unit

class CategoryAdapter(private val clickHandler: CategoryClickHandler):
    ListAdapter<Category, CategoryAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class ItemHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: CategoryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.category_item,
            parent,
            false
        )
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.category = getItem(position)
        Glide.with(holder.binding.imageView2.context).load(getItem(position).image?.src)
            .into(holder.binding.imageView2)
        holder.binding.imageView2.setOnClickListener {
            clickHandler.invoke(getItem(position))
        }
    }
}