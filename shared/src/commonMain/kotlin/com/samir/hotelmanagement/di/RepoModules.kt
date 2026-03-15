package com.samir.hotelmanagement.di

import com.samir.hotelmanagement.data.repo.BookingRepositoryImpl
import com.samir.hotelmanagement.data.repo.HotelRepositoryImpl
import com.samir.hotelmanagement.data.repo.LoginRepositoryImpl
import com.samir.hotelmanagement.data.repo.RegisterRepositoryImpl
import com.samir.hotelmanagement.domain.repo.BookingRepository
import com.samir.hotelmanagement.domain.repo.HotelRepository
import com.samir.hotelmanagement.domain.repo.LoginRepository
import com.samir.hotelmanagement.domain.repo.RegisterRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val repoModules = module {
    factoryOf(::RegisterRepositoryImpl) { bind<RegisterRepository>() }
    factoryOf(::LoginRepositoryImpl) { bind<LoginRepository>() }
    factoryOf(::HotelRepositoryImpl) { bind<HotelRepository>() }
    factoryOf(::BookingRepositoryImpl) { bind<BookingRepository>() }
}
