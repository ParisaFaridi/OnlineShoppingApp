package com.example.onlineshoppingapp.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.*
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val product = MutableLiveData<Resource<Product>>()
    val reviews = MutableLiveData<Resource<List<Review>>>()

    fun getProduct(id : Int) = viewModelScope.launch {
        product.postValue(Resource.Loading())
        reviews.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                product.postValue(repository.getProductById(id))
                reviews.postValue(repository.getProductReviews(id))
            }
        } else {
            product.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
            reviews.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        }
    }

    fun createReview(review: Review):MutableLiveData<Resource<Review>>{
        val mReview = MutableLiveData<Resource<Review>>(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                mReview.postValue(repository.createReview(review))
            }
        }else
            mReview.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        return mReview
    }
    fun updateReview(review: Review):MutableLiveData<Resource<Review>>{
        val mReview = MutableLiveData<Resource<Review>>(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                mReview.postValue(repository.updateReview(review,review.id))
            }
        }else
            mReview.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        return mReview


    }
    fun deleteReview(id: Int):MutableLiveData<Resource<DeletedReview>>{
        val mReview = MutableLiveData<Resource<DeletedReview>>(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                mReview.postValue(repository.deleteReview(id))
            }
        }else
            mReview.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        return mReview
    }

    fun addToCart(quantity:Int) =viewModelScope.launch{
        if (!repository.exists(product.value?.data?.id!!)){
            product.value!!.data?.let {
                repository.insertCartProduct(cartProduct = CartProduct(
                    it.id!!,
                    it.name!!,
                    it.images?.get(0)?.src!!,
                    quantity,
                    it.price!!,
                    (it.price.toLong() * quantity.toLong()).toString()
                )
                )
            }
        }else{
            val mProduct = repository.getCartProduct(product.value?.data?.id!!)
            val newQuantity = quantity + mProduct.quantity
            val newTotal = newQuantity * mProduct.price.toLong()
            repository.insertCartProduct(
                CartProduct(mProduct.id,
                    mProduct.name,
                    mProduct.image,
                    newQuantity,
                    mProduct.price,
                    newTotal.toString()
                    )
            )
        }
    }
}