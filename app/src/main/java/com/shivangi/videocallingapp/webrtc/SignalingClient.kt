package com.shivangi.videocallingapp.webrtc

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object SignalingClient {
    private val _signals = MutableSharedFlow<String>()
    val signals: SharedFlow<String> = _signals

    suspend fun sendSignal(signalData: String) {
        // Send or emit your data
        _signals.emit(signalData)
    }

    // In a real setup, you'd also have a socket connection,
    // listener for inbound messages, etc.
}
