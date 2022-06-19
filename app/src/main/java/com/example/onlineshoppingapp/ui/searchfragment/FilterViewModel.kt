package com.example.onlineshoppingapp.ui.searchfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.AttributeTerm
import com.example.onlineshoppingapp.data.model.Product
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FilterViewModel @Inject constructor(private val repository: Repository, app: Application) : AndroidViewModel(app)  {

    val colorFilters = MutableLiveData<Resource<List<AttributeTerm>>>()
    val sizeFilters = MutableLiveData<Resource<List<AttributeTerm>>>()
    var searchQuery = ""

    init {
        getColorFilter()
        getSizeFilter()
    }

    fun getColorFilter() = viewModelScope.launch {

        colorFilters.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                colorFilters.postValue(repository.getAttributeItems(3))
            }
        } else
            colorFilters.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }
    fun getSizeFilter() = viewModelScope.launch {

        sizeFilters.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                sizeFilters.postValue(repository.getAttributeItems(4))
            }
        } else
            sizeFilters.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }
    val searchResults = MutableLiveData<Resource<List<Product>>>()

    fun search(query : String, perPage:Int, orderBy:String, order:String = "desc") = viewModelScope.launch {

        searchResults.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                searchResults.postValue(repository.search(query, perPage, orderBy = orderBy, order = order))
                searchQuery = query
            }
        } else
            searchResults.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))

    }
    fun getFilteredList(){
        val filterIds = arrayListOf<Int>()
        for (i in colorFilters.value?.data!!){
            if (i.isSelected)
                filterIds.add(i.id)
        }
        for (i in sizeFilters.value?.data!!){
            if (i.isSelected)
                filterIds.add(i.id)
        }
        val ids :List<Int> = filterIds
        if (filterIds.isNotEmpty()){
            searchResults.postValue(Resource.Loading())
            if (hasInternetConnection()) {
                viewModelScope.launch {
                    searchResults.postValue(repository.getFilteredProducts(ids,searchQuery))
                }
            } else
                searchResults.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        }
    }
}