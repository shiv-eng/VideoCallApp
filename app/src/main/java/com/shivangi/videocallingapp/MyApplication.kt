package com.shivangi.videocallingapp

import android.app.Application
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Optionally set default Jitsi config
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si"))
            // Example: disable the invite feature globally
            .setFeatureFlag("invite.enabled", false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }
}
