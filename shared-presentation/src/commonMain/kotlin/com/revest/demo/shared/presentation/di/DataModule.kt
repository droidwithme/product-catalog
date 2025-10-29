package com.revest.demo.shared.presentation.di

import com.revest.demo.shared.data.repoImpl.GetProductDetailsRepoImpl
import com.revest.demo.shared.data.repoImpl.GetProductRepoImpl
import com.revest.demo.shared.domain.repository.GetProductDetailsRepository
import com.revest.demo.shared.domain.repository.GetProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val dataModule = module {
    single {
        HttpClient {
            install(HttpCache)
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    single<GetProductRepository> {
        GetProductRepoImpl(client = get())
    }

    single<GetProductDetailsRepository> {
        GetProductDetailsRepoImpl(client = get())
    }




}
