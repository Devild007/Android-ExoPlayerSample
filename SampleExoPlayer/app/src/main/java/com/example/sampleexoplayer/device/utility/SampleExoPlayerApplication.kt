package com.example.sampleexoplayer.device.utility

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class SampleExoPlayerApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}