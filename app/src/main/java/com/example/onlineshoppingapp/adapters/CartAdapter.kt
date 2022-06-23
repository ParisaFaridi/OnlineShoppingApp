package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.LineItem
import com.example.onlineshoppingapp.databinding.CartItemBinding
import java.text.NumberFormat
import java.util.*

typealias QuantityClickHandler = (LineItem,Int) -> Unit
typealias DeleteClickHandler = (Int) -> Unit

class CartAdapter(private val clickHandler: QuantityClickHandler,private val deleteClickHandler:DeleteClickHandler):
    ListAdapter<LineItem, CartAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack: DiffUtil.ItemCallback<LineItem>() {
        override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
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
            lineItem = getItem(position)
            tvPrice.text = getFormattedPrice(position)
        }
        holder.binding.btnMinus.setOnClickListener {
            if (holder.binding.tvProductNumber.text == "1")
                return@setOnClickListener
            else {
                holder.binding.tvProductNumber.text =
                    decrementQuantityTv(holder.binding.tvProductNumber)
                clickHandler.invoke(getItem(position),holder.binding.tvProductNumber.text.toString().toInt())
            }
        }
        holder.binding.btnPlus.setOnClickListener {
            if (holder.binding.tvProductNumber.text == "10")
                return@setOnClickListener
            else {
                holder.binding.tvProductNumber.text =
                    incrementQuantityTv(holder.binding.tvProductNumber)
                clickHandler.invoke(getItem(position),holder.binding.tvProductNumber.text.toString().toInt())
            }
        }
        holder.binding.btnDelete.setOnClickListener {
            deleteClickHandler.invoke(getItem(position).id)
        }
    }
    private fun incrementQuantityTv(textView: TextView) = (textView.text.toString().toInt() + 1).toString()
    private fun decrementQuantityTv(textView: TextView) = (textView.text.toString().toInt() - 1).toString()
    private fun getFormattedPrice(position: Int) = NumberFormat.getNumberInstance(Locale.US).format(getItem(position).total?.toLong()) + " تومان"
}
