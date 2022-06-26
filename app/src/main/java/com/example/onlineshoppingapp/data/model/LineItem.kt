package com.example.onlineshoppingapp.data.model

import com.squareup.moshi.Json

data class LineItem(
    @Json(name = "id")
    val id: Int=0,
    @Json(name = "meta_data")
    val metaData: List<Any>?=null,
    @Json(name = "name")
    val name: String?=null,
    @Json(name = "parent_name")
    val parentName: Any?= null,
    @Json(name = "price")
    var price: Double?= null,
    @Json(name = "product_id")
    val productId: Int,
    @Json(name = "quantity")
    var quantity: Int,
    @Json(name = "sku")
    val sku: String?= null,
    @Json(name = "subtotal")
    val subtotal: String?= null,
    @Json(name = "subtotal_tax")
    val subtotalTax: String?= null,
    @Json(name = "tax_class")
    val taxClass: String?= null,
    @Json(name = "taxes")
    val taxes: List<Any>?= null,
    @Json(name = "total")
    var total: String?= null,
    @Json(name = "total_tax")
    val totalTax: String?= null,
    @Json(name = "variation_id")
    val variationId: Int?= null,
)