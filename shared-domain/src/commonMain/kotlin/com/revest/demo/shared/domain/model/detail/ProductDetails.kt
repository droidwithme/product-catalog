package com.revest.demo.shared.domain.model.detail

public data class ProductDetails(
    val id: Long, val title: String, val description: String, val category: String,
    val price: Double, val discountPercentage: Double, val rating: Double, val stock: Long,
    val tags: List<String>, val brand: String?, val sku: String, val weight: Int,
    val dimensions: Dimensions, val warrantyInformation: String, val shippingInformation: String,
    val availabilityStatus: String, val reviews: List<Review>,
    val returnPolicy: String, val minimumOrderQuantity: Int,
    val images: List<String>, val thumbnail: String
)

public data class Dimensions(val width: Double, val height: Double, val depth: Double)

public data class Review(
    val rating: Int, val comment: String,
    val reviewerName: String
)