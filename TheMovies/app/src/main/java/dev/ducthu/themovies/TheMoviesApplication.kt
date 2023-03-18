package dev.ducthu.themovies

import android.app.Application
import timber.log.Timber

class TheMoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}