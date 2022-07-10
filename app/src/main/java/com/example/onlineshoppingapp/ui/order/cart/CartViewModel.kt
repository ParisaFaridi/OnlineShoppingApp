package com.example.onlineshoppingapp.ui.order.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.CartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    fun deleteProduct(id: Int) =viewModelScope.launch {
        repository.deleteProduct(id)
    }

    fun updateProductQuantity(newQuantity: Int, id: Int) =viewModelScope.launch {
        val mProduct = repository.getCartProduct(id)
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
    fun getAllCartProducts() = repository.getAllCartProducts()

    fun getTotalPrice() = repository.getTotalPrice()
}