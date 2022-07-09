package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class MetaData(
    @Json(name = "id")
    val id: Int,
    @Json(name = "key")
    val key: String,
    @Json(name = "value")
    val value: String
)