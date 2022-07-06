package com.example.onlineshoppingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.Review
import com.example.onlineshoppingapp.databinding.ReviewItemBinding

typealias DeleteListener = (Review) -> Unit
typealias EditListener = (Review) -> Unit

class ReviewAdapter(val context:Context, private val deleteListener: DeleteListener, private val editListener: EditListener)
    : ListAdapter<Review, ReviewAdapter.ItemHolder>(FormulaDiffCallBack) {

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
        val userInfoShared = context.getSharedPreferences(context.getString(R.string.user_info),Context.MODE_PRIVATE)
        val email = userInfoShared.getString(context.getString(R.string.email_share),"")
        if (getItem(position).reviewerEmail == email){
            holder.binding.btnDelete.visibility = View.VISIBLE
            holder.binding.btnEdit.visibility = View.VISIBLE
        }
        holder.binding.btnDelete.setOnClickListener {
            deleteListener.invoke(getItem(position))
        }
        holder.binding.btnEdit.setOnClickListener {
            editListener.invoke(getItem(position))
        }
    }
    private fun fixDescription(description: String): String {
        return description.replace("</p>", "")
            .replace("<p>", "").replace("<br />", "\n")
    }
}