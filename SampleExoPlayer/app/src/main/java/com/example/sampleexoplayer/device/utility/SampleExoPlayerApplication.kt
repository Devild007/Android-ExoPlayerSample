package com.example.sampleexoplayer.device.utility

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.example.sampleexoplayer.device.utility.SampleExoPlayerApplication
import androidx.multidex.MultiDex

class SampleExoPlayerApplication : MultiDexApplication() {
    companion object {
        var instance: SampleExoPlayerApplication? = null

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
    }
}