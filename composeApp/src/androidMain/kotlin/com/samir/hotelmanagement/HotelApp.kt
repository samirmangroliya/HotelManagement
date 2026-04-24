package com.samir.hotelmanagement

import android.app.Application
import com.samir.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

class HotelApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
        initKoin {
            androidContext(this@HotelApp)
        }
    }
}
