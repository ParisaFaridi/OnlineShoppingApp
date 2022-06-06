package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Self(
    @Json(name = "href")
    val href: String
)