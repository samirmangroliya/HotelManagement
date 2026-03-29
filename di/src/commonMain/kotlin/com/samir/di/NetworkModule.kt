package com.samir.di

import com.samir.network.api.ApiService
import com.samir.network.client.HttpClientFactory
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create() }
    single { ApiService(get()) }
}
