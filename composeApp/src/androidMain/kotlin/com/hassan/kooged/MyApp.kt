package com.hassan.kooged

import android.app.Application
import com.hassan.kooged.di.initKoin
import org.koin.android.ext.koin.androidContext


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApp)
        }
    }


}