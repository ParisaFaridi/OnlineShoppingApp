package com.example.onlineshoppingapp.data.model


import com.squareup.moshi.Json

data class Order(
    @Json(name = "billing")
    val billing: Billing? = null,
    @Json(name = "cart_hash")
    val cartHash: String? = null,
    @Json(name = "cart_tax")
    val cartTax: String? = null,
    @Json(name = "coupon_lines")
    val couponLines: List<Any>? = null,
    @Json(name = "created_via")
    val createdVia: String?= null,
    @Json(name = "currency")
    val currency: String?= null,
    @Json(name = "currency_symbol")
    val currencySymbol: String?= null,
    @Json(name = "customer_id")
    val customerId: Int=0,
    @Json(name = "customer_ip_address")
    val customerIpAddress: String?= null,
    @Json(name = "customer_note")
    val customerNote: String?= null,
    @Json(name = "customer_user_agent")
    val customerUserAgent: String?= null,
    @Json(name = "discount_tax")
    val discountTax: String?= null,
    @Json(name = "discount_total")
    val discountTotal: String?= null,
    @Json(name = "fee_lines")
    val feeLines: List<Any>?= null,
    @Json(name = "id")
    val id: Int=0,
    @Json(name = "line_items")
    val lineItems: List<LineItem>?= null,
    @Json(name = "_links")
    val links: Links?= null,
    @Json(name = "meta_data")
    val metaData: List<Any>?= null,
    @Json(name = "number")
    val number: String?= null,
    @Json(name = "order_key")
    val orderKey: String?= null,
    @Json(name = "parent_id")
    val parentId: Int=0,
    @Json(name = "payment_method")
    val paymentMethod: String?= null,
    @Json(name = "payment_method_title")
    val paymentMethodTitle: String?= null,
    @Json(name = "prices_include_tax")
    val pricesIncludeTax: Boolean?= null,
    @Json(name = "refunds")
    val refunds: List<Any>?= null,
    @Json(name = "shipping")
    val shipping: Shipping?= null,
    @Json(name = "shipping_lines")
    val shippingLines: List<Any>?= null,
    @Json(name = "shipping_tax")
    val shippingTax: String?= null,
    @Json(name = "shipping_total")
    val shippingTotal: String?= null,
    @Json(name = "status")
    val status: String?= "pending",
    @Json(name = "tax_lines")
    val taxLines: List<Any>?= null,
    @Json(name = "total")
    val total: String?= null,
    @Json(name = "total_tax")
    val totalTax: String?= null
)