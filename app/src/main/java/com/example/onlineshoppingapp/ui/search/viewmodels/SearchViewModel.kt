package com.example.onlineshoppingapp.ui.search.viewmodels

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
class SearchViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {
    val searchResults = MutableLiveData<Resource<List<Product>>>()

    fun search(searchQuery : String,perPage:Int)  {

        searchResults.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                searchResults.postValue(repository.search(searchQuery,perPage,"title","desc", listOf(),
                    listOf()))
            }
        } else
            searchResults.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))

    }
}