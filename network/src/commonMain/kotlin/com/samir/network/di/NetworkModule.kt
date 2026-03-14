package com.samir.network.di

import com.samir.network.api.ApiService
import com.samir.network.client.HttpClientFactory
import com.samir.network.repository.ApiRepository
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create() }
    single { ApiService(get()) }
    single { ApiRepository(get()) }
}
