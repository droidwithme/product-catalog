package com.revest.demo.shared.domain.repository

import com.revest.demo.shared.domain.model.detail.ProductDetails
import kotlinx.coroutines.flow.Flow

public interface GetProductDetailsRepository {
    public fun getProductDetails(productId: Long): Flow<ProductDetails>
}
