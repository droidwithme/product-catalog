package com.revest.demo.shared.domain.usecase

import app.cash.turbine.test
import com.revest.demo.shared.domain.Result
import com.revest.demo.shared.domain.TestPaginatedData
import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.repository.GetProductRepository
import com.revest.demo.shared.domain.usecase.products.SearchProductsUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs

class SearchProductTest {

    private companion object {
        val TestParam = SearchProductsUseCase.Param(query = "query")
    }

    private val repository = mock<GetProductRepository>()

    private val useCase = SearchProductsUseCase(repository = repository)

    @Test
    @JsName(name = "result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every {
            repository.searchProducts(query = any())
        } returns flowOf(TestPaginatedData)

        useCase(param = TestParam).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName(name = "result_flow_emits_Success")
    fun `result flow emits Success`() = runTest {
        every {
            repository.searchProducts(query = any())
        } returns flowOf(TestPaginatedData)

        useCase(param = TestParam).test {
            assertIs<Result.Success<PaginatedData<Products>>>(value = expectMostRecentItem())
            awaitComplete()

        }
    }

    @Test
    @JsName(name = "result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every {
            repository.searchProducts(query = any())
        } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
