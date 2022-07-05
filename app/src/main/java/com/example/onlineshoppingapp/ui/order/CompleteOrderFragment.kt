package com.example.onlineshoppingapp.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Shipping
import com.example.onlineshoppingapp.databinding.FragmentCompleteOrderBinding
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.example.onlineshoppingapp.ui.order.viewmodels.CompleteOrderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        viewModel.getOrder()
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
                    binding.tvTotalPrice.text = it.total
                    showDialog()
                    //findNavController().navigate(R.id.action_completeOrderFragment_to_cartFragment)

                }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
        viewModel.coupon.observe(viewLifecycleOwner) { response ->
            when (response) {
//                is Resource.Loading -> {
//                    binding.layout.visibility = View.GONE
//                    binding.lottie.visibility = View.VISIBLE
//                }
                is Resource.Success -> { response.data?.let {
                    Toast.makeText(requireContext(), " + ${viewModel.order.value?.data?.total}کد با موفقیتی اعمال شد", Toast.LENGTH_SHORT)
                        .show()
                    binding.tvTotalPrice.text = viewModel.order.value?.data?.total

                }
                }
                is Resource.Error -> {
                    response.message?.let {  message ->
                        response.code?.let { showErrorSnack(message, it) }
                    }
                }
            }
        }
        binding.btnAddCoupon.setOnClickListener {
            if (binding.etCoupon.text.toString().isNotEmpty()){
                viewModel.checkCoupon(binding.etCoupon.text.toString())
            }
        }
    }
    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.order_completed))
            .setNeutralButton(getString(R.string.ok_)){ _, _ -> }
            .show()
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