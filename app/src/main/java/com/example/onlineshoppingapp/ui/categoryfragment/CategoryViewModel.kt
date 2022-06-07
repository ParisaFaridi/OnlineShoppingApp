package com.example.onlineshoppingapp.ui.categoryfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: Repository):ViewModel(){

    fun getCategoryById(id: Int): LiveData<Category> {
        val category = MutableLiveData<Category>()
        viewModelScope.launch {
            //category.value = repository.getCategoryById(id)
        }
        return category
    }
}