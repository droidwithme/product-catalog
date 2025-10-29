package com.revest.demo.shared.presentation.ui.screen.home

import androidx.compose.runtime.Stable
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.presentation.core.ViewState

@Stable
internal data class HomeViewState(
    val productsViewState: ViewState<List<Products>> = ViewState.Loading,
    val searchResults: ViewState<List<Products>> = ViewState.Success(emptyList()),
    val searchQuery: String = "",
    val searchActive: Boolean = false,
    val error: Throwable? = null,
)
