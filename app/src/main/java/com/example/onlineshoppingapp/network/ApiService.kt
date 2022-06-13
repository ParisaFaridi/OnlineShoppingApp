package com.example.onlineshoppingapp.network

import com.example.onlineshoppingapp.data.model.Category
import com.example.onlineshoppingapp.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val CONSUMER_KEY = "ck_33d507c4632f7d97ff70b4f3bae877a94375b177"
const val CONSUMER_SECRET = "cs_d172173de2ce65486fe0921aa1b9044e9b37535f"
const val CONSUMER_KEY_AND_SECRET = "consumer_key=$CONSUMER_KEY&consumer_secret=$CONSUMER_SECRET"

interface ApiService {

    @GET("products?$CONSUMER_KEY_AND_SECRET")
    suspend fun getProducts(
        @Query("per_page")
        perPage :Int=10,
        @Query("page")
        pages:Int =1,
        @Query("orderby")
        orderBy:String
    ): Response<List<Product>>

    @GET("products/{id}?$CONSUMER_KEY_AND_SECRET")
    suspend fun getProductById(
        @Path(value = "id")
        productId:Int
    ):Response<Product>

    @GET("products/categories?$CONSUMER_KEY_AND_SECRET")
    suspend fun getCategories():Response<List<Category>>

    @GET("products?$CONSUMER_KEY_AND_SECRET")
    suspend fun getProductsByCategory(
        @Query(value = "category")
        categoryId:Int
    ):Response<List<Product>>

    @GET("products?$CONSUMER_KEY_AND_SECRET")
    suspend fun search(
        @Query("search")
        searchQuery:String,
        @Query("per_page")
        perPage :Int=7
    ):Response<List<Product>>
}