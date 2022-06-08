package com.example.onlineshoppingapp.ui.categoryfragment

import android.app.Application
import androidx.lifecycle.*
import com.example.onlineshoppingapp.ConnectionLiveData
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Category
import com.example.onlineshoppingapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val hasInternetConnection = ConnectionLiveData(app)
    val products = MutableLiveData<Resource<List<Product>>>()

    fun getProductsByCategoryId(categoryId: Int) = viewModelScope.launch {
        if (hasInternetConnection.value == true)
            products.value = repository.getProductsByCategory(categoryId)
    }
}