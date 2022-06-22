package com.example.onlineshoppingapp.ui.detailfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.LineItem
import com.example.onlineshoppingapp.data.model.Order
import com.example.onlineshoppingapp.data.model.OrderId
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val product = MutableLiveData<Resource<Product>>()

    fun getProduct(id : Int) = viewModelScope.launch {
        product.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                product.postValue(repository.getProductById(id))
            }
        } else
            product.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))

    }
    fun getOrder(quantity:Int) {
        viewModelScope.launch {
            if (repository.isOrderNew()== null){
                val response = repository.createOrder(
                    Order(lineItems = listOf(
                            LineItem(productId = product.value?.data?.id!!, quantity = quantity))))
                repository.insert(OrderId(response.data?.id!!))
            }else{
                repository.updateOrder(
                    repository.isOrderNew()!!.id,listOf(updateItemLines(quantity)))
            }
        }
    }
    suspend fun updateItemLines(newQuantity:Int):LineItem{
        val x = repository.getOrder(repository.isOrderNew()!!.id).data?.lineItems
        var id =0
        var quantity=0
        if (x != null) {
            for(i in x){
                if (i.productId == product.value?.data?.id){
                    id = i.id
                    quantity=i.quantity
                }
            }
        }
        return LineItem(id = id, productId = product.value?.data?.id!!, quantity = quantity+newQuantity)
    }
}