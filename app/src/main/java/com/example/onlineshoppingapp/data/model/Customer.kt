package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Customer(
    @Json(name = "avatar_url")
    val avatarUrl: String? = null,
    @Json(name = "date_created")
    val dateCreated: String? = null,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String? = null,
    @Json(name = "date_modified")
    val dateModified: String? = null,
    @Json(name = "date_modified_gmt")
    val dateModifiedGmt: String? = null,
    @Json(name = "is_paying_customer")
    val isPayingCustomer: Boolean? = null,
    @Json(name = "_links")
    val links: Links? = null,
    @Json(name = "meta_data")
    val metaData: List<Any>? = null,
    @Json(name = "role")
    val role: String? = null,
    @Json(name = "billing")
    val billing: Billing? = null,
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