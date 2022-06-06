package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Links(
    @Json(name = "collection")
    val collection: List<Collection>,
    @Json(name = "self")
    val self: List<Self>
)