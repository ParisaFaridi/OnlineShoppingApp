package com.example.onlineshoppingapp.ui.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.databinding.FragmentCreateAccountBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment() {

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
        val customerId = activity?.getSharedPreferences(getString(R.string.user_info),Context.MODE_PRIVATE)?.
        getInt(getString(R.string.customer_id),0)

        if ( customerId!= 0)
            findNavController().navigate(R.id.action_createAccountFragment_to_profileFragment)

        binding.btnSignUp.setOnClickListener {
            if (hasEmptyField()) {
                checkForErrors()
                return@setOnClickListener
            }
            signup()
            signUpViewModel.customerLive.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Loading ->{
                        showProgressBar()
                    }
                    is Resource.Success ->{
                        hideProgressBar()
                        Toast.makeText(requireContext(),getString(R.string.successful_signup),Toast.LENGTH_LONG).show()
                        saveInShare(it.data)
                        findNavController().navigate(R.id.action_createAccountFragment_to_profileFragment)
                    }
                    is Resource.Error -> {
                            it.message?.let {  message ->
                                it.code?.let { code -> showErrorSnack(message, code) }
                        }
                    }
                }
            }
        }
    }
    private fun showErrorSnack(message: String, code: Int) {
        val snackBar = Snackbar.make(binding.layout, getErrorMessage(message,code), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.ok_)) {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    private fun hideProgressBar() = binding.apply {
        layout.visibility = View.VISIBLE
        lottie.visibility = View.GONE
    }
    private fun showProgressBar() = binding.apply {
        layout.visibility = View.GONE
        lottie.visibility = View.VISIBLE
        lottie.playAnimation()
    }
    private fun saveInShare(customer: Customer?) {
        val userInfoShared = activity?.getSharedPreferences(getString(R.string.user_info),Context.MODE_PRIVATE)
        val editor = userInfoShared?.edit()
        customer?.id?.let { it -> editor?.putInt(getString(R.string.customer_id), it)?.apply() }
        customer?.email?.let { it -> editor?.putString(getString(R.string.email_share),it)?.apply() }
        editor?.putString("name","${customer?.firstName}  ${customer?.lastName}")
    }

    private fun signup() =
        signUpViewModel.signUp(Customer(email = binding.etEmail.text.toString(),
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            username = binding.etUserName.text.toString(),))

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
}