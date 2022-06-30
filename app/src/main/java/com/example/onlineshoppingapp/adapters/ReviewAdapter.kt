package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.Review
import com.example.onlineshoppingapp.databinding.ProductItemBinding
import com.example.onlineshoppingapp.databinding.ReviewItemBinding
import java.text.NumberFormat
import java.util.*

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ItemHolder>(FormulaDiffCallBack) {

    object FormulaDiffCallBack: DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ItemHolder(val binding: ReviewItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: ReviewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.review_item,
            parent,
            false
        )
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.review = getItem(position)
        holder.binding.tvReview.text = fixDescription(getItem(position).review)
    }
    private fun fixDescription(description: String): String {
        return description.replace("</p>", "")
            .replace("<p>", "").replace("<br />", "\n")
    }
}