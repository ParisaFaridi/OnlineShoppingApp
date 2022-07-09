package com.example.onlineshoppingapp.ui.category.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Category
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: Repository,
    app: Application
) : AndroidViewModel(app) {

    val categories = MutableLiveData<Resource<List<Category>>>()

    fun getCategories() =viewModelScope.launch {
        categories.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                categories.postValue(repository.getCategories())
            }
        } else
            categories.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))

    }

}