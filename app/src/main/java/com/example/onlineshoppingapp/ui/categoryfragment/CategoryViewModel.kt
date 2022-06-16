package com.example.onlineshoppingapp.ui.categoryfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.ui.handle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {

    val products = MutableLiveData<Resource<List<Product>>>()

    fun getProductsByCategoryId(categoryId: Int) = viewModelScope.launch {
        handle(products,repository.getProductsByCategory(categoryId))
    }

}