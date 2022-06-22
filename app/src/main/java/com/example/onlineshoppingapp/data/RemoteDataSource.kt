package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.data.model.Customer
import com.example.onlineshoppingapp.data.model.LineItem
import com.example.onlineshoppingapp.data.model.Order
import com.example.onlineshoppingapp.network.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(orderBy: String,onSale:Boolean,perPage: Int) = apiService.getProducts(orderBy = orderBy, onSale = onSale, perPage = perPage)

    suspend fun getProductById(id:Int) = apiService.getProductById(productId = id)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getProductsByCategory(categoryId:Int) = apiService.getProductsByCategory(categoryId = categoryId)

    suspend fun search(searchQuery:String,perPage:Int,orderBy: String,order: String,ids:List<Int>,attribute: List<String>) =
        apiService.getFilteredProducts(attribute = attribute, searchQuery = searchQuery,perPage= perPage,orderBy = orderBy, order = order,attributeTerms = ids)

    suspend fun signUp(customer: Customer) = apiService.signUp(customer = customer)

    suspend fun createOrder(order: Order) = apiService.createOrder(order = order)

    suspend fun getCustomer(id:Int) = apiService.getCustomer(id = id)

    suspend fun getOrder(id:Int) = apiService.getOrder(id = id)

    suspend fun getAttributeItems(id :Int) = apiService.getAttributeItems(id)
    suspend fun updateOrder(orderId: Int, listOf: List<LineItem>) = apiService.updateOrder(id = orderId,order = Order(id = orderId, lineItems = listOf))
}