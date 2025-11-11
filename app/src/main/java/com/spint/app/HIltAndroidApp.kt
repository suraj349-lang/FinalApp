package com.spint.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HIltAndroidApp:Application() {
    override fun onCreate() {
        super.onCreate()

    }
}