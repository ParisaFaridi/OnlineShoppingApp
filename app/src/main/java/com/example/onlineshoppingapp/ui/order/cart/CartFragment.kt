package com.example.onlineshoppingapp.ui.order.cart

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
import com.example.onlineshoppingapp.adapters.CartAdapter
import com.example.onlineshoppingapp.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

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

        val adapter = CartAdapter ({ quantity,id ->
            viewModel.updateProductQuantity(id,quantity)
        },{
            viewModel.deleteProduct(it)
        }
        )
        binding.rvCartProducts.adapter = adapter

        viewModel.getAllCartProducts().observe(viewLifecycleOwner){
            if (it.isEmpty()){
                showProgressBar()
                binding.lottie.setAnimation(R.raw.empty_cart)
                binding.lottie.playAnimation()
                binding.tv.visibility = View.VISIBLE
            }else{
                hideProgressBar()
                adapter.submitList(it)
            }
        }
        viewModel.getTotalPrice().observe(viewLifecycleOwner){
            if (it != null)
                binding.tvSumOfPrice.text = it.toString()
        }
        val customerId = activity?.getSharedPreferences(getString(R.string.user_info),Context.MODE_PRIVATE)?.getInt(getString(R.string.customer_id),0)
        binding.btnSubmitOrder.setOnClickListener {
            if (customerId == 0)
                Toast.makeText(requireContext(), getString(R.string.sign_up_first), Toast.LENGTH_LONG).show()
            else
                findNavController().navigate(R.id.action_cartFragment_to_completeOrderFragment)
        }
    }

    private fun hideProgressBar() = binding.apply{
        layout.visibility = View.VISIBLE
        lottie.visibility = View.GONE
        tv.visibility = View.GONE
    }
    private fun showProgressBar() = binding.apply{
        layout.visibility = View.GONE
        tv.visibility = View.GONE
        lottie.apply {
            visibility = View.VISIBLE
            setAnimation(R.raw.loading)
            playAnimation()
        }
    }
}