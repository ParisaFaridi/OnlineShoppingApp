package com.example.onlineshoppingapp.ui.account

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.onlineshoppingapp.Resource
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

        val customerId = activity?.getSharedPreferences("user_info", Context.MODE_PRIVATE)?.getInt("customer_id",0)

        if (signUpViewModel.customerLive.value == null)
            customerId?.let { signUpViewModel.getCustomer(it) }

        signUpViewModel.customerLive.observe(viewLifecycleOwner){ response ->
            if (response != null) {
                when (response) {
                    is Resource.Success -> {
                        binding.tvName.text = response.data?.firstName + response.data?.lastName
                        binding.tvEmail.text = response.data?.email
                    }
                }
            }
        }
    }
}