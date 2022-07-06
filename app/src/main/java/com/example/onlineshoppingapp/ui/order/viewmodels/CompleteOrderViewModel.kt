package com.example.onlineshoppingapp.ui.order.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Coupon
import com.example.onlineshoppingapp.data.model.Order
import com.example.onlineshoppingapp.data.model.Shipping
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteOrderViewModel @Inject constructor(
    private val repository: Repository,
    app: Application
) :
    AndroidViewModel(app) {

    val order = MutableLiveData<Resource<Order>>()
    val coupon = MutableLiveData<Resource<Coupon>>()

    fun completeOrder(orderId: Int, shipping: Shipping) {
        order.postValue(Resource.Loading())
        viewModelScope.launch {
            if (hasInternetConnection()) {
                val currentOrder = repository.getOrder(orderId)
                order.postValue(currentOrder.data?.lineItems?.let {
                    repository.updateOrder(
                        orderId,
                        it,
                        shipping = shipping,
                        status = getApplication<Application>().getString(R.string.completed),
                        couponLines = getCouponLine()
                    )
                })
                repository.deleteOrder()
            } else
                order.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(R.string.no_internet_error),
                        code = 1
                    )
                )
        }
    }

    private fun getCouponLine(): List<Coupon> {
        return if (coupon.value == null || coupon.value is Resource.Error)
            listOf()
        else {
            listOf(Coupon(code = coupon.value!!.data?.code!!))
        }
    }
    fun checkCoupon(couponCode: String) {
        coupon.postValue(Resource.Loading())
        viewModelScope.launch {
            if (hasInternetConnection()) {
                val coupons = repository.getCoupons().data
                if (coupons != null) {
                    for (i in coupons){
                        if (i.code == couponCode) {
                            coupon.postValue(Resource.Success(i))
                            order.value?.data?.total = (order.value?.data?.total?.toDouble()?.minus(i.amount?.toDouble()!!)).toString()
                            break
                        }
                        coupon.postValue(Resource.Error(getApplication<Application>().getString(R.string.coupon_error), code = 10))
                    }
                }
            } else {
                order.postValue(
                    Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
            }
        }
    }
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
}