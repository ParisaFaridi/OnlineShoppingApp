package com.example.onlineshoppingapp.network

import com.example.onlineshoppingapp.data.model.Category
import com.example.onlineshoppingapp.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val CONSUMER_KEY = "ck_33d507c4632f7d97ff70b4f3bae877a94375b177"
const val CONSUMER_SECRET = "cs_d172173de2ce65486fe0921aa1b9044e9b37535f"

interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("consumer_key")
        consumerKey :String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret :String = CONSUMER_SECRET,
        @Query("per_page")
        perPage :Int=20,
        @Query("page")
        pages:Int =1,
        @Query("orderby")
        orderBy:String
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path(value = "id")
        productId:Int,
        @Query("consumer_key")
        consumerKey :String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret :String = CONSUMER_SECRET
    ):Response<Product>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("consumer_key")
        consumerKey :String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret :String = CONSUMER_SECRET,
    ):Response<List<Category>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("consumer_key")
        consumerKey :String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret :String = CONSUMER_SECRET,
        @Query(value = "category")
        categoryId:Int
    ):Response<List<Product>>
}