package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Up(
    @Json(name = "href")
    val href: String
)