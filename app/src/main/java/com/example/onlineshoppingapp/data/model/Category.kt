package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Category(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "display")
    val display: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image")
    val image: Image?,
    @Json(name = "menu_order")
    val menuOrder: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "parent")
    val parent: Int?,
    @Json(name = "slug")
    val slug: String?
)