package com.example.onlineshoppingapp.ui.order.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
class CompleteOrderViewModel @Inject constructor(
    private val repository: Repository,
    app: Application) : AndroidViewModel(app) {

    val order = MutableLiveData<Resource<Order>>()
    val coupon = MutableLiveData<Resource<Coupon>>()
    var total = ""

    init {
        getAllAddresses()
    }
    fun completeOrder(address: Address,name:String) {
        order.postValue(Resource.Loading())
        viewModelScope.launch {
            if (hasInternetConnection()) {
                val splitName = name.split(" ")
                val shipping = Shipping(
                    address1 = address.address1,
                    address2 = "${address.lat},${address.long}",
                    city = address.city,
                    country = address.country,
                    firstName = splitName[0],
                    lastName = splitName[1],
                    postcode = address.postcode
                )
                order.postValue(repository.createOrder(
                        Order(
                            shipping = shipping,
                            status = getApplication<Application>().getString(R.string.completed),
                            couponLines = getCouponLine(),
                            lineItems = getLineItems()
                        )
                ))
                emptyCart()
            } else
                order.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        }
    }
    private fun getLineItems():List<LineItem>{
        val lineItems = arrayListOf<LineItem>()
        val cartProducts = repository.getAllCartProducts().value
        if (cartProducts != null) {
            for (product in cartProducts){
                lineItems.add(LineItem(productId = product.id,
                        quantity = product.quantity))
            }
        }
        return lineItems
    }
    private fun emptyCart() =viewModelScope.launch {  repository.emptyCart()}

    private fun getCouponLine(): List<Coupon> {
        return if (coupon.value == null || coupon.value is Resource.Error)
            listOf()
        else {
            listOf(Coupon(code = coupon.value!!.data?.code!!))
        }
    }
    fun checkCoupon(couponCode: String,oldTotal:String){
        coupon.postValue(Resource.Loading())
        viewModelScope.launch {
            if (hasInternetConnection()) {
                val coupons = repository.getCoupons().data
                if (coupons != null) {
                    for (i in coupons){
                        if (i.code == couponCode) {
                            coupon.postValue(Resource.Success(i))
                            total = (oldTotal.toDouble().minus(i.amount?.toDouble()!!)).toString()
                            break
                        }
                        coupon.postValue(Resource.Error(getApplication<Application>().getString(R.string.coupon_error)))
                    }
                }
            } else {
                order.postValue(
                    Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
            }
        }
    }
    fun getAllAddresses(): LiveData<List<Address?>> {
        lateinit var addresses : LiveData<List<Address?>>
        viewModelScope.launch {
            addresses = repository.getAllAddresses()
        }
        return addresses
    }
    fun addAddress(address: Address) = viewModelScope.launch {
        repository.insertAddress(address)
    }

    fun getTotalPrice() = repository.getTotalPrice()
}