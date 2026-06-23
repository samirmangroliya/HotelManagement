package com.samir.di

import com.samir.domain.usercase.CreateBookingUseCase
import com.samir.domain.usercase.GetBookingsUseCase
import com.samir.domain.usercase.GetHotelByIdUseCase
import com.samir.domain.usercase.GetRoomsUseCase
import com.samir.domain.usercase.HotelUseCase
import com.samir.domain.usercase.LoginUseCase
import com.samir.domain.usercase.RegisterUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val userCaseModules = module {
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::HotelUseCase)
    factoryOf(::GetRoomsUseCase)
    factoryOf(::CreateBookingUseCase)
    factoryOf(::GetBookingsUseCase)
    factoryOf(::GetHotelByIdUseCase)
}
