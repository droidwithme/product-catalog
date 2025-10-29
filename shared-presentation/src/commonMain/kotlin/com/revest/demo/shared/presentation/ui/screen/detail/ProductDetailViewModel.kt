package com.revest.demo.shared.presentation.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revest.demo.shared.domain.usecase.detail.GetProductDetailsUseCase
import com.revest.demo.shared.presentation.core.WhileUiSubscribed
import com.revest.demo.shared.presentation.core.toViewState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class ProductDetailViewModel(
    getProductDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: Long = savedStateHandle.get<Long>("productId")!!

    val uiState: StateFlow<ProductDetailViewState> =
        getProductDetailsUseCase(GetProductDetailsUseCase.Param(productId))
            .map { result -> ProductDetailViewState(productDetails = result.toViewState()) }
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = ProductDetailViewState()
            )
}
