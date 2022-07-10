package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Coupon(
    @Json(name = "amount")
    val amount: String?=null,
    @Json(name = "code")
    val code: String?=null,
    @Json(name = "date_created")
    val dateCreated: String?=null,
    @Json(name = "date_created_gmt")
    val dateCreatedGmt: String?=null,
    @Json(name = "date_expires")
    val dateExpires: String?=null,
    @Json(name = "date_expires_gmt")
    val dateExpiresGmt: String?=null,
    @Json(name = "date_modified")
    val dateModified: String?=null,
    @Json(name = "date_modified_gmt")
    val dateModifiedGmt: String?=null,
    @Json(name = "description")
    val description: String?=null,
    @Json(name = "discount_type")
    val discountType: String?=null,
    @Json(name = "email_restrictions")
    val emailRestrictions: List<Any>?=null,
    @Json(name = "exclude_sale_items")
    val excludeSaleItems: Boolean?=null,
    @Json(name = "excluded_product_categories")
    val excludedProductCategories: List<Any>?=null,
    @Json(name = "excluded_product_ids")
    val excludedProductIds: List<Any>?=null,
    @Json(name = "free_shipping")
    val freeShipping: Boolean?=null,
    @Json(name = "id")
    val id: Int?=null,
    @Json(name = "individual_use")
    val individualUse: Boolean?=null,
    @Json(name = "limit_usage_to_x_items")
    val limitUsageToXItems: Any?=null,
    @Json(name = "maximum_amount")
    val maximumAmount: String?=null,
    @Json(name = "meta_data")
    val metaData: List<Any>?=null,
    @Json(name = "minimum_amount")
    val minimumAmount: String?=null,
    @Json(name = "product_categories")
    val productCategories: List<Any>?=null,
    @Json(name = "product_ids")
    val productIds: List<Any>?=null,
    @Json(name = "status")
    val status: String?=null,
    @Json(name = "usage_count")
    val usageCount: Int?=null,
    @Json(name = "usage_limit")
    val usageLimit: Any?=null,
    @Json(name = "usage_limit_per_user")
    val usageLimitPerUser: Any?=null,
    @Json(name = "used_by")
    val usedBy: List<String>?=null
)