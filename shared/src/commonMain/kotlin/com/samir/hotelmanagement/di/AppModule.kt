package com.samir.hotelmanagement.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(config: KoinAppDeclaration) {
    startKoin {
        config.invoke(this)
        modules(appModules)
    }
}

val appModules = module {
    includes(viewModelModule, userCaseModules, repoModules, networkModule)
}
