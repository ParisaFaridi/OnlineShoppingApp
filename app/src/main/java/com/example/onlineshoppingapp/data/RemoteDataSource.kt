package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.network.ApiService
import java.nio.ByteOrder
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(orderBy: String) = apiService.getProducts(orderBy = orderBy)

    suspend fun getProductById(id:Int) = apiService.getProductById(id)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getProductsByCategory(categoryId:Int) = apiService.getProductsByCategory(categoryId)

    suspend fun search(searchQuery:String,perPage:Int,orderBy: String,order: String) = apiService.search(searchQuery,perPage,orderBy,order)

    suspend fun signUp(customer: Customer) = apiService.signUp(customer)
}