package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Tag(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "slug")
    val slug: String
)