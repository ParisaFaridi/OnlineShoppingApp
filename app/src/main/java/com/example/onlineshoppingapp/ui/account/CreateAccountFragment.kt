package com.example.onlineshoppingapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.databinding.FragmentCreateAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    lateinit var binding :FragmentCreateAccountBinding
    val signUpViewModel : CustomerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //if (shared) go to profile
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
                        //val shared = SharedPreferences()
                        //findNavController().navigate()
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