package com.example.onlineshoppingapp.ui.detailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.onlineshoppingapp.adapters.ImageViewPagerAdapter
import com.example.onlineshoppingapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator3

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
        }
    }
    private fun setUpViewPager() {

        binding.viewPager.adapter = imageViewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(binding.viewPager)
        val currentPageIndex = 0
        binding.viewPager.currentItem = currentPageIndex

        // registering for page change callback
//        binding.viewPager.registerOnPageChangeCallback(
//            object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    //update the image number textview
//                }
//            }
//        )
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )
    }
}