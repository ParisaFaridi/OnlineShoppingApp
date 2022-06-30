package com.example.onlineshoppingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlineshoppingapp.data.model.Shipping
import com.example.onlineshoppingapp.databinding.FragmentCompleteOrderBinding
import com.example.onlineshoppingapp.ui.CompleteOrderViewModel
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteOrderFragment : Fragment() {

    private lateinit var binding: FragmentCompleteOrderBinding
    private val viewModel: CompleteOrderViewModel by viewModels()
    private val args :CompleteOrderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCompleteOrder.setOnClickListener {
            viewModel.completeOrder(args.orderId,Shipping(address1 = binding.etAddress.text.toString(),
            city = binding.etCity.text.toString(),
            postcode = binding.etZipCode.text.toString()))
        }
        viewModel.order.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.layout.visibility = View.GONE
                    binding.lottie.visibility = View.VISIBLE
                }
                is Resource.Success -> { response.data?.let {
                    binding.layout.visibility = View.VISIBLE
                    binding.lottie.visibility = View.GONE
                    Toast.makeText(requireContext(), "سفارش با موفقیت ثبت شد!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_completeOrderFragment_to_cartFragment)

                }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            viewModel.completeOrder(args.orderId,Shipping(address1 = binding.etAddress.text.toString(),
                city = binding.etCity.text.toString(),
                postcode = binding.etZipCode.text.toString()))
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }

}