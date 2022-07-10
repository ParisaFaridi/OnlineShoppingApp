package com.example.onlineshoppingapp.data.model

import com.squareup.moshi.Json

data class AttributeTerm(
    @Json(name = "count")
    val count: Int,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "menu_order")
    val menuOrder: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "slug")
    val slug: String,
    var isSelected :Boolean =false
)