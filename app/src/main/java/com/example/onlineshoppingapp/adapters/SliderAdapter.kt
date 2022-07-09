package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.data.model.Image
import com.example.onlineshoppingapp.databinding.ImageViewBinding
import com.example.onlineshoppingapp.databinding.SliderImageViewBinding

class SliderAdapter(private val imageList: MutableList<Image>, val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderAdapter.ViewPagerViewHolder>() {

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    inner class ViewPagerViewHolder(val binding: SliderImageViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .error(R.drawable.ic_baseline_error_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        }
    }

    override fun getItemCount(): Int = imageList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val binding = SliderImageViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setData(imageList[position].src)
        if (position == imageList.size - 2)
            viewPager2.post(runnable)
    }

}