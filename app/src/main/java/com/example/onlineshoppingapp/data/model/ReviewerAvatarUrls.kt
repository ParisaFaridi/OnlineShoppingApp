package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class ReviewerAvatarUrls(
    @Json(name = "24")
    val x24: String,
    @Json(name = "48")
    val x48: String,
    @Json(name = "96")
    val x96: String
)