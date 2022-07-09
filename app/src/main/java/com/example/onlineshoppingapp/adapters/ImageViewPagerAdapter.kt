package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshoppingapp.R
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.onlineshoppingapp.data.model.Image
import com.example.onlineshoppingapp.databinding.ImageViewBinding

class ImageViewPagerAdapter(private val imageList: List<Image>) :
    RecyclerView.Adapter<ImageViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(val binding: ImageViewBinding) :
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

        val binding = ImageViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setData(imageList[position].src)
    }

}
