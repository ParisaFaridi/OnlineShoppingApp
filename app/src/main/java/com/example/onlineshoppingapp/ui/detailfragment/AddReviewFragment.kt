package com.example.onlineshoppingapp.ui.detailfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Review
import com.example.onlineshoppingapp.databinding.FragmentAddReviewBinding
import com.example.onlineshoppingapp.ui.BaseFragment
import com.example.onlineshoppingapp.ui.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReviewFragment : BaseFragment() {

    private lateinit var binding: FragmentAddReviewBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val args:AddReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfoShared = activity?.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        val email =userInfoShared?.getString("email","")
        val reviewerName = userInfoShared?.getString("name","")
        binding.btnSubmit.setOnClickListener {
            if (binding.etReview.text.isNullOrEmpty()){
                setError(binding.etReview)
                return@setOnClickListener
            }
            detailViewModel.createReview(Review(
                rating = binding.ratingBar.rating.toInt(),
                review = binding.etReview.text.toString(), productId = args.productId,
                reviewer = reviewerName!!,
                reviewerEmail = email!!)
            ).observe(viewLifecycleOwner){ response ->
                when (response) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            hideProgressBar()
                            Toast.makeText(requireContext(), "نظر شما ثبت شد!", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_addReviewFragment_to_detailFragment)
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            response.code?.let { showErrorSnack(message, it,reviewerName, email) }
                        }
                    }
                }
            }
        }
    }
    private fun hideProgressBar() {
        binding.layout.visibility = View.VISIBLE
        binding.lottie.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.lottie.setAnimation(R.raw.loading)
        binding.lottie.visibility = View.VISIBLE
        binding.layout.visibility = View.GONE
        binding.lottie.playAnimation()
    }

    private fun showErrorSnack(message: String, code: Int,reviewerName:String?,email:String?) {
        val snackBar = Snackbar.make(
            binding.layout,
            getErrorMessage(message, code),
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction(
            getString(R.string.try_again)
        ) {
            detailViewModel.createReview(Review(
                rating = binding.ratingBar.rating.toInt(),
                review = binding.etReview.text.toString(), productId = args.productId,
                reviewer = reviewerName!!,
                reviewerEmail = email!!))
            binding.lottie.playAnimation()
        }
        snackBar.show()
    }

}