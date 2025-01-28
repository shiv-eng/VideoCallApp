package com.shivangi.videocallingapp.webrtc

import android.util.Log
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

class WebRtcManager {

    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var videoSource: VideoSource? = null
    private var videoTrack: VideoTrack? = null
    private var audioSource: AudioSource? = null
    private var audioTrack: AudioTrack? = null
    private var peerConnection: PeerConnection? = null

    private var eglBase: EglBase? = null

    fun initPeerConnectionFactory() {
        // Initialize
        val options = PeerConnectionFactory.InitializationOptions
            .builder(/* applicationContext= */null)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        eglBase = EglBase.create()

        val encoderFactory = DefaultVideoEncoderFactory(
            eglBase?.eglBaseContext,
            /* enableIntelVp8Encoder= */true,
            /* enableH264HighProfile= */true
        )
        val decoderFactory = DefaultVideoDecoderFactory(eglBase?.eglBaseContext)

        val pcOptions = PeerConnectionFactory.Options()
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(pcOptions)
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()
    }

    fun startLocalVideoCapture() {
        // Create capturer (front camera if found)
        val videoCapturer: VideoCapturer? = createCameraCapturer()
        if (videoCapturer == null) {
            Log.e("WebRtcManager", "No suitable camera capturer found.")
            return
        }

        // Create video source
        videoSource = peerConnectionFactory?.createVideoSource(videoCapturer.isScreencast)
        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase?.eglBaseContext)
        videoCapturer.initialize(surfaceTextureHelper, null, videoSource?.capturerObserver)
        videoCapturer.startCapture(640, 480, 30)

        // Video track
        videoTrack = peerConnectionFactory?.createVideoTrack("VIDEO", videoSource)

        // Audio track
        audioSource = peerConnectionFactory?.createAudioSource(MediaConstraints())
        audioTrack = peerConnectionFactory?.createAudioTrack("AUDIO", audioSource)

        // In a real app: create peerConnection, add these tracks, set up ICE servers, etc.
    }

    private fun createCameraCapturer(): VideoCapturer? {
        // Example using Camera2Enumerator:
        val enumerator = org.webrtc.Camera2Enumerator(null)
        val deviceNames = enumerator.deviceNames

        // Attempt front camera
        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                val capturer = enumerator.createCapturer(deviceName, null)
                if (capturer != null) return capturer
            }
        }
        // Attempt any other camera
        for (deviceName in deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                val capturer = enumerator.createCapturer(deviceName, null)
                if (capturer != null) return capturer
            }
        }
        return null
    }

    fun stopCall() {
        try {
            videoTrack?.dispose()
            videoSource?.dispose()
            audioTrack?.dispose()
            audioSource?.dispose()
            peerConnection?.close()
            peerConnection = null
        } catch (e: Exception) {
            Log.e("WebRtcManager", "Error stopping call: ${e.message}")
        } finally {
            videoTrack = null
            videoSource = null
            audioTrack = null
            audioSource = null
        }
    }
}
