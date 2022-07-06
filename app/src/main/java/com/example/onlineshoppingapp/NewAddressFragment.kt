package com.example.onlineshoppingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        }

    private fun addNewAddress() =
        viewModel.addAddress(Address(
            title = binding.etTitle.text.toString(),
            address1 = binding.etAddress1.text.toString(),
            address2 = binding.etAddress2.text.toString(),
            country = binding.etCountry.text.toString(),
            city = binding.etCity.text.toString(),
            phone = binding.etPhone.text.toString(),
            postcode = binding.etPostCode.text.toString())
        )

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