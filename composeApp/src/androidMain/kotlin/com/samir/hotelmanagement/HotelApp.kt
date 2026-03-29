package com.samir.hotelmanagement

import android.app.Application
import com.samir.di.initKoin
import org.koin.android.ext.koin.androidContext

class HotelApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@HotelApp)
        }
    }
}
