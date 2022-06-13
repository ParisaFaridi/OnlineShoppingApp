package com.example.onlineshoppingapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {
    val searchResults = MutableLiveData<Resource<List<Product>>>()

    fun search(searchQuery : String){
        searchResults.postValue(Resource.Loading())
        if (hasInternetConnection())
            viewModelScope.launch { searchResults.postValue(repository.search(searchQuery)) }
        else
            searchResults.postValue(Resource.Error("خطا در اتصال به اینترنت", code = 1))
    }
}