package com.revest.demo.shared.domain.repository

import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import kotlinx.coroutines.flow.Flow

public interface GetProductRepository {

    public fun getProducts(
        limit: Int,
        skip: Int
    ): Flow<PaginatedData<Products>>

    public fun searchProducts(query: String): Flow<PaginatedData<Products>>

}
