package com.example.onlineshoppingapp.ui.categoriesfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.ConnectionLiveData
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: Repository,app:Application)
    : AndroidViewModel(app) {

    val categories = MutableLiveData<Resource<List<Category>>>()
    val hasInternetConnection = ConnectionLiveData(app)

    fun getCategories() = viewModelScope.launch {
        if (hasInternetConnection.value == true)
            categories.value = repository.getCategories()
    }
}