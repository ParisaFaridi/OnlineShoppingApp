package com.example.onlineshoppingapp.network

import com.example.onlineshoppingapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

const val CONSUMER_KEY = "ck_63f4c52da932ddad1570283b31f3c96c4bd9fd6f"
const val CONSUMER_SECRET = "cs_294e7de35430398f323b43c21dd1b29f67b5370b"

interface ApiService {

    //product

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

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query(value = "category")
        categoryId: Int
    ): Response<List<Product>>

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
        @Query("attribute")
        attribute:List<String>,
        @Query("attribute_term")
        attributeTerms: List<Int>,
        @Query("search")
        searchQuery: String,
        @Query("orderby")
        orderBy: String,
        @Query("order")
        order: String
    ): Response<List<Product>>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET
    ): Response<List<Category>>


    @GET("products")
    suspend fun getRelatedProducts(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query("include")
        relatedIds: List<Int>
    ):Response<List<Product>>


    //customer

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


    //order

    @POST("orders")
    suspend fun createOrder(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Body order: Order
    ): Response<Order>


    //reviews

    @GET("products/reviews")
    suspend fun getProductReviews(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query("product")
        productId:Int
    ):Response<List<Review>>

    @POST("products/reviews")
    suspend fun createReview(
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Body review: Review
    ):Response<Review>

    @POST("products/reviews/{id}")
    suspend fun updateReview(
        @Path("id")
        id: Int,
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Body review: Review
    ):Response<Review>

    @DELETE("products/reviews/{id}")
    suspend fun deleteReview(
        @Path("id")
        id: Int,
        @Query("consumer_key")
        consumerKey: String = CONSUMER_KEY,
        @Query("consumer_secret")
        consumerSecret: String = CONSUMER_SECRET,
        @Query("force")
        force:Boolean=true
    ):Response<DeletedReview>

    //coupon
    @GET("coupons")
    suspend fun getCoupons(
    @Query("consumer_key")
    consumerKey: String = CONSUMER_KEY,
    @Query("consumer_secret")
    consumerSecret: String = CONSUMER_SECRET
    ):Response<List<Coupon>>
}