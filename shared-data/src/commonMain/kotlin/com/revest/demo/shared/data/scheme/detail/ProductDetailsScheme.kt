package com.revest.demo.shared.data.scheme.detail

import com.revest.demo.shared.domain.model.detail.Dimensions
import com.revest.demo.shared.domain.model.detail.ProductDetails
import com.revest.demo.shared.domain.model.detail.Review
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductDetailsScheme(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val tags: List<String>,
    val brand: String? = null,
    val sku: String,
    val weight: Int,
    val dimensions: DimensionsScheme,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ReviewScheme>,
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val images: List<String>,
    val thumbnail: String
) {
    fun toDomain(): ProductDetails = ProductDetails(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand ?: title,
        sku = sku,
        weight = weight,
        dimensions = dimensions.toDomain(),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { it.toDomain() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
        thumbnail = thumbnail
    )
}

@Serializable
internal data class DimensionsScheme(val width: Double, val height: Double, val depth: Double) {
    fun toDomain(): Dimensions = Dimensions(width, height, depth)
}

@Serializable
internal data class ReviewScheme(
    val rating: Int, val comment: String, val reviewerName: String
) {
    fun toDomain(): Review = Review(rating, comment, reviewerName)
}
