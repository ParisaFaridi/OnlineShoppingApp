package com.example.onlineshoppingapp.ui.detailfragment

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
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val product = MutableLiveData<Resource<Product>>()

    fun getProduct(id: Int){
        product.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                viewModelScope.launch {
                    product.postValue(repository.getProductById(id))
                }
            }else
                product.postValue(Resource.Error("خطا در اتصال به اینترنت"))
        }catch (t :Throwable){
            product.postValue(Resource.Error("خطا در اتصال به سرور"))
        }
    }
}