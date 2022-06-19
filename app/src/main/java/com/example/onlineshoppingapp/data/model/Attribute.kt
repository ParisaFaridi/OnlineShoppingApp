package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Attribute(
    @Json(name = "has_archives")
    val hasArchives: Boolean,
    @Json(name = "id")
    val id: Int,
    @Json(name = "_links")
    val links: Links,
    @Json(name = "name")
    val name: String,
    @Json(name = "order_by")
    val orderBy: String,
    @Json(name = "slug")
    val slug: String,
    @Json(name = "type")
    val type: String
)