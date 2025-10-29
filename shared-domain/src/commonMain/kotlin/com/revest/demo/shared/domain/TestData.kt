package com.revest.demo.shared.domain

import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products


public val testProduct: List<Products> = buildList {
    repeat(times = 5) { id -> add(testItem(id = id.toLong())) }
}

public fun testItem(id: Long = 0): Products = Products(
    id = id,
    title = "",
    description = "",
    price = 23.0,
    discountPercentage = 2.3,
    rating = 2.3,
    stock = 2,
    brand = "",
    category = "",
    thumbnail = "",
    images = listOf(),
)

public val TestPaginatedData: PaginatedData<Products> = PaginatedData(
    page = 1,
    data = testProduct,
    totalResults = testProduct.size,
    totalPages = 1,
)


