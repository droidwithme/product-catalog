package com.revest.demo.shared.domain.usecase.products

import com.revest.demo.shared.domain.Result
import com.revest.demo.shared.domain.asResult
import com.revest.demo.shared.domain.core.UseCase
import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.repository.GetProductRepository
import kotlinx.coroutines.flow.Flow

public class GetProductsUseCase(
    private val repository: GetProductRepository
) : UseCase<GetProductsUseCase.Param, PaginatedData<Products>> {

    public data class Param(
        val limit: Int,
        val skip: Int
    )

    override operator fun invoke(param: Param): Flow<Result<PaginatedData<Products>>> =
        repository.getProducts(
            limit = param.limit,
            skip = param.skip
        ).asResult()

}
