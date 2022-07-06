package com.example.onlineshoppingapp.ui.detailfragment

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
    val order = MutableLiveData<Resource<Order>>()
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
    fun createOrder(quantity:Int) {
        viewModelScope.launch {
            if (repository.isOrderNew()== null){
                order.postValue(repository.createOrder(
                    Order(lineItems = listOf(
                            LineItem(productId = product.value?.data?.id!!, quantity = quantity)))))
                if (order.value != null)
                    repository.insert(OrderId(order.value?.data?.id!!))
            }else{
                repository.updateOrder(
                    repository.isOrderNew()!!.id,listOf(updateItemLines(quantity)), couponLines =listOf())
            }
        }
    }
    private suspend fun updateItemLines(newQuantity:Int):LineItem{
        val mOrder = repository.getOrder(repository.isOrderNew()!!.id).data?.lineItems
        var id =0
        var quantity=0
        if (mOrder != null) {
            for(i in mOrder){
                if (i.productId == product.value?.data?.id){
                    id = i.id
                    quantity=i.quantity
                }
            }
        }
        return LineItem(id = id, productId = product.value?.data?.id!!, quantity = quantity+newQuantity)
    }
}