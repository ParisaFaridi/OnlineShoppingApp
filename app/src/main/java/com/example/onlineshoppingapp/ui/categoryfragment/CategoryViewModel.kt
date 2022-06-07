package com.example.onlineshoppingapp.ui.categoryfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Category
import com.example.onlineshoppingapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: Repository):ViewModel(){

    fun getCategoryById(categoryId :Int): LiveData<List<Product>> {
        val products = MutableLiveData<List<Product>>()
        viewModelScope.launch {
            products.value = repository.getProductsByCategory(categoryId)
        }
        return products
    }
}