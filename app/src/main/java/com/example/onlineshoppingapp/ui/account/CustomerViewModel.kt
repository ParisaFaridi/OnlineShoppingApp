package com.example.onlineshoppingapp.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: Repository,app:Application):
    AndroidViewModel(app){

    var customerLive = MutableLiveData<Resource<Customer>>()

    fun signUp(customer: Customer)  {
        customerLive.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                customerLive.postValue(repository.signUp(customer))
            }
        } else
            customerLive.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }

    fun getCustomer(id:Int)  {
        customerLive.postValue(Resource.Loading())
        if (hasInternetConnection()) {
            viewModelScope.launch {
                customerLive.postValue(repository.getCustomer(id))
            }
        } else
            customerLive.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
    }
}