package com.revest.demo.shared.presentation.ui.screen.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revest.demo.shared.domain.Result
import com.revest.demo.shared.domain.model.products.Products
import com.revest.demo.shared.domain.usecase.products.GetProductsUseCase
import com.revest.demo.shared.domain.usecase.products.SearchProductsUseCase
import com.revest.demo.shared.presentation.core.WhileUiSubscribed
import com.revest.demo.shared.presentation.core.toViewState
import com.revest.demo.shared.presentation.core.toViewStatePaginated
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.FlowPreview::class)
@Stable
internal class HomeViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(null)
    private val _products = MutableStateFlow<List<Products>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _searchActive = MutableStateFlow(false)

    private var currentPage = 0
    private var totalPages = 1
    private var isFetching = false

    private val searchResults = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isNotBlank()) {
                searchProductsUseCase(SearchProductsUseCase.Param(query))
            } else {
                MutableStateFlow(Result.Success(com.revest.demo.shared.domain.model.PaginatedData(0, emptyList(), 0, 0)))
            }
        }
        .stateIn(viewModelScope, WhileUiSubscribed, Result.Loading)

    val error: StateFlow<Throwable?> = _error
    val uiState: StateFlow<HomeViewState> = combine(
        _products,
        searchResults,
        _searchQuery,
        _searchActive,
        error
    ) { products, searchResults, query, active, error ->
        HomeViewState(
            productsViewState = products.toViewState(),
            searchResults = searchResults.toViewStatePaginated(),
            searchQuery = query,
            searchActive = active,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = HomeViewState(),
    )

    init {
        loadMore()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSearchActiveChange(active: Boolean) {
        _searchActive.value = active
    }

    fun loadMore() {
        if (isFetching || currentPage >= totalPages) return

        viewModelScope.launch {
            isFetching = true
            getProductsUseCase(param = GetProductsUseCase.Param(limit = 10, skip = currentPage * 10))
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val response = result.value
                            if (response.data.isNotEmpty()) {
                                _products.value += response.data
                                totalPages = response.totalPages
                                currentPage++
                            }
                        }
                        is Result.Error -> {
                            _error.value = result.exception
                        }
                        Result.Loading -> { /* No-op */ }
                    }
                    isFetching = false
                }
        }
    }

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
