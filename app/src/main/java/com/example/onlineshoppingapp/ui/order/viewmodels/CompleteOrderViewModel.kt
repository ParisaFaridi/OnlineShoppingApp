package com.example.onlineshoppingapp.ui.order.viewmodels

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
            if (hasInternetConnection()) {
                val currentOrder = repository.getOrder(orderId)
                order.postValue(currentOrder.data?.lineItems?.let {
                    repository.updateOrder(
                        orderId,
                        it, shipping = shipping, status = getApplication<Application>().getString(R.string.completed)
                    )
                })
                repository.deleteOrder()
            }else
                order.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        }
    }
}