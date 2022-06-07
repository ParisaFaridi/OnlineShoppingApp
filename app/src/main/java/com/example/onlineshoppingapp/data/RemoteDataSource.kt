package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.network.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(orderBy: String) = apiService.getProducts(orderBy = orderBy)

    suspend fun getProductById(id:Int) = apiService.getProductById(id)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getProductsByCategory(categoryId:Int) = apiService.getProductsByCategory(categoryId)

}