package com.shivangi.videocallingapp

import android.app.Application
// If using Hilt or other DI, you’d add annotations here (e.g., @HiltAndroidApp)

class MyVideoCallApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize anything at application start, if needed
    }
}
