package com.example.onlineshoppingapp.ui.order

import android.annotation.SuppressLint
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
import com.example.onlineshoppingapp.adapters.AddressAdapter
import com.example.onlineshoppingapp.data.model.Address
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
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTotalPrice().observe(viewLifecycleOwner){
            if (it != null)
                binding.tvTotalPrice.text = it
        }
        val userInfoShared = activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        val firstName = userInfoShared?.getString("first_name","")
        val lastName = userInfoShared?.getString("last_name","")

        viewModel.getAllAddresses().observe(viewLifecycleOwner) {
            if (it != null) {
                addressAdapter = AddressAdapter(it as ArrayList<Address>)
                binding.rvAddresses.adapter = addressAdapter
                addressAdapter.notifyDataSetChanged()
            }
            binding.btnCompleteOrder.setOnClickListener {
                val address = addressAdapter.selected
                if (address != null) {
                    viewModel.completeOrder(address,firstName!!,lastName!!)
                }
            }
            binding.btnNewAddress.setOnClickListener {
                findNavController().navigate(R.id.action_completeOrderFragment_to_mapsFragment)
            }
            viewModel.order.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding.layout.visibility = View.GONE
                        binding.lottie.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            binding.layout.visibility = View.VISIBLE
                            binding.lottie.visibility = View.GONE
                            findNavController().navigate(R.id.action_completeOrderFragment_to_cartFragment)
                            showDialog()
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            response.code?.let { code -> showErrorSnack(message, code) }
                        }
                    }
                }
            }
            viewModel.coupon.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding.layout.visibility = View.GONE
                        binding.lottie.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.layout.visibility = View.VISIBLE
                        binding.lottie.visibility = View.GONE
                        response.data?.let {
                            Toast.makeText(
                                requireContext(),
                                "کد با موفقیت اعمال شد",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            binding.tvTotalPrice.text = viewModel.total
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            response.code?.let { code -> showErrorSnack(message, code) }
                        }
                    }
                }
            }
            binding.btnAddCoupon.setOnClickListener {
                if (binding.etCoupon.text.toString().isNotEmpty()) {
                    viewModel.checkCoupon(binding.etCoupon.text.toString(),binding.tvTotalPrice.text.toString())
                }
            }
        }
    }
    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.order_completed))
            .setNeutralButton(getString(R.string.ok_)) { _, _ -> }
            .show()
    }
    private fun showErrorSnack(message: String, code: Int) {
        binding.layout.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
        val snackBar = Snackbar.make(
            binding.layout,
            getErrorMessage(message, code),
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("باشه") {

        }
        snackBar.show()
    }
}