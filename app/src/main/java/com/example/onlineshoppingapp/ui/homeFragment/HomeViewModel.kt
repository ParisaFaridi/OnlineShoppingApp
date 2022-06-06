package com.example.onlineshoppingapp.ui.homeFragment

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

    val bestProducts = MutableLiveData<List<Product>>()
    val newProducts = MutableLiveData<List<Product>>()
    val mostViewedProducts = MutableLiveData<List<Product>>()

    init {
        getBestProducts()
        getNewProducts()
        getMostViewedProducts()
    }

    private fun getBestProducts(){
        viewModelScope.launch {
            bestProducts.value = repository.getProducts("rating")
        }
    }
    private fun getNewProducts(){
        viewModelScope.launch {
            newProducts.value = repository.getProducts("date")
        }
    }
    private fun getMostViewedProducts(){
        viewModelScope.launch {
            mostViewedProducts.value = repository.getProducts("popularity")
        }
    }
}