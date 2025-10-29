package com.revest.demo.shared.presentation.ui.screen.home

import app.cash.turbine.test
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.repository.GetProductRepository
import com.revest.demo.shared.domain.usecase.products.GetProductsUseCase
import com.revest.demo.shared.domain.usecase.products.SearchProductsUseCase
import com.revest.demo.shared.presentation.TestPaginatedData
import com.revest.demo.shared.presentation.core.ViewState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HomeViewModelTest {

    private val repository = mock<GetProductRepository>()
    private val useCase = GetProductsUseCase(repository = repository)
    private val searchProductsUseCase = SearchProductsUseCase(repository = repository)

    @Test
    @JsName(name = "product loading")
    fun `product loading`() = runTest {
        every {
            repository.getProducts(
                limit = any(), skip = any()
            )
        } returns flowOf()

        HomeViewModel(
            getProductsUseCase = useCase,
            searchProductsUseCase = searchProductsUseCase
        ).uiState.test {
            awaitItem().validateLoading()
        }
    }

    private fun HomeViewState.validateLoading() {
        assertIs<ViewState.Loading>(
            value = searchResults,
            message = "searchResults"
        )
        assertIs<ViewState.Loading>(
            value = productsViewState,
            message = "productsViewState"
        )
        assertNull(actual = error)
    }


    @Test
    @JsName(name = "product_loading_success")
    fun `product loading success`() = runTest {
        every {
            repository.getProducts(
                limit = any(), skip = any()
            )
        } returns flowOf(value = TestPaginatedData)

        HomeViewModel(
            getProductsUseCase = useCase,
            searchProductsUseCase = searchProductsUseCase
        ).uiState.test {
            skipItems(count = 0)
            awaitItem().validateSuccess()
        }
    }


    private fun HomeViewState.validateSuccess() {
        assertIs<ViewState.Success<List<Products>>>(
            value = searchResults,
            message = "searchResults"
        )
        assertIs<ViewState.Success<List<Products>>>(
            value = productsViewState,
            message = "productsViewState"
        )
        assertNull(actual = error)
    }

    private fun HomeViewState.validateError() {
        assertIs<ViewState.Error>(
            value = searchResults,
            message = "searchResults"
        )
        assertIs<ViewState.Error>(
            value = productsViewState,
            message = "productsViewState"
        )
        assertNotNull(actual = error)
    }
}
