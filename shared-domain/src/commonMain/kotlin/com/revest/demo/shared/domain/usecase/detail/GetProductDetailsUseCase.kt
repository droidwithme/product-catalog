package com.revest.demo.shared.domain.usecase.detail

import com.revest.demo.shared.domain.Result
import com.revest.demo.shared.domain.asResult
import com.revest.demo.shared.domain.core.UseCase
import com.revest.demo.shared.domain.model.detail.ProductDetails
import com.revest.demo.shared.domain.repository.GetProductDetailsRepository
import kotlinx.coroutines.flow.Flow

public class GetProductDetailsUseCase(
    private val repository: GetProductDetailsRepository
) : UseCase<GetProductDetailsUseCase.Param, ProductDetails> {

    public data class Param(val productId: Long)

    override operator fun invoke(param: Param): Flow<Result<ProductDetails>> =
        repository.getProductDetails(param.productId).asResult()

}
