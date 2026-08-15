package com.samir.core

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platform() = "Android"

actual val platformModule: Module = module {
    single<PreferenceManager> { AndroidPreferenceManager(get()) }
}
