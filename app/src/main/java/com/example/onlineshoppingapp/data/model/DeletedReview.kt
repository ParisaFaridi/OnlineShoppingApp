package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class DeletedReview(
    @Json(name = "deleted")
    val deleted: Boolean,
    @Json(name = "previous")
    val previous: Review
)