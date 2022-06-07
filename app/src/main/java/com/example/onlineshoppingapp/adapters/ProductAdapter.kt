package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.databinding.ProductItemBinding
import com.example.onlineshoppingapp.data.model.Product

typealias ClickHandler = (Product) -> Unit

class ProductAdapter(private val clickHandler: ClickHandler):
    ListAdapter<Product, ProductAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ItemHolder(val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: ProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_item,
            parent,
            false
        )
        return ItemHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.product = getItem(position)
        Glide.with(holder.binding.image.context).load(getItem(position).images?.get(0)?.src)
            .into(holder.binding.image)
        holder.binding.image.setOnClickListener {
            clickHandler.invoke(getItem(position))
        }
    }
}