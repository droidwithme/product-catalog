package com.revest.demo.shared.presentation.di

import com.revest.demo.shared.domain.usecase.detail.GetProductDetailsUseCase
import com.revest.demo.shared.domain.usecase.products.GetProductsUseCase
import com.revest.demo.shared.domain.usecase.products.SearchProductsUseCase
import org.koin.dsl.module

internal val useCaseModule = module {


    single {
        GetProductsUseCase(repository = get())
    }

    single {
        SearchProductsUseCase(repository = get())
    }

    single {
        GetProductDetailsUseCase(repository = get())
    }



}
