package com.example.onlineshoppingapp.ui.detailfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getProduct(id: Int): LiveData<Product> {
        val product = MutableLiveData<Product>()
        viewModelScope.launch {
            product.value = repository.getProductById(id)
        }
        return product
    }

}