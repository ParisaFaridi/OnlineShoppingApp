package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Reviewer(
    @Json(name = "embeddable")
    val embeddable: Boolean,
    @Json(name = "href")
    val href: String
)