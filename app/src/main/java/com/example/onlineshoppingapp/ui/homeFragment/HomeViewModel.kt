package com.example.onlineshoppingapp.ui.homeFragment

import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshoppingapp.ConnectionLiveData
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val bestProducts = MutableLiveData<Resource<List<Product>>>()
    val newProducts = MutableLiveData<Resource<List<Product>>>()
    val mostViewedProducts = MutableLiveData<Resource<List<Product>>>()
    val hasInternetConnection = ConnectionLiveData(app)

    init {
        getBestProducts()
        getNewProducts()
        getMostViewedProducts()
    }

    fun getBestProducts() = viewModelScope.launch {
        if (hasInternetConnection.value == true)
            bestProducts.postValue(repository.getProducts("rating"))
    }

    fun getNewProducts() = viewModelScope.launch {
        if (hasInternetConnection.value == true)
            newProducts.value = repository.getProducts("date")
    }

    fun getMostViewedProducts() = viewModelScope.launch {
        if (hasInternetConnection.value == true)
            mostViewedProducts.value = repository.getProducts("popularity")
    }

}