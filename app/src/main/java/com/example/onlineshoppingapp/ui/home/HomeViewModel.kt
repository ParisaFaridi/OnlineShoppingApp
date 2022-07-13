package com.example.onlineshoppingapp.ui.home

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
class HomeViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val bestProducts = MutableLiveData<Resource<List<Product>>>()
    val newProducts = MutableLiveData<Resource<List<Product>>>()
    val mostViewedProducts = MutableLiveData<Resource<List<Product>>>()
    val onSaleProducts = MutableLiveData<Resource<List<Product>>>()
    val sliderPics = MutableLiveData<Resource<Product>>()

    init {
        getPicForSliders()
    }

    private fun getPicForSliders() {
        sliderPics.postValue(Resource.Loading())
        if (hasInternetConnection())
            viewModelScope.launch { sliderPics.postValue(repository.getProductById(608)) }
        else
            bestProducts.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }

    fun getOnSaleProducts() = handleApiCalls(getApplication<Application>().getString(R.string.date), onSaleProducts, onSale = true)

    fun getBestProducts() = handleApiCalls(getApplication<Application>().getString(R.string.rating), bestProducts)

    fun getNewProducts() = handleApiCalls(getApplication<Application>().getString(R.string.date), newProducts)

    fun getMostViewedProducts() = handleApiCalls(getApplication<Application>().getString(R.string.popularity), mostViewedProducts)

    private fun handleApiCalls(orderBy: String,
        productList: MutableLiveData<Resource<List<Product>>>,
        onSale:Boolean=false,perPage:Int= 10) = viewModelScope.launch {
            productList.postValue(Resource.Loading())
            if (hasInternetConnection()) {
                viewModelScope.launch {
                    productList.postValue(repository.getProducts(orderBy,onSale,perPage))
                }
            } else
                productList.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }
}