package com.samir.di

import com.samir.hotelmanagement.viewmodels.HotelViewModel
import com.samir.viewmodels.BookingViewModel
import com.samir.viewmodels.LoginViewModel
import com.samir.viewmodels.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    factoryOf(::LoginViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::BookingViewModel)

    single { HotelViewModel(get()) }
}