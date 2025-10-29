package com.revest.demo.shared.presentation.ui.screen.detail

import androidx.compose.runtime.Stable
import com.revest.demo.shared.domain.model.detail.ProductDetails
import com.revest.demo.shared.presentation.core.ViewState

@Stable
internal data class ProductDetailViewState(
    val productDetails: ViewState<ProductDetails> = ViewState.Loading
)
