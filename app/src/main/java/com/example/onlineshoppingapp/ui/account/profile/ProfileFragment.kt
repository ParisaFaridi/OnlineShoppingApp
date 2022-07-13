package com.example.onlineshoppingapp.ui.account.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.databinding.FragmentProfileBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    lateinit var binding : FragmentProfileBinding
    private val signUpViewModel : ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customerId = activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)?.
        getInt(getString(R.string.customer_id),0)

        if (signUpViewModel.customerLive.value == null)
            customerId?.let { signUpViewModel.getCustomer(it) }

        signUpViewModel.customerLive.observe(viewLifecycleOwner){ response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading ->{
                        showProgressBar(binding.group!!,binding.lottie)
                    }
                    is Resource.Success -> {
                        hideProgressBar(binding.group!!,binding.lottie)
                        binding.tvName.text = response.data?.let { setName(it) }
                        binding.tvEmail.text = response.data?.email
                    }
                    is Resource.Error -> {
                        response.message?.let {  message ->
                            response.code?.let { code -> showErrorSnack(message, code) } }
                    }
                }
            }
        }
        binding.btnLogOut.setOnClickListener {
            val shared = activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
            shared?.edit()?.clear()?.apply()
            findNavController().navigate(R.id.action_profileFragment_to_createAccountFragment)
        }
    }
    private fun setName(data: Customer) = "${data.firstName} ${data.lastName}"

    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.tvName, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.ok_)) {
            snackBar.dismiss()
        }
        snackBar.show()
    }
}