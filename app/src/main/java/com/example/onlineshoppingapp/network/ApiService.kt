package com.example.onlineshoppingapp.network

import com.example.onlineshoppingapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

const val CONSUMER_KEY = "ck_33d507c4632f7d97ff70b4f3bae877a94375b177"
const val CONSUMER_SECRET = "cs_d172173de2ce65486fe0921aa1b9044e9b37535f"

interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query("per_page")
        perPage: Int,
        @Query("page")
        pages: Int = 1,
        @Query("orderby")
        orderBy: String,
        @Query("on_sale")
        onSale: Boolean,
        @Query("exclude")
        excludes: Array<Int> = arrayOf(608)
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path(value = "id")
        productId: Int,
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET
    ): Response<Product>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET
    ): Response<List<Category>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query(value = "category")
        categoryId: Int
    ): Response<List<Product>>

    @POST("customers")
    suspend fun signUp(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Body customer: Customer
    ): Response<Customer>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path(value = "id")
        id: Int,
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET

    ): Response<Customer>

    @POST("orders")
    suspend fun createOrder(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Body order: Order
    ): Response<Order>

    @GET("products/attributes/{attribute_id}/terms")
    suspend fun getAttributeItems(
        @Path("attribute_id")
        id: Int,
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET
    ): Response<List<AttributeTerm>>

    @GET("products")
    suspend fun getFilteredProducts(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query("per_page")
        perPage: Int,
        @Query("page")
        pages: Int = 1,
        @Query("exclude")
        excludes: Array<Int> = arrayOf(608),
        @Query("attribute_term")
        attributeTerms: List<Int>,
        @Query("search")
        searchQuery: String,
        @Query("orderby")
        orderBy: String,
        @Query("order")
        order: String
    ): Response<List<Product>>

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id")
        id: Int,
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Body order: Order
    ): Response<Order>
}