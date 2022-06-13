package com.example.onlineshoppingapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: Repository,app:Application):
    AndroidViewModel(app){

    val customerLive = MutableLiveData<Resource<Customer>>()

    fun signUp(customer: Customer) {
        customerLive.postValue((Resource.Loading()))
        if (hasInternetConnection())
            viewModelScope.launch { customerLive.postValue(repository.signUp(customer)) }
        else
            customerLive.postValue(Resource.Error("خطا در اتصال به اینترنت", code = 1))
    }
}