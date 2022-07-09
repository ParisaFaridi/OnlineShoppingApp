package com.example.onlineshoppingapp.ui.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding
    private val signUpViewModel : CustomerViewModel by activityViewModels()

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
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        binding.tvName.text = response.data?.let { setName(it) }
                        binding.tvEmail.text = response.data?.email
                    }
                    is Resource.Error -> {

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

    private fun hideProgressBar() = binding.apply {
        tvName.visibility = View.VISIBLE
        tvEmail.visibility = View.VISIBLE
        btnLogOut.visibility=View.VISIBLE
        lottie.visibility = View.GONE
    }
    private fun showProgressBar() = binding.apply {
        tvName.visibility = View.GONE
        tvEmail.visibility = View.GONE
        lottie.visibility = View.VISIBLE
        lottie.playAnimation()
    }
}