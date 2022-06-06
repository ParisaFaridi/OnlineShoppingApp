package com.example.onlineshoppingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshoppingapp.data.Product
import com.example.onlineshoppingapp.databinding.ProductItemBinding

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
//        Glide.with(holder.binding.imageView.context).load(POSTER_PATH + getItem(position).poster_path)
//            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//            .into(holder.binding.imageView)
        holder.binding.image.setOnClickListener {
            clickHandler.invoke(getItem(position))
        }
    }
}