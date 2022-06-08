package com.example.onlineshoppingapp.ui.detailfragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.onlineshoppingapp.adapters.ImageViewPagerAdapter
import com.example.onlineshoppingapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val detailViewModel : DetailViewModel by viewModels()
    private lateinit var binding : FragmentDetailBinding
    private val args :DetailFragmentArgs by navArgs()
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel.getProduct(args.productId).observe(viewLifecycleOwner){
            binding.product = it
            imageViewPagerAdapter = it.images?.let { it1 -> ImageViewPagerAdapter(it1) }!!
            setUpViewPager()
            binding.tvDescription.text = it.description?.replace("</p>","")
            ?.replace("<p>","")?.replace("<br />","\n")
            binding.ratingbar.rating = it?.averageRating?.toFloat()!!
            binding.tvPrice.text = NumberFormat.getNumberInstance(Locale.US).format(it.price?.toLong());
        }

    }
    private fun setUpViewPager() {

        binding.viewPager.adapter = imageViewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(binding.viewPager)
        val currentPageIndex = 0
        binding.viewPager.currentItem = currentPageIndex

    }
    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )
    }
}