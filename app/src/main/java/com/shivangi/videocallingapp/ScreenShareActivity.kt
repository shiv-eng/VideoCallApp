package com.shivangi.videocallingapp

import android.app.Activity
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService

class ScreenShareActivity : AppCompatActivity() {

    private val REQUEST_CODE_MEDIA_PROJECTION = 1001
    private var mediaProjectionManager: MediaProjectionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start the media projection request
        mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
            mediaProjectionManager?.createScreenCaptureIntent(),
            REQUEST_CODE_MEDIA_PROJECTION
        )

        // Provide some minimal Compose UI
        setContent {
            ScreenSharePlaceholderUI()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val mediaProjection = mediaProjectionManager?.getMediaProjection(resultCode, data)
                // TODO: Integrate mediaProjection with WebRTC for screen sharing
            } else {
                // User canceled or permission denied
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

@Composable
fun ScreenSharePlaceholderUI() {
    val context = LocalContext.current
    Text(text = "Screen Sharing Activity. Implement your UI here.")
}
