package com.revest.demo.shared.data.repoImpl

import com.revest.demo.shared.data.extensions.getFlow
import com.revest.demo.shared.data.scheme.detail.ProductDetailsScheme
import com.revest.demo.shared.domain.model.detail.ProductDetails
import com.revest.demo.shared.domain.repository.GetProductDetailsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class GetProductDetailsRepoImpl(private val client: HttpClient) : GetProductDetailsRepository {
    override fun getProductDetails(productId: Long): Flow<ProductDetails> = client.getFlow<ProductDetailsScheme> {
        url(scheme = "https", host = "dummyjson.com", path = "/products/$productId")
    }.map { it.toDomain() }
}
