package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.data.model.*
import com.example.onlineshoppingapp.network.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    //product
    suspend fun getProducts(orderBy: String, onSale: Boolean, perPage: Int) =
        apiService.getProducts(orderBy = orderBy, onSale = onSale, perPage = perPage)

    suspend fun getProductById(id: Int) = apiService.getProductById(productId = id)

    suspend fun getProductsByCategory(categoryId: Int) =
        apiService.getProductsByCategory(categoryId = categoryId)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun search(searchQuery: String, perPage: Int,
        orderBy: String, order: String, ids: List<Int>, attribute: List<String>) =
        apiService.getFilteredProducts(
            attribute = attribute, searchQuery = searchQuery, perPage = perPage,
            orderBy = orderBy, order = order, attributeTerms = ids)

    suspend fun getAttributeItems(id: Int) = apiService.getAttributeItems(id)

    suspend fun getCoupons() = apiService.getCoupons()


    //customer
    suspend fun signUp(customer: Customer) = apiService.signUp(customer = customer)

    suspend fun getCustomer(id: Int) = apiService.getCustomer(id = id)

    //order
    suspend fun createOrder(order: Order) = apiService.createOrder(order = order)

    suspend fun getOrder(id: Int) = apiService.getOrder(id = id)

    suspend fun updateOrder(orderId: Int, listOf: List<LineItem>, shipping: Shipping?,
        status: String, couponLines: List<Coupon>) =
        apiService.updateOrder(id = orderId, order = Order(id = orderId, lineItems = listOf,
                shipping = shipping, status = status, couponLines = couponLines))


    //review
    suspend fun getProductReviews(id: Int) = apiService.getProductReviews(productId = id)

    suspend fun createReview(review: Review) = apiService.createReview(review = review)

    suspend fun updateReview(review: Review, id: Int) =
        apiService.updateReview(review = review, id = id)

    suspend fun deleteReview(id: Int) = apiService.deleteReview(id = id)

}