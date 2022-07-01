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
        getFilters(3,colorFilters)
        getFilters(4,sizeFilters)
    }
    fun getFilters( filterId : Int,liveData:MutableLiveData<Resource<List<AttributeTerm>>>){
        liveData.postValue(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                liveData.postValue(repository.getAttributeItems(filterId))
            }
        }else
            liveData.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }

    fun search(query : String=searchQuery,orderBy:String, order:String = getApplication<Application>().getString(R.string.desc)) =
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
                    attrs.add(getApplication<Application>().getString(R.string.pa_color))
            }
        }
        for (i in sizeFilters.value?.data!!){
            if (i.isSelected) {
                ids.add(i.id)
                if (attrs[1].isEmpty())
                    attrs.add(getApplication<Application>().getString(R.string.pa_size))
            }
        }
        filtersIds = ids
        attributes=attrs
    }
}