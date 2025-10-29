package com.revest.demo.shared.presentation.di

import com.revest.demo.shared.presentation.ui.screen.detail.ProductDetailViewModel
import com.revest.demo.shared.presentation.ui.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductDetailViewModel)

    /*viewModel { params ->
        KeywordMoviesViewModel(
            pager = KeywordMoviesPager(
                keywordId = params.get(),
                useCase = get(),
            )
        )
    }*/
}
