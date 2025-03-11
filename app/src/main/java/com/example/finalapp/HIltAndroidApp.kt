package com.example.finalapp

import android.app.Application
import com.example.finalapp.repository.ProfileRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class HIltAndroidApp:Application() {
    override fun onCreate() {
        super.onCreate()

    }
}