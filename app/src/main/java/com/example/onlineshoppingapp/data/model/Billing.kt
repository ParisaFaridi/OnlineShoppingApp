package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Billing(
    @Json(name = "address_1")
    val address1: String,
    @Json(name = "address_2")
    val address2: String?=null,
    @Json(name = "city")
    val city: String,
    @Json(name = "company")
    val company: String?=null,
    @Json(name = "country")
    val country: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "postcode")
    val postcode: String,
    @Json(name = "state")
    val state: String?=null
)