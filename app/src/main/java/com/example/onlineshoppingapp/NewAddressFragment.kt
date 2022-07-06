package com.example.onlineshoppingapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.data.model.Address
import com.example.onlineshoppingapp.databinding.FragmentNewAddressBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.order.viewmodels.CompleteOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewAddressFragment : BaseFragment() {

    lateinit var binding :FragmentNewAddressBinding
    private val viewModel: CompleteOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveAddress.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            addNewAddress()
            findNavController().navigate(R.id.action_newAddressFragment_to_completeOrderFragment)
            }
        binding.btnLocation.setOnClickListener {
            findNavController().navigate(R.id.action_newAddressFragment_to_mapsFragment)
        }
        }

    private fun addNewAddress() {
        val shared = activity?.getSharedPreferences("lat_long",Context.MODE_PRIVATE)
        val lat = shared?.getString("latitude","0.0")?.toDouble()
        val long = shared?.getString("longitude","0.0")?.toDouble()
        findNavController().navigate(R.id.action_mapsFragment_to_newAddressFragment)
        if (lat != null && long != null){
            viewModel.addAddress(
                Address(
                    title = binding.etTitle.text.toString(),
                    address1 = binding.etAddress1.text.toString(),
                    country = binding.etCountry.text.toString(),
                    city = binding.etCity.text.toString(),
                    phone = binding.etPhone.text.toString(),
                    postcode = binding.etPostCode.text.toString(),
                    lat = lat,
                    long = long
                )
            )
        }else{
            Toast.makeText(requireContext(), "لطفا لوکیشن را تایید کنید", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasEmptyField(): Boolean {
        return (binding.etTitle.text.isNullOrEmpty() || binding.etAddress1.text.isNullOrEmpty() ||
                binding.etCountry.text.isNullOrEmpty() || binding.etCity.text.isNullOrEmpty()
                || binding.etPhone.text.isNullOrEmpty()|| binding.etPostCode.text.isNullOrEmpty())
    }
    private fun checkForErrors() {
        setError(binding.etTitle)
        setError(binding.etAddress1)
        setError(binding.etCountry)
        setError(binding.etCity)
        setError(binding.etPhone)
        setError(binding.etPostCode)
    }
}