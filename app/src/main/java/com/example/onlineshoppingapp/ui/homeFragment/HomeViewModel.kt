package com.example.onlineshoppingapp.ui.homeFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val bestProducts = MutableLiveData<Resource<List<Product>>>()
    val newProducts = MutableLiveData<Resource<List<Product>>>()
    val mostViewedProducts = MutableLiveData<Resource<List<Product>>>()

    val sliderPics = MutableLiveData<Resource<Product>>()
    init {
        getPicForSliders()
    }

    private fun getPicForSliders() {
        sliderPics.postValue(Resource.Loading())
        if (hasInternetConnection())
            viewModelScope.launch { sliderPics.postValue(repository.getProductById(608)) }
        else
            bestProducts.postValue(Resource.Error("خطا در اتصال به اینترنت", code = 1))
    }


    fun getBestProducts() = handleApiCalls("rating", bestProducts)

    fun getNewProducts() = handleApiCalls("date", newProducts)

    fun getMostViewedProducts() = handleApiCalls("popularity", mostViewedProducts)

    private fun handleApiCalls(orderBy: String, productList: MutableLiveData<Resource<List<Product>>>) {
        productList.postValue(Resource.Loading())
        if (hasInternetConnection())
            viewModelScope.launch { productList.postValue(repository.getProducts(orderBy)) }
        else
            bestProducts.postValue(Resource.Error("خطا در اتصال به اینترنت", code = 1))
    }
}