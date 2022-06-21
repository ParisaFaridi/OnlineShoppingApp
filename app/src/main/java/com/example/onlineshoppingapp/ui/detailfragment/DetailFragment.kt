package com.example.onlineshoppingapp.ui.detailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ImageViewPagerAdapter
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.databinding.FragmentDetailBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.online_store)
        if (detailViewModel.product.value == null)
            detailViewModel.getProduct(args.productId)

        detailViewModel.product.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> { response.data?.let {
                        hideProgressBar()
                        setProductData(it)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
        binding.btnMinus.setOnClickListener {
            if (binding.tvProductNumber.text == getString(R.string._0))
                return@setOnClickListener
            else
                binding.tvProductNumber.text = decrementQuantityTv(binding.tvProductNumber)
        }
        binding.btnPlus.setOnClickListener {
            if (binding.tvProductNumber.text == getString(R.string._10))
                return@setOnClickListener
            else
                binding.tvProductNumber.text = incrementQuantityTv(binding.tvProductNumber)
        }
        binding.btnAddToCart.setOnClickListener {
            detailViewModel.getOrder(binding.tvProductNumber.text.toString().toInt())
        }
    }
    private fun incrementQuantityTv(textView: TextView) = (textView.text.toString().toInt() + 1).toString()
    private fun decrementQuantityTv(textView: TextView) = (textView.text.toString().toInt() - 1).toString()

    private fun setProductData(data: Product) {
        imageViewPagerAdapter = data.images?.let { images -> ImageViewPagerAdapter(images) }!!
        setUpViewPager()
        binding.apply {
            product = data
            tvDescription.text = data.description?.let { fixDescription(it) }
            ratingbar.rating = data.averageRating?.toFloat()!!
            tvPrice.text = NumberFormat.getNumberInstance(Locale.US).format(data.price?.toLong())
        }

    }
    private fun fixDescription(description: String): String {
        return description.replace("</p>", "")
            .replace("<p>", "").replace("<br />", "\n")
    }

    private fun hideProgressBar() {
        binding.layout.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.lottie.setAnimation(R.raw.loading)
        binding.lottie.visibility = View.VISIBLE
        binding.lottie.playAnimation()

    }

    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            detailViewModel.getProduct(args.productId)
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }

    private fun setUpViewPager() {
        binding.viewPager.apply {
            adapter = imageViewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
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