package com.example.onlineshoppingapp.ui.search.viewmodels

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
    val searchResults = MutableLiveData<Resource<List<Product>>>()
    var searchQuery = ""
    private var attributes = listOf<String>()
    private var filtersIds= listOf<Int>()

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
    private fun getSizeFilter() = viewModelScope.launch {

        sizeFilters.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                sizeFilters.postValue(repository.getAttributeItems(4))
            }
        } else
            sizeFilters.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }

    fun search(query : String=searchQuery,orderBy:String, order:String = "desc") =
        viewModelScope.launch {
        searchResults.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                searchResults.postValue(repository.search(query,50, orderBy = orderBy, order = order,filtersIds,attributes))
                searchQuery = query
            }
        } else
            searchResults.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))

    }
    fun addFilters(){
        val ids = arrayListOf<Int>()
        val attrs = arrayListOf("","")
        for (i in colorFilters.value?.data!!){
            if (i.isSelected) {
                ids.add(i.id)
                if (attrs[0].isEmpty())
                    attrs.add("pa_color")
            }
        }
        for (i in sizeFilters.value?.data!!){
            if (i.isSelected) {
                ids.add(i.id)
                if (attrs[1].isEmpty())
                    attrs.add("pa_size")
            }
        }
        filtersIds = ids
        attributes=attrs
    }
}