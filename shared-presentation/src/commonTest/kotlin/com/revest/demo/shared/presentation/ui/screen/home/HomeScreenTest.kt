package com.revest.demo.shared.presentation.ui.screen.home

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.revest.demo.shared.domain.repository.GetProductRepository
import com.revest.demo.shared.domain.usecase.products.GetProductsUseCase
import com.revest.demo.shared.domain.usecase.products.SearchProductsUseCase
import com.revest.demo.shared.presentation.TestPaginatedData
import com.revest.demo.shared.presentation.ui.theme.RevestAppTheme
import com.revest.demo.shared.presentation.ui.widgets.products.ProductsListDefaults
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atMost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import revestdemo.shared_presentation.generated.resources.Res
import revestdemo.shared_presentation.generated.resources.app_name
import kotlin.js.JsName
import kotlin.test.Test


@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenTest {

    private val repository = mock<GetProductRepository> {
        every { getProducts(any(), any()) } returns emptyFlow()
    }

    private val loadProductsUseCase = GetProductsUseCase(repository)
    private val searchProductsUseCase = SearchProductsUseCase(repository)

    @OptIn(ExperimentalTestApi::class)
    @Test
    @JsName("check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        waitForIdle()
        val expectedTitle = runBlocking { getString(Res.string.app_name) }
        onNodeWithText(text = expectedTitle)
            .assertExists("No Title component found.")
            .assertIsDisplayed()
    }


    @OptIn(ExperimentalTestApi::class)
    @Test
    @JsName(name = "check_success_state")
    fun `check success state`() = runComposeUiTest {
        every {
            repository.getProducts(
                limit = any(), skip = any()
            )
        } returns flowOf(value = TestPaginatedData)
        testContent()
        onAllNodesWithTag(testTag = ProductsListDefaults.list)
            .assertCountEquals(expectedSize = 5)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun ComposeUiTest.testContent(
        onSearch: (Long) -> Unit = {},
    ) = setContent {
        RevestAppTheme {
            Home(
                onProductDetails = onSearch,
                viewModel = HomeViewModel(
                    getProductsUseCase = loadProductsUseCase,
                    searchProductsUseCase = searchProductsUseCase
                )
            )
        }
    }
}
