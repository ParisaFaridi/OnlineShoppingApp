package com.example.onlineshoppingapp.ui.detailfragment

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
class DetailViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val hasInternetConnection = ConnectionLiveData(app)
    val product = MutableLiveData<Resource<Product>>()

    fun getProduct(id: Int) = viewModelScope.launch {
        if (hasInternetConnection.value == true)
            product.postValue(repository.getProductById(id))
    }
}