package com.example.onlineshoppingapp.ui.order.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.adapters.CartAdapter
import com.example.onlineshoppingapp.databinding.FragmentCartBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    lateinit var adapter :CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllCartProducts()
        setAdapter()

        viewModel.getAllCartProducts().observe(viewLifecycleOwner){
            if (it.isEmpty()){
                showProgressBar(binding.layout,binding.lottie)
                binding.tv.visibility = View.VISIBLE
                binding.lottie.apply {
                    setAnimation(R.raw.empty_cart)
                    playAnimation()
                }
            }else{
                hideProgressBar(binding.layout,binding.lottie)
                binding.tv.visibility = View.GONE
                adapter.submitList(it)
            }
        }
        viewModel.getTotalPrice().observe(viewLifecycleOwner){
            if (it != null)
                binding.tvSumOfPrice.text =
                    NumberFormat.getNumberInstance(Locale.US).format(it.toLong())
        }
        val customerId = activity?.getSharedPreferences(getString(R.string.user_info),Context.MODE_PRIVATE)?.getInt(getString(R.string.customer_id),0)
        binding.btnSubmitOrder.setOnClickListener {
            if (customerId == 0)
                Toast.makeText(requireContext(), getString(R.string.sign_up_first), Toast.LENGTH_LONG).show()
            else
                findNavController().navigate(R.id.action_cartFragment_to_completeOrderFragment)
        }
    }
    private fun setAdapter() {
        adapter = CartAdapter ({ quantity,id ->
            viewModel.updateProductQuantity(id,quantity)},{ viewModel.deleteProduct(it) }
        )
        binding.rvCartProducts.adapter = adapter
    }
}