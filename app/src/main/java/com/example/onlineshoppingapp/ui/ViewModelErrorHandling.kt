package com.example.onlineshoppingapp.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.Errors
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.hasInternetConnection
import kotlinx.coroutines.launch
import java.lang.Exception


fun <T> AndroidViewModel.handle(liveDataObject: MutableLiveData<Resource<T>>, repoResponse : Resource<T>){

    liveDataObject.postValue(Resource.Loading())
    try {
        if (hasInternetConnection())
            viewModelScope.launch { liveDataObject.postValue(repoResponse) }
        else
            liveDataObject.postValue(Resource.Error(Errors.INTERNET_FAILURE.message, code = 1))
    }catch (e:Exception){
        liveDataObject.postValue(Resource.Error(Errors.INTERNET_FAILURE.message, code = 1))
    }


}