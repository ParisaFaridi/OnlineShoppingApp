package com.example.onlineshoppingapp.ui.homeFragment

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
class HomeViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    fun getBestProducts():LiveData<List<Product>>{
        val bestProducts = MutableLiveData<List<Product>>()
        viewModelScope.launch {
            bestProducts.value = repository.getProducts("rating")
        }
        return bestProducts
    }
    fun getNewProducts():LiveData<List<Product>>{
        val newProducts = MutableLiveData<List<Product>>()
        viewModelScope.launch {
            newProducts.value = repository.getProducts("date")
        }
        return newProducts
    }
    fun getMostViewedProducts():LiveData<List<Product>>{
        val mostViewedProducts = MutableLiveData<List<Product>>()
        viewModelScope.launch {
            mostViewedProducts.value = repository.getProducts("popularity")
        }
        return mostViewedProducts
    }
}