package com.revest.demo

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.revest.demo.shared.presentation.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class RevestDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        initKoin {
            androidContext(androidContext = this@RevestDemoApp)
            androidLogger(level = Level.INFO)
        }
    }

}
