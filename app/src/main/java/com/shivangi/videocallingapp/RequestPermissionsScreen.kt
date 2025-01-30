package com.shivangi.videocallingapp

import android.Manifest
import androidx.compose.runtime.*
import com.google.accompanist.permissions.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsScreen(
    content: @Composable () -> Unit
) {
    // Ask for camera & mic
    val permsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    val allGranted = permsState.allPermissionsGranted

    // If not granted, request immediately
    LaunchedEffect(Unit) {
        if (!allGranted) {
            permsState.launchMultiplePermissionRequest()
        }
    }

    if (allGranted) {
        // If user granted everything, show the main UI
        content()
    } else {
        // Show a simple message + button to request again
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Camera/Microphone required for video calls.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    permsState.launchMultiplePermissionRequest()
                }) {
                    Text("Grant Permissions")
                }
            }
        }
    }
}
