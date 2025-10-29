package com.revest.demo.shared.domain.usecase.products

import com.revest.demo.shared.domain.Result
import com.revest.demo.shared.domain.asResult
import com.revest.demo.shared.domain.core.UseCase
import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.repository.GetProductRepository
import kotlinx.coroutines.flow.Flow

public class SearchProductsUseCase(
    private val repository: GetProductRepository
) : UseCase<SearchProductsUseCase.Param, PaginatedData<Products>> {

    public data class Param(val query: String)

    override operator fun invoke(param: Param): Flow<Result<PaginatedData<Products>>> =
        repository.searchProducts(param.query).asResult()

}
