package com.revest.demo.shared.data.repoImpl

import com.revest.demo.shared.data.extensions.getFlow
import com.revest.demo.shared.data.scheme.products.ProductsPaginatedScheme
import com.revest.demo.shared.data.scheme.products.toDomain
import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.repository.GetProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class GetProductRepoImpl(private val client: HttpClient) : GetProductRepository {

    override fun getProducts(
        limit: Int,
        skip: Int
    ): Flow<PaginatedData<Products>> = client.getFlow<ProductsPaginatedScheme> {
        url(scheme = "https", host = "dummyjson.com", path = "/products") {
            parameter(key = "limit", value = limit)
            parameter(key = "skip", value = skip)
        }
    }.map(ProductsPaginatedScheme::toDomain)

    override fun searchProducts(query: String): Flow<PaginatedData<Products>> = client.getFlow<ProductsPaginatedScheme> {
        url(scheme = "https", host = "dummyjson.com", path = "/products/search") {
            parameter(key = "q", value = query)
        }
    }.map(ProductsPaginatedScheme::toDomain)

}
