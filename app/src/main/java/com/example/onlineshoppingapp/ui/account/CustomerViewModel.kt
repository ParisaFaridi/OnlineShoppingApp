package com.example.onlineshoppingapp.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.ui.handle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(private val repository: Repository,app:Application):
    AndroidViewModel(app){

    var customerLive = MutableLiveData<Resource<Customer>>()

    fun signUp(customer: Customer) = viewModelScope.launch {
        handle(liveDataObject = customerLive, repoResponse = repository.signUp(customer))
    }

    fun getCustomer(id:Int) = viewModelScope.launch {
            handle(liveDataObject = customerLive , repoResponse = repository.getCustomer(id))
    }
}