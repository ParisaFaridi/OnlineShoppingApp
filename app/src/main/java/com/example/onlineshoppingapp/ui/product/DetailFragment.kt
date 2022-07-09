package com.example.onlineshoppingapp.ui.product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.ImageViewPagerAdapter
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.adapters.ReviewAdapter
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.data.model.Review
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
    private lateinit var reviewsAdapter: ReviewAdapter
    private lateinit var relatedAdapter: ProductAdapter

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
        val userInfoShared =
            activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        binding.btnAddReview.setOnClickListener {
            if (userInfoShared == null) {
                Toast.makeText(requireContext(), "ابتدا ثبت نام کنید!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val action = detailViewModel.product.value?.data?.id?.let { it1 ->
                DetailFragmentDirections.actionDetailFragmentToAddReviewFragment(it1,0,"",0)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
        detailViewModel.product.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let {
                        hideProgressBar()
                        setProductData(it)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
        detailViewModel.reviews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let {
                        hideProgressBar()
                        setReviews(it)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
        detailViewModel.relatedProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { relatedProducts->
                        hideProgressBar()
                        relatedAdapter = ProductAdapter {
                            it.id?.let { it1 -> goToDetailFragment(it1) }
                        }
                        binding.rvRelatedProducts.adapter = relatedAdapter
                        relatedAdapter.submitList(relatedProducts)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
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
            detailViewModel.addToCart(binding.tvProductNumber.text.toString().toInt())
            Toast.makeText(
                requireContext(),
                getString(R.string.added_to_cart),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun goToDetailFragment(id: Int) {
        val action = DetailFragmentDirections.actionDetailFragmentSelf(id)
        findNavController().navigate(action)
    }
    private fun incrementQuantityTv(textView: TextView) =
        (textView.text.toString().toInt() + 1).toString()

    private fun decrementQuantityTv(textView: TextView) =
        (textView.text.toString().toInt() - 1).toString()

    private fun setReviews(list: List<Review>) {
        reviewsAdapter = ReviewAdapter(requireContext(),
            deleteListener = { review->
                detailViewModel.deleteReview(review.id).observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                        is Resource.Success -> {
                            response.data?.let {
                                hideProgressBar()
                                Toast.makeText(
                                    requireContext(), "نظر شما حذف شد!", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is Resource.Error -> {
                            response.message?.let { message ->
                                response.code?.let {
                                    showErrorSnack(message, it)
                                }
                            }
                        }
                    }
                }
            }, editListener = {
                val action = detailViewModel.product.value?.data?.id?.let { it1 ->
                    DetailFragmentDirections.actionDetailFragmentToAddReviewFragment(
                        it1,it.rating,it.review,it.id)
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }
        )
        binding.rvReviews.adapter = reviewsAdapter
        reviewsAdapter.submitList(list)
    }

    private fun setProductData(data: Product) {
        imageViewPagerAdapter = data.images?.let { images -> ImageViewPagerAdapter(images) }!!
        setUpViewPager()
        binding.apply {
            product = data
            tvDescription.text = data.description?.let { fixDescription(it) }
            ratingbar.rating = data.averageRating?.toFloat()!!
            tvPrice.text = NumberFormat.getNumberInstance(Locale.US).format(data.price?.toLong())
            data.relatedIds?.let { detailViewModel.getRelatedProducts(it) }
        }
    }

    private fun fixDescription(description: String): String {
        return description.replace("</p>", "")
            .replace("<p>", "").replace("<br />", "\n")
    }

    private fun hideProgressBar() {
        binding.layout.visibility = View.VISIBLE
        binding.divider.visibility = View.VISIBLE
        binding.bottom.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.lottie.setAnimation(R.raw.loading)
        binding.lottie.visibility = View.VISIBLE
        binding.layout.visibility = View.GONE
        binding.divider.visibility = View.GONE
        binding.bottom.visibility = View.GONE
        binding.lottie.playAnimation()

    }

    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(
            binding.layout,
            getErrorMessage(message, code),
            Snackbar.LENGTH_INDEFINITE
        )
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