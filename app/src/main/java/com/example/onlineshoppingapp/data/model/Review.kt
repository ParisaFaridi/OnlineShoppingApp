package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Review(
    @Json(name = "date_created")
    val dateCreated: String?=null,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String?=null,
    @Json(name = "id")
    val id: Int=0,
    @Json(name = "_links")
    val links: Links?=null,
    @Json(name = "product_id")
    val productId: Int,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "review")
    val review: String,
    @Json(name = "reviewer")
    val reviewer: String,
    @Json(name = "reviewer_avatar_urls")
    val reviewerAvatarUrls: ReviewerAvatarUrls?=null,
    @Json(name = "reviewer_email")
    val reviewerEmail: String,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "verified")
    val verified: Boolean?=null,
    @Json(name = "product_name")
    val productName: String?=null,
    @Json(name = "product_permalink")
    val productPermalink: String?=null,
)