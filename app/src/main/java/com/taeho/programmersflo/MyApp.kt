package com.taeho.programmersflo

import android.app.Application
import com.taeho.programmersflo.di.diModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(diModule)
        }
    }
}