package com.shivangi.videocallingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import org.jitsi.meet.sdk.JitsiMeetView
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.PermissionListener
import java.net.URL
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity(), JitsiMeetActivityInterface {

    /**
     * Required for Jitsi. This will forward permission requests
     * (e.g. camera, microphone) to the standard requestPermissions flow.
     */
    override fun requestPermissions(
        permissions: Array<out String>?,
        requestCode: Int,
        listener: PermissionListener?
    ) {
        if (!permissions.isNullOrEmpty()) {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    /**
     * Forward callbacks so Jitsi can handle them.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun onBackPressed() {
        if (!JitsiMeetActivityDelegate.onBackPressed()) {
            // If Jitsi did not handle it, proceed normally
            super.onBackPressed()
        }
    }

    // Standard onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    // We track whether the user is in a call
                    var inCall by remember { mutableStateOf(false) }

                    if (!inCall) {
                        // A simple "Join Call" screen
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Welcome to Jitsi Compose Demo!")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { inCall = true }) {
                                Text("Join Call")
                            }
                        }
                    } else {
                        // Show the Jitsi Meet embedded composable
                        JitsiMeetComposable(
                            roomName = "testRoom", // Example static room
                            onLeave = { inCall = false }
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable that embeds a JitsiMeetView via AndroidView.
 * The Activity hosting this Composable must implement JitsiMeetActivityInterface.
 */
@Composable
fun JitsiMeetComposable(
    roomName: String,
    serverUrl: String = "https://meet.jit.si",
    onLeave: () -> Unit
) {
    // Android context. Must be the Activity implementing JitsiMeetActivityInterface
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

    // Join the call on first composition
    LaunchedEffect(roomName) {
        jitsiView.join(options)
    }

    // Dispose the view when removed from composition
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
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Button(onClick = {
                jitsiView.dispose()  // End the call
                onLeave()            // Notify the caller
            }) {
                Text("Leave")
            }
        }
    }
}
