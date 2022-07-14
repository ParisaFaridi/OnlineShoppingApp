package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.CartProduct
import com.example.onlineshoppingapp.data.model.LineItem
import com.example.onlineshoppingapp.databinding.CartItemBinding
import java.text.NumberFormat
import java.util.*

typealias QuantityClickHandler = (Int,Int) -> Unit
typealias DeleteClickHandler = (Int) -> Unit

class CartAdapter(private val clickHandler: QuantityClickHandler,private val deleteClickHandler:DeleteClickHandler):
    ListAdapter<CartProduct, CartAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack: DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.price == newItem.price
        }
    }

    class ItemHolder(val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: CartItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cart_item,
            parent,false)
        return ItemHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.apply {
            cartProduct = getItem(position)
            tvPrice.text = getFormattedPrice(position)
            tvTotalOfProduct.text = getFormattedTotalPrice(position)
            Glide.with(holder.binding.image.context).load(getItem(position).image).centerCrop()
                .into(holder.binding.image)
        }
        holder.binding.btnMinus.setOnClickListener {
            if (holder.binding.tvProductNumber.text == "1")
                return@setOnClickListener
            else {
                holder.binding.tvProductNumber.text =
                    decrementQuantityTv(holder.binding.tvProductNumber)
                clickHandler.invoke(getItem(position).id,holder.binding.tvProductNumber.text.toString().toInt())
            }
        }
        holder.binding.btnPlus.setOnClickListener {
            if (holder.binding.tvProductNumber.text == "10")
                return@setOnClickListener
            else {
                holder.binding.tvProductNumber.text =
                    incrementQuantityTv(holder.binding.tvProductNumber)
                clickHandler.invoke(getItem(position).id,holder.binding.tvProductNumber.text.toString().toInt())
            }
        }
        holder.binding.btnDelete.setOnClickListener {
            deleteClickHandler.invoke(getItem(position).id)
        }
    }
    private fun incrementQuantityTv(textView: TextView) = (textView.text.toString().toInt() + 1).toString()
    private fun decrementQuantityTv(textView: TextView) = (textView.text.toString().toInt() - 1).toString()
    private fun getFormattedPrice(position: Int) = NumberFormat.getNumberInstance(Locale.US).format(getItem(position).price.toLong()) + " تومان"
    private fun getFormattedTotalPrice(position: Int) = NumberFormat.getNumberInstance(Locale.US).format(getItem(position).totalPrice.toLong())
}
