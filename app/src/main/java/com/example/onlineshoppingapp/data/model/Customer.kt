package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Customer(
    @Json(name = "email")
    val email: String?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "shipping")
    val shipping: Shipping? = null,
    @Json(name = "username")
    val username: String?
)