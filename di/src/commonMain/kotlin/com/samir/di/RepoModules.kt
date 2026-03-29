package com.samir.di

import com.samir.data.remote.repo.BookingRepositoryImpl
import com.samir.data.remote.repo.HotelRepositoryImpl
import com.samir.data.remote.repo.LoginRepositoryImpl
import com.samir.data.remote.repo.RegisterRepositoryImpl
import com.samir.domain.repo.BookingRepository
import com.samir.domain.repo.HotelRepository
import com.samir.domain.repo.LoginRepository
import com.samir.domain.repo.RegisterRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val repoModules = module {
    factoryOf(::RegisterRepositoryImpl) { bind<RegisterRepository>() }
    factoryOf(::LoginRepositoryImpl) { bind<LoginRepository>() }
    factoryOf(::HotelRepositoryImpl) { bind<HotelRepository>() }
    factoryOf(::BookingRepositoryImpl) { bind<BookingRepository>() }
}
