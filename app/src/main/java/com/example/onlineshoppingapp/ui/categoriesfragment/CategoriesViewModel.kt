package com.example.onlineshoppingapp.ui.categoriesfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val categories = MutableLiveData<List<Category>>()

    init {
        getCategories()
    }

    private fun getCategories() = viewModelScope.launch {
        categories.value = repository.getCategories()
    }
}