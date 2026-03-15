package com.samir.hotelmanagement.di

import com.samir.hotelmanagement.domain.usercase.HotelUseCase
import com.samir.hotelmanagement.domain.usercase.LoginUseCase
import com.samir.hotelmanagement.domain.usercase.RegisterUseCase
import com.samir.hotelmanagement.viewmodels.HotelViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val userCaseModules = module {
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::HotelUseCase)
}