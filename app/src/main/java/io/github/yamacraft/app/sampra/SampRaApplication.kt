package io.github.yamacraft.app.sampra

import android.app.Application
import timber.log.Timber

@Suppress("unused")
class SampRaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
