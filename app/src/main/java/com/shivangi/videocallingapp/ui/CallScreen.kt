package com.shivangi.videocallingapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shivangi.videocallingapp.viewmodel.CallViewModel

@Composable
fun CallScreen(
    viewModel: CallViewModel,
    onEnterPiP: () -> Unit,
    onStartScreenShare: () -> Unit
) {
    val uiState by viewModel.callUiState.collectAsState()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Local Video Box
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(150.dp)
                ) {
                    // In a real app, embed a SurfaceViewRenderer or TextureView
                    // using AndroidView to show local video track
                    Text(text = "Local Video (placeholder)")
                }

                Spacer(Modifier.height(16.dp))

                // Remote Video Box
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(200.dp)
                ) {
                    // In a real app, embed a remote video track similarly
                    Text(text = "Remote Video (placeholder)")
                }

                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.startCall() }) {
                    Text(text = "Start Call")
                }

                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.endCall() }) {
                    Text(text = "End Call")
                }

                Spacer(Modifier.height(16.dp))
                Button(onClick = onEnterPiP) {
                    Text(text = "Enter PiP Mode")
                }

                Spacer(Modifier.height(16.dp))
                Button(onClick = onStartScreenShare) {
                    Text(text = "Share Screen")
                }

                Spacer(Modifier.height(24.dp))
                Text(text = uiState.statusMessage)
            }
        }
    }
}
