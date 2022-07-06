package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Review(
    @Json(name = "date_created")
    val dateCreated: String,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "_links")
    val links: Links,
    @Json(name = "product_id")
    val productId: Int,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "review")
    val review: String,
    @Json(name = "reviewer")
    val reviewer: String,
    @Json(name = "reviewer_avatar_urls")
    val reviewerAvatarUrls: ReviewerAvatarUrls,
    @Json(name = "reviewer_email")
    val reviewerEmail: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "verified")
    val verified: Boolean,
    @Json(name = "product_name")
    val productName: String,
    @Json(name = "product_permalink")
    val productPermalink: String,
)