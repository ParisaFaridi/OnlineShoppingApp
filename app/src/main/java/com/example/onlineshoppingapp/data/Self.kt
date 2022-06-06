package com.example.onlineshoppingapp.data


import com.squareup.moshi.Json

data class Self(
    @Json(name = "href")
    val href: String
)