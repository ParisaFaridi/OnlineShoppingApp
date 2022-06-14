package com.example.onlineshoppingapp.ui.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.databinding.FragmentCreateAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    lateinit var binding :FragmentCreateAccountBinding
    private val signUpViewModel : CustomerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customerId = activity?.getSharedPreferences("user_info",Context.MODE_PRIVATE)?.getInt("customer_id",0)
        if ( customerId!= 0)
            findNavController().navigate(R.id.action_createAccountFragment_to_profileFragment)

        binding.btnSignUp.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            signUpViewModel.signUp(Customer(email = binding.etEmail.text.toString(),
                firstName = binding.etFirstName.text.toString(),
                lastName = binding.etLastName.text.toString(),
                username = binding.etUserName.text.toString(),
            ))
            signUpViewModel.customerLive.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Success ->{
                        Toast.makeText(requireContext(), "${it.data?.id} added!", Toast.LENGTH_LONG).show()
                        val userInfoShared = activity?.getSharedPreferences("user_info",Context.MODE_PRIVATE)
                        val editor = userInfoShared?.edit()
                        it.data?.id?.let { it1 -> editor?.putInt("customer_id", it1)?.apply() }

                        findNavController().navigate(R.id.action_createAccountFragment_to_profileFragment)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "${it.code}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    private fun hasEmptyField(): Boolean {
        return (binding.etFirstName.text.isNullOrEmpty() || binding.etLastName.text.isNullOrEmpty() ||
                binding.etUserName.text.isNullOrEmpty() || binding.etEmail.text.isNullOrEmpty())
    }

    private fun checkForErrors() {
        setError(binding.etFirstName)
        setError(binding.etLastName)
        setError(binding.etUserName)
        setError(binding.etEmail)
    }

    private fun setError(editText: EditText) {
        if (editText.text.isNullOrEmpty())
            editText.error = getString(R.string.must_be_filled)
    }
}