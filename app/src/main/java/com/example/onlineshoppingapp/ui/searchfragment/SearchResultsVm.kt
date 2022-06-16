package com.example.onlineshoppingapp.ui.searchfragment

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
class SearchResultsVm @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app) {
    val searchResults = MutableLiveData<Resource<List<Product>>>()

    fun search(searchQuery : String, perPage:Int, orderBy:String, order:String = "desc") = viewModelScope.launch {
        handle(searchResults,repository.search(searchQuery, perPage, orderBy = orderBy, order = order))
    }
}