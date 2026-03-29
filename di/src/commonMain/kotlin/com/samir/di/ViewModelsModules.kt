package com.samir.di

import com.samir.viewmodels.HotelViewModel
import com.samir.viewmodels.LoginViewModel
import com.samir.viewmodels.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::HotelViewModel)
}