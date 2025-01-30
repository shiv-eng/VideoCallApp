package com.shivangi.videocallingapp

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import org.jitsi.meet.sdk.JitsiMeetView
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

@Composable
fun JitsiMeetComposable(
    roomName: String,
    onLeave: () -> Unit,
    serverUrl: String = "https://meet.jit.si"
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Create the Jitsi view once
    val jitsiView = remember {
        JitsiMeetView(context)
    }

    // Build the conference options
    val options = remember(roomName) {
        JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL(serverUrl))
            .setRoom(roomName)
            // Example: disable invite
            .setFeatureFlag("invite.enabled", false)
            .build()
    }

    // Join call on first composition
    LaunchedEffect(roomName) {
        jitsiView.join(options)
    }

    // Dispose the view when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            jitsiView.dispose()
        }
    }

    Box(Modifier.fillMaxSize()) {
        // Host the JitsiMeetView in Compose
        AndroidView(
            factory = { jitsiView },
            modifier = Modifier.fillMaxSize()
        )

        // A "Leave" button on top
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            Button(onClick = {
                jitsiView.dispose() // End call
                onLeave()           // Update VM state
            }) {
                Text("Leave")
            }
        }
    }
}
