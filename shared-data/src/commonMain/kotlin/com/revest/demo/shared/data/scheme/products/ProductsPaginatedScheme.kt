package com.revest.demo.shared.data.scheme.products

import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductsPaginatedScheme(
    @SerialName("products")
    val products: List<ProductScheme>,
    @SerialName("total")
    val total: Int,
    @SerialName("skip")
    val skip: Int,
    @SerialName("limit")
    val limit: Int
)

internal fun ProductsPaginatedScheme.toDomain(): PaginatedData<Products> = 
    PaginatedData(
        page = if (limit > 0) (skip / limit) + 1 else 0,
        data = products.toDomain(),
        totalResults = total,
        totalPages = if (limit > 0) (total + limit - 1) / limit else 0
    )
