package com.revest.demo.shared.data.scheme.products

import com.revest.demo.shared.domain.model.products.Products
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductScheme(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Double,
    @SerialName("discountPercentage")
    val discountPercentage: Double,
    @SerialName("rating")
    val rating: Double,
    @SerialName("stock")
    val stock: Int,
    @SerialName("brand")
    val brand: String? = null,
    @SerialName("category")
    val category: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("images")
    val images: List<String>
)

internal fun List<ProductScheme>.toDomain(): List<Products> = map(ProductScheme::toDomain)

internal fun ProductScheme.toDomain(): Products = Products(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    brand = brand ?: title,
    category = category,
    thumbnail = thumbnail,
    images = images
)
