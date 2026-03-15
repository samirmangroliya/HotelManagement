package com.samir.hotelmanagement.di

import com.samir.hotelmanagement.viewmodels.HotelViewModel
import com.samir.hotelmanagement.viewmodels.LoginViewModel
import com.samir.hotelmanagement.viewmodels.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::HotelViewModel)
}