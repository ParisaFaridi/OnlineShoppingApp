package com.example.onlineshoppingapp.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.databinding.OnSaleItemBinding
import java.text.NumberFormat
import java.util.*


class OnSaleProductAdapter (private val clickHandler: ClickHandler):
    ListAdapter<Product, OnSaleProductAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ItemHolder(val binding: OnSaleItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: OnSaleItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.on_sale_item,
            parent,
            false
        )
        return ItemHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.apply {
            regularPrice.text = getFormattedPrice("regular",position)
            onSalePrice.text =  getFormattedPrice("sale",position)
            regularPrice.paintFlags = holder.binding.regularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            product = getItem(position)
        }
        if (getItem(position).images?.firstOrNull()?.src == null){
            Glide.with(holder.binding.image.context).load(R.drawable.ic_baseline_error_24).centerCrop()
                .into(holder.binding.image)
        }else{
            Glide.with(holder.binding.image.context).load(getItem(position).images?.get(0)?.src).centerCrop()
                .into(holder.binding.image)
        }
        holder.binding.image.setOnClickListener {
            clickHandler.invoke(getItem(position))
        }
    }
    private fun getFormattedPrice(type :String?,position: Int): String {
        return if (type == "regular")
            NumberFormat.getNumberInstance(Locale.US).format(getItem(position).regularPrice?.toLong())
        else
            NumberFormat.getNumberInstance(Locale.US).format(getItem(position).salePrice?.toLong()) + " تومان!"
    }
}