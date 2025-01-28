package com.shivangi.videocallingapp

import android.app.PictureInPictureParams
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.shivangi.videocallingapp.ui.CallScreen
import com.shivangi.videocallingapp.viewmodel.CallViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CallScreen(
                viewModel = viewModel,
                onEnterPiP = { enterPipMode() },
                onStartScreenShare = { startScreenShare() }
            )
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // Called when user is about to leave the app (e.g. pressing home)
        enterPipMode()
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(16, 9)
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(params)
        }
    }

    private fun startScreenShare() {
        // Start an activity that requests MediaProjection
        val intent = Intent(this, ScreenShareActivity::class.java)
        startActivity(intent)
    }
}
