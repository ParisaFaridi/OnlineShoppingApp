package com.example.onlineshoppingapp.data


import com.squareup.moshi.Json

data class Collection(
    @Json(name = "href")
    val href: String
)