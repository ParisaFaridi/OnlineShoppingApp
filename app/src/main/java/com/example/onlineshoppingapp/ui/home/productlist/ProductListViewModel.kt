package com.example.onlineshoppingapp.ui.home.productlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: Repository, app: Application)
    : AndroidViewModel(app) {

    val productList = MutableLiveData<Resource<List<Product>>>()

    fun getProducts() = viewModelScope.launch {
        productList.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                productList.postValue(
                    repository.getProducts("title", false, 100)
                )
            }
        }else
            productList.postValue(
                Resource.Error(
                    getApplication<Application>().getString(R.string.no_internet_error), code = 1))

    }
}