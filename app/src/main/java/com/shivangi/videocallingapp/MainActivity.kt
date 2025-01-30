package com.shivangi.videocallingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Jitsi-required imports
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Use the VideoCallViewModel for our call state
            val videoCallViewModel: VideoCallViewModel = viewModel()
            val currentRoom = videoCallViewModel.currentRoomName.value

            // Check camera & mic permissions
            RequestPermissionsScreen {
                // If permissions are granted, proceed with app UI
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        if (currentRoom == null) {
                            // If not in a call, show a screen to enter room name
                            JoinCallScreen(
                                onJoin = { room ->
                                    videoCallViewModel.joinCall(room)
                                }
                            )
                        } else {
                            // If in a call, show the Jitsi view
                            JitsiMeetComposable(
                                roomName = currentRoom,
                                onLeave = {
                                    videoCallViewModel.leaveCall()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // -------------------------
    // Overridden Activity Methods
    // -------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Let Jitsi handle the results
        JitsiMeetActivityDelegate.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
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
        JitsiMeetActivityDelegate.onBackPressed()
        super.onBackPressed()
    }
}
