package com.example.onlineshoppingapp.ui.homeFragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.HeaderAdapter
import com.example.onlineshoppingapp.adapters.OnSaleProductAdapter
import com.example.onlineshoppingapp.adapters.ProductAdapter
import com.example.onlineshoppingapp.adapters.SliderAdapter
import com.example.onlineshoppingapp.data.model.Image
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.databinding.FragmentHomeBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModelHome: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bestProductsAdapter: ProductAdapter
    private lateinit var newProductsAdapter: ProductAdapter
    private lateinit var mostViewedProductsAdapter: ProductAdapter
    private lateinit var imageViewPagerAdapter: SliderAdapter
    private lateinit var onSaleProductAdapter: OnSaleProductAdapter
     var handler = Handler()
     var runnable = Runnable{}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViews()
        if (viewModelHome.bestProducts.value == null) {
            getLists()
        }
        binding.apply {
            searchView.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
            btnBestProducts.setOnClickListener { goToProductListFragment(getString(R.string.rating))  }
            btnNewProducts.setOnClickListener { goToProductListFragment(getString(R.string.date)) }
            btnMostViewed.setOnClickListener { goToProductListFragment(getString(R.string.popularity)) }
            btnOnSaleProducts.setOnClickListener { goToProductListFragment(getString(R.string.on_sale)) }
            btnSetting.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_settingFragment) }
        }
        viewModelHome.sliderPics.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> { response.data?.let {
                        imageViewPagerAdapter = it.images?.let { images -> SliderAdapter(images as MutableList<Image>,binding.viewPager) }!!
                        setUpViewPager()
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }}
                }
            }
        }
        viewModelHome.onSaleProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        binding.layout.visibility = View.VISIBLE
                        binding.lottie.visibility = View.GONE
                        onSaleProductAdapter.submitList(data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }
                    }
                }
            }
        }
        viewModelHome.bestProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        showData(bestProductsAdapter, data)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }}
                }
            }
        }
        viewModelHome.newProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data -> showData(newProductsAdapter, data) }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }}
                }
            }
        }
        viewModelHome.mostViewedProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    response.data?.let { data -> showData(mostViewedProductsAdapter, data) }
                }
                is Resource.Error -> {
                    response.message?.let { message -> response.code?.let { code -> showErrorSnack(message, code) }
                    }
                }
            }
        }
    }

    private fun goToProductListFragment(orderBy: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductListFragment(orderBy)
        findNavController().navigate(action)
    }

    private fun setUpViewPager() {
        binding.viewPager.apply {
            adapter = imageViewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setCurrentItem(1,false)
        }
        val currentPageIndex = 0
        binding.viewPager.apply {
            currentItem = currentPageIndex
            offscreenPageLimit = 3
            clipChildren= false
            clipToPadding = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.viewPager.setPageTransformer(transformer)
        handler = Handler()
        runnable = Runnable {
            binding.viewPager.currentItem = binding.viewPager.currentItem+1
        }
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable,3000)
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,3000)
    }

    private fun showProgressBar() = binding.lottie.apply {
        setAnimation(R.raw.loading)
        visibility = View.VISIBLE
        playAnimation()
        binding.layout.visibility = View.GONE
    }

    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            getLists()
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }

    private fun getLists() = viewModelHome.apply {
        getNewProducts()
        getMostViewedProducts()
        getBestProducts()
        getOnSaleProducts()
    }

    private fun showData(adapter: ProductAdapter, data: List<Product>) {
        binding.layout.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
        adapter.submitList(data)
    }

    private fun setRecyclerViews() {
        bestProductsAdapter =
            ProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }
        newProductsAdapter =
            ProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }
        mostViewedProductsAdapter =
            ProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }

        onSaleProductAdapter = OnSaleProductAdapter { product -> product.id?.let { it -> goToDetailFragment(it) } }

        binding.apply {
            rvBestProducts.adapter = bestProductsAdapter
            rvNewProducts.adapter = newProductsAdapter
            rvMostViewedProducts.adapter = mostViewedProductsAdapter
            rvOnSaleProducts.adapter = ConcatAdapter(HeaderAdapter(),onSaleProductAdapter)
        }
    }

    private fun goToDetailFragment(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }
}