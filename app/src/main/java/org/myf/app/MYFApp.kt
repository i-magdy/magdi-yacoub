package org.myf.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MYFApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}