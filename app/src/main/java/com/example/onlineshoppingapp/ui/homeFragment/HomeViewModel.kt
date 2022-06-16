package com.example.onlineshoppingapp.ui.homeFragment

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

    fun getBestProducts() = handleApiCalls("rating",bestProducts)

    fun getNewProducts() = handleApiCalls("date",newProducts)

    fun getMostViewedProducts() = handleApiCalls("popularity",mostViewedProducts)

    private fun handleApiCalls(orderBy:String, productList :MutableLiveData<Resource<List<Product>>>){
        productList.postValue(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                productList.postValue(repository.getProducts(orderBy))
            }
        }else
            bestProducts.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }
}