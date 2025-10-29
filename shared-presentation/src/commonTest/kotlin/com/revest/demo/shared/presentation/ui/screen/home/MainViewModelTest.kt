package com.revest.demo.shared.presentation.ui.screen.home

import app.cash.turbine.test
import com.revest.demo.shared.domain.Result // <-- IMPORTANT: Import your Result wrapper
import com.revest.demo.shared.domain.model.PaginatedData
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.repository.GetProductRepository
// It's better to mock the use case interfaces, not create real ones
import com.revest.demo.shared.domain.usecase.products.GetProductsUseCase
import com.revest.demo.shared.domain.usecase.products.SearchProductsUseCase
import com.revest.demo.shared.presentation.TestPaginatedData
import com.revest.demo.shared.presentation.core.ViewState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    // 1. Mock the use case INTERFACES directly. This is cleaner.
    private val getProductsUseCase: GetProductRepository = mock()
    private val searchProductsUseCase: GetProductRepository = mock()
    private val testDispatcher = StandardTestDispatcher()

    // 2. Set up Coroutine Dispatcher for testing. This is crucial for ViewModels.
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @JsName("product_loading_state_is_emitted_first")
    fun `product loading state is emitted first`() = runTest {
        // Arrange: Make the use case return an empty flow that never completes
        every { getProductsUseCase.getProducts(any(), any()) } returns flowOf()

        // Act & Assert
        HomeViewModel(
            getProductsUseCase = GetProductsUseCase(getProductsUseCase),
            searchProductsUseCase = SearchProductsUseCase(getProductsUseCase)
        ).uiState.test {
            // Assert: The very first item is the Loading state
            awaitItem().validateLoading()

            // Optional: Ensure no other items are emitted
            expectNoEvents()
        }
    }

    @Test
    @JsName("product_loading_success")
    fun `product loading success`() = runTest {
        // Arrange: Prepare the successful result from the use case
        val successResult = Result.Success(TestPaginatedData)
        every { getProductsUseCase.getProducts(any(), any()) } returns flowOf(TestPaginatedData)

        // Act
        HomeViewModel(
            getProductsUseCase = GetProductsUseCase(getProductsUseCase),
            searchProductsUseCase = SearchProductsUseCase(getProductsUseCase)
        ).uiState.test {
            // Assert
            // 3. ⭐ THE FIX: Skip the initial Loading state ⭐
            skipItems(1)

            // Now, await the next item, which should be the Success state
            val successState = awaitItem()
            successState.validateSuccess()

            // You can also add more specific assertions
            val products = (successState.productsViewState as ViewState.Success).data
            assertEquals(TestPaginatedData.data, products)
        }
    }


    // --- Helper Functions ---

    private fun HomeViewState.validateLoading() {
        // When loading, searchResults should be empty, not loading.
        assertIs<ViewState.Success<List<Products>>>(
            value = searchResults,
            message = "searchResults should be empty Success on initial load"
        )
        assertEquals(true, (searchResults as ViewState.Success).data.isEmpty())

        assertIs<ViewState.Loading>(
            value = productsViewState,
            message = "productsViewState should be Loading"
        )
        assertNull(actual = error)
    }

    private fun HomeViewState.validateSuccess() {
        // On success, searchResults should still be the initial empty success state
        assertIs<ViewState.Success<List<Products>>>(
            value = searchResults,
            message = "searchResults should be empty Success after product load"
        )

        assertIs<ViewState.Success<List<Products>>>(
            value = productsViewState,
            message = "productsViewState should be Success"
        )
        assertNull(actual = error)
    }
}
