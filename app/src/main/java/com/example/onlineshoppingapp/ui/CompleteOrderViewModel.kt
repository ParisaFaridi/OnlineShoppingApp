package com.example.onlineshoppingapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Order
import com.example.onlineshoppingapp.data.model.Shipping
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteOrderViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val order = MutableLiveData<Resource<Order>>()

    fun completeOrder(orderId:Int,shipping: Shipping) {
        order.postValue(Resource.Loading())
        viewModelScope.launch {
            val orderc = repository.getOrder(orderId)
            order.postValue(orderc.data?.lineItems?.let {
                repository.updateOrder(orderId,
                    it,shipping=shipping, status = "completed")
            })
            repository.deleteOrder()
        }
    }
}