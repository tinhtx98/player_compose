package com.tinhtx.player

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TinhTXPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}