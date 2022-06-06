package com.example.onlineshoppingapp.network

import com.example.onlineshoppingapp.data.Product
import retrofit2.http.GET
import retrofit2.http.Query

const val CONSUMER_KEY = "ck_33d507c4632f7d97ff70b4f3bae877a94375b177"
const val CONSUMER_SECRET = "cs_d172173de2ce65486fe0921aa1b9044e9b37535f"


interface ApiService {

    @GET("products?consumer_key=$CONSUMER_KEY&consumer_secret=$CONSUMER_SECRET")
    suspend fun getProducts(
        @Query("per_page")
        perPage :Int=20,
        @Query("page")
        pages:Int =1,
        @Query("orderby")
        orderBy:String
    ):List<Product>
}