package com.example.onlineshoppingapp.ui.order.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Order
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val order = MutableLiveData<Resource<Order>>()

    fun getOrder() = viewModelScope.launch {
        if (repository.isOrderNew() == null) {
            order.postValue(
                Resource.Error(
                    getApplication<Application>().getString(R.string.empty_cart),
                    code = 2
                )
            )
            return@launch
        }
        order.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                order.postValue(repository.getOrder(repository.isOrderNew()!!.id))
            }
        } else
            order.postValue(
                Resource.Error(
                    getApplication<Application>().getString(R.string.no_internet_error),
                    code = 1
                )
            )
    }

    fun updateQuantity(id: Int, newQuantity: Int) {
        order.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                val lineItems = order.value?.data?.lineItems!!
                for (i in lineItems) {
                    if (i.id == id) {
                        i.quantity = newQuantity
                        i.total = (i.price?.times(i.quantity)).toString()
                        order.postValue(repository.updateOrder(order.value!!.data?.id!!, lineItems))
                    }
                }
            }
        } else
            order.postValue(
                Resource.Error(
                    getApplication<Application>().getString(R.string.no_internet_error),
                    code = 1
                )
            )
    }
}