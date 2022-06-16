package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.data.model.Order
import com.example.onlineshoppingapp.network.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(orderBy: String,onSale:Boolean,perPage: Int) = apiService.getProducts(orderBy = orderBy, onSale = onSale, perPage = perPage)

    suspend fun getProductById(id:Int) = apiService.getProductById(productId = id)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getProductsByCategory(categoryId:Int) = apiService.getProductsByCategory(categoryId = categoryId)

    suspend fun search(searchQuery:String,perPage:Int,orderBy: String,order: String) = apiService.search(searchQuery = searchQuery, perPage = perPage, orderBy = orderBy, order = order)

    suspend fun signUp(customer: Customer) = apiService.signUp(customer = customer)

    suspend fun createOrder(order: Order) = apiService.createOrder(order = order)

    suspend fun getCustomer(id:Int) = apiService.getCustomer(id = id)
}