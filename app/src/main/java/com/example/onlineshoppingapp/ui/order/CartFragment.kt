package com.example.onlineshoppingapp.ui.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.adapters.CartAdapter
import com.example.onlineshoppingapp.databinding.FragmentCartBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrder()
        val adapter = CartAdapter ({ lineItem,quantity ->
            viewModel.updateQuantity(lineItem.id,quantity)
        },{
            viewModel.updateQuantity(it,0)}
        )
        binding.rvCartProducts.adapter = adapter
        viewModel.order.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> { response.data?.let {
                    hideProgressBar()
                    adapter.submitList(it.lineItems)
                    binding.tvSumOfPrice.text = it.total
                }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
        val customerId = activity?.getSharedPreferences("user_info",Context.MODE_PRIVATE)?.getInt("customer_id",0)
        binding.btnSubmitOrder.setOnClickListener {
            if (customerId == 0)
                Toast.makeText(requireContext(), "ابتدا ثبت نام کنید!", Toast.LENGTH_LONG).show()
            else
                findNavController().navigate(R.id.action_cartFragment_to_completeOrderFragment)

        }
    }
    private fun hideProgressBar() {
        binding.layout.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.lottie.visibility = View.VISIBLE
        binding.lottie.playAnimation()
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            viewModel.getOrder()
            showProgressBar()
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }
}